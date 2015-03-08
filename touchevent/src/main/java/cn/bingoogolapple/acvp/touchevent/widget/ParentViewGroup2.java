package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

public class ParentViewGroup2 extends LinearLayout {
    private static final String TAG = ParentViewGroup2.class.getSimpleName();

    public ParentViewGroup2(Context context) {
        super(context);
    }

    public ParentViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        // 告诉父组件，不要拦截我的事件
        getParent().requestDisallowInterceptTouchEvent(true);
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, "onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.onInterceptTouchEvent(event);
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
//        return super.onTouchEvent(event);
        return true;
    }
}