package cn.bingoogolapple.acvp.touchevent.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.bingoogolapple.acvp.touchevent.R;
import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;
import cn.bingoogolapple.bacvp.BaseActivity;

/**
 * 如果Activity的dispatchTouchEvent方法返回true或者false，则直接消费事件，不会传递到Activity里的其他控件，并且Activity的onTouchEvent方法也不会被调用。
 * 必须调用super.dispatchTouchEvent(event)时，Activity的onTouchEvent方法才有可能会被调用「Activity里的其他控件都不处理该事件时」
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    public void demo2(View v) {
        startActivity(new Intent(this, Demo2Activity.class));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        return super.dispatchTouchEvent(event);
//        return true;
//        return false;

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return super.dispatchTouchEvent(event);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        return super.onTouchEvent(event);
//        return true;
//        return false;
    }
}