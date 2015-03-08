package cn.bingoogolapple.acvp.touchevent.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;

import cn.bingoogolapple.acvp.touchevent.R;
import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

/**
 * dispatchTouchEvent  -->  onInterceptTouchEvent  -->  onTouchEvent
 */
@BGAALayout(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.dispatchTouchEvent(event);
//        return true;
//        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onTouchEvent(event);
//        return true;
//        return false;
    }
}