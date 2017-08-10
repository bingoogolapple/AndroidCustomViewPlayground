package cn.bingoogolapple.testservice.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import cn.bingoogolapple.testservice.R;
import cn.bingoogolapple.testservice.TestServiceAidlInterface;
import cn.bingoogolapple.testservice.activity.MainActivity;
import cn.bingoogolapple.testservice.activity.ServiceActivity;

/**
 * 1. BroadcastReceiver 能实现进程间通信，LocalBroadcastReceiver 不能实现进程间通信
 * 2. 多个 Activity 都调用了 startService，在其中一个 Activity 里调用了 stopService 后，这个 Service 会停止（前提是该 Service 没有和任何一个 Activity bind 的情况下）
 * 3. 「startService -> onCreate -> onStartCommand -> startService -> onStartCommand」----「stopService -> onDestroy」
 * 4. 「bindService -> onCreate -> onBind -> onServiceConnected」----「unbindService -> onDestroy」
 * 5. 「startService -> onCreate -> onStartCommand -> bindService -> onBind -> onServiceConnected」----「unbindService -> stopService -> onDestroy」
 */
public class TestService extends Service {
    private static final String TAG = TestService.class.getSimpleName();

    public static final String ACTION_SERVICE_RECEIVE = "ACTION_SERVICE_RECEIVE";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive");
            Intent broadcastIntent = new Intent(ServiceActivity.ACTION_ACTIVITY_RECEIVE);
            broadcastIntent.putExtra("ext", "我是 Service BroadcastReceiver onReceive 扩展信息" + System.currentTimeMillis());
            sendBroadcast(broadcastIntent);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, Thread.currentThread().getName() + " onCreate");

        registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_SERVICE_RECEIVE));

        createForegroundService();

        /**
         * 1. Activity 是 5 秒，BroadcastReceiver 是 10 秒，Service 是 20 秒会报 ANR
         * 2. 这是运行在该进程的主线程里的，如果是单进程的话会导致超过 5 秒回导致 ANR
         * 3. 如果是多进程的话只会阻塞该进程中的主线程，并不会影响到当前的应用程序的主线程，但超过 20 秒还是会导致 ANR
         */
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * 创建前台进程
     */
    private void createForegroundService() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker("Ticker")
                .setContentTitle("ContentTitle")
                .setContentText("ContentText")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, Thread.currentThread().getName() + " onBind");
//        return new TestBinder();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, Thread.currentThread().getName() + " onStartCommand " + intent.getStringExtra("ext"));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    /**
     * 进程间通信不能用这种方式
     */
    public class TestBinder extends Binder {
        public String serviceBinderMethod(String param) {
            Log.d(TAG, "serviceBinderMethod");

            Intent intent = new Intent(ServiceActivity.ACTION_ACTIVITY_RECEIVE);
            intent.putExtra("ext", "我是 Service Binder 扩展信息" + System.currentTimeMillis());
            sendBroadcast(intent);

            return "我是 serviceBinderMethod 返回的结果 " + param.toUpperCase();
        }
    }

    private TestServiceAidlInterface.Stub mBinder = new TestServiceAidlInterface.Stub() {

        @Override
        public String serviceBinderMethod(String param) throws RemoteException {
            Log.d(TAG, "serviceBinderMethod");

            Intent intent = new Intent(ServiceActivity.ACTION_ACTIVITY_RECEIVE);
            intent.putExtra("ext", "我是 Service Stub Binder 扩展信息" + System.currentTimeMillis());
            sendBroadcast(intent);

            return "我是 Stub serviceBinderMethod 返回的结果 " + param.toUpperCase();
        }
    };
}
