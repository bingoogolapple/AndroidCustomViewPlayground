package cn.bingoogolapple.testservice.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
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

    public static final int WHAT_CALL_SERVICE_BINDER_METHOD = 1;

    private Messenger mMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_CALL_SERVICE_BINDER_METHOD:
                    Log.d(TAG, "handleMessage " + msg.getData().getString("ext"));

                    Message message = Message.obtain();
                    message.what = ServiceActivity.WHAT_SERVICE_RESPONSE;
                    Bundle bundle = new Bundle();
                    bundle.putString("ext", "我是服务端的消息");
                    message.setData(bundle);
                    try {
                        msg.replyTo.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    });

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

        // 让服务运行在前台，同时展示通知栏。id 为通知栏的标识，不能为 0
        startForeground(1, notification);

        // 不让服务运行在前台。removeNotification 为 true 表示移除通知栏；否则通知栏不会移除，除非主动移除或服务死掉
//        stopForeground(removeNotification);
    }

    /**
     * 只有在第一个客户端绑定时，系统才会调用该方法来检索 IBinder
     * 系统随后无需再次调用该方法，便可将同一 IBinder 传递至任何其他绑定的客户端
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, Thread.currentThread().getName() + " onBind");

//        return new TestBinder();

//        return mAidlBinder;

        return mMessenger.getBinder();
    }

    /**
     * 当所有客户端都断开连接时调用。
     * 当返回值为 false 时，当所有客户端都断开连接时 onUnbind 被调用一次，后续有新客户端链接，然后再全部断开时 onUnbind 不会被调用。
     * 当返回值为 true 时，每当所有客户端都断开连接时 onUnbind 被调用，后续有新客户绑定时会回调 onRebind
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return true;
    }

    /**
     * onUnbind 返回 true，且有新的客户端绑定时会回调 onRebind
     */
    @Override
    public void onRebind(Intent intent) {
        Log.d(TAG, "onRebind");
    }

    /**
     * START_NOT_STICKY：如果系统在 onStartCommand 返回后终止服务，则除非有挂起 Intent 要传递，否则系统不会重建服务
     * START_STICKY：如果系统在 onStartCommand 返回后终止服务，则会重建服务并调用 onStartCommand，但绝对不会重新传递最后一个 Intent。除非有挂起 Intent 要启动服务，否则系统会通过空 Intent 调用 onStartCommand。这适用于不执行命令、但无限期运行并等待作业的服务（例如播放器）
     * START_REDELIVER_INTENT：如果系统在 onStartCommand 返回后终止服务，则会重建服务，并通过传递给服务的最后一个 Intent 调用 onStartCommand。任何挂起 Intent 均依次传递。这适用于主动执行应该立即恢复的任务（例如下载文件）的服务
     *
     * @param startId startId是请求的id唯一标识，通过 stopSelfResult(startId) 可以停止对应服务
     * @return 返回值描述服务被杀死后启动的方式
     */
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
            Log.d(TAG, "serviceBinderMethod Binder");

            Intent intent = new Intent(ServiceActivity.ACTION_ACTIVITY_RECEIVE);
            intent.putExtra("ext", "我是 Service Binder 扩展信息" + System.currentTimeMillis());
            sendBroadcast(intent);

            return "我是 serviceBinderMethod 返回的结果 " + param.toUpperCase();
        }
    }

    /**
     * AIDL（Android 接口定义语言）实现进程间通信
     */
    private TestServiceAidlInterface.Stub mAidlBinder = new TestServiceAidlInterface.Stub() {

        @Override
        public String serviceBinderMethod(String param) throws RemoteException {
            Log.d(TAG, "serviceBinderMethod AIDL");

            Intent intent = new Intent(ServiceActivity.ACTION_ACTIVITY_RECEIVE);
            intent.putExtra("ext", "我是 Service Stub Binder 扩展信息" + System.currentTimeMillis());
            sendBroadcast(intent);

            return "我是 Stub serviceBinderMethod 返回的结果 " + param.toUpperCase();
        }
    };
}
