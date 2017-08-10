package cn.bingoogolapple.testservice.activity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import java.util.List;

import cn.bingoogolapple.bacvp.BaseActivity;
import cn.bingoogolapple.testservice.R;
import cn.bingoogolapple.testservice.service.TestService;

public class ServiceActivity extends BaseActivity {
    private static final String TAG = ServiceActivity.class.getSimpleName();
    public static final String ACTION_ACTIVITY_RECEIVE = "ACTION_ACTIVITY_RECEIVE";

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive " + intent.getStringExtra("ext"));
        }
    };


    //    private TestService.TestBinder mTestBinder;
//    private TestServiceAidlInterface mTestBinder;
    private Messenger mServiceMessenger;

    public static final int WHAT_SERVICE_RESPONSE = 1;
    private Messenger mActivityMessenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_SERVICE_RESPONSE:
                    Log.d(TAG, "handleMessage " + msg.getData().getString("ext"));
                    break;
                default:
                    break;
            }
        }
    });

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // 当 bind 成功时会被调用
            Log.d(TAG, "onServiceConnected");
//            mTestBinder = (TestService.TestBinder) service;
//            mTestBinder = TestServiceAidlInterface.Stub.asInterface(service);
            mServiceMessenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // 在 Service 丢失时才会被调用，通常会在 Service 所在进程被迫终止时才会调用，当 Service 重新运行时会再次调用 onServiceConnected 方法
            Log.d(TAG, "onServiceDisconnected");
//            mTestBinder = null;
            mServiceMessenger = null;
        }
    };

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_service);
        registerReceiver(mBroadcastReceiver, new IntentFilter(ACTION_ACTIVITY_RECEIVE));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    public void start(View v) {
        Intent intent = new Intent(this, TestService.class);
        intent.putExtra("ext", "我是 ServiceActivity startService 扩展信息 " + System.currentTimeMillis());
        startService(intent);
    }

    public void stop(View v) {
        stopService(new Intent(this, TestService.class));
    }

    public void bind(View v) {
        Intent intent = new Intent(this, TestService.class);
        intent.putExtra("ext", "我是 ServiceActivity bindService 扩展信息" + System.currentTimeMillis());
        bindService(intent, mServiceConnection, BIND_AUTO_CREATE);
    }

    public void unbind(View v) {
        /**
         * 如果已经 unbind 了，再次调用 unbind 的话会报 IllegalArgumentException
         * bindService 后必须在 Activity 销毁之前 unbindService
         */
//        if (mTestBinder != null) {
//            mTestBinder = null;
//            unbindService(mServiceConnection);
//        }

        if (mServiceMessenger != null) {
            mServiceMessenger = null;
            unbindService(mServiceConnection);
        }
    }

    public void callBinderMethod(View v) {
        /*
        if (mTestBinder != null) {
//            String result = mTestBinder.serviceBinderMethod("bga");
//            Log.d(TAG, result);

            try {
                String result = mTestBinder.serviceBinderMethod("bga");
                Log.d(TAG, result);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        */

        if (mServiceMessenger != null) {
            try {
                Message message = Message.obtain();
                message.what = TestService.WHAT_CALL_SERVICE_BINDER_METHOD;
                Bundle bundle = new Bundle();
                bundle.putString("ext", "我是客户端的消息");
                message.setData(bundle);
                message.replyTo = mActivityMessenger;
                mServiceMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendBroadcast(View v) {
        sendBroadcast(new Intent(TestService.ACTION_SERVICE_RECEIVE));
    }

    public void isServiceRunning(View v) {
        if (isServiceRunning(this, TestService.class)) {
            Log.d(TAG, TestService.class.getSimpleName() + " 运行中");
        } else {
            Log.d(TAG, TestService.class.getSimpleName() + " 已停止运行");
        }
    }

    /**
     * 判断服务是否在运行中
     *
     * @param context
     * @param serviceClass
     * @return
     */
    public static final boolean isServiceRunning(Context context, Class<? extends Service> serviceClass) {
        if (context == null || serviceClass == null) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(100);
        for (ActivityManager.RunningServiceInfo info : list) {
            if ((info.service.getClassName()).equals(serviceClass.getName())) {
                return true;
            }
        }
        return false;
    }
}