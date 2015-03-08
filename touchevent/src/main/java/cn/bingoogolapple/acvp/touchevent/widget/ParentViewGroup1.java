package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

public class ParentViewGroup1 extends LinearLayout {
    private static final String TAG = ParentViewGroup1.class.getSimpleName();

    public ParentViewGroup1(Context context) {
        super(context);
    }

    public ParentViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.i(TAG, "onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            return super.onInterceptTouchEvent(event);
        } else {
            return true;
        }
        // 如果返回true，则子视图将收不到任何事件
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
//        return super.onTouchEvent(event);
        return true;
    }
}