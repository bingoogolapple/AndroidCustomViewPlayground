package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

public class ChildView1 extends View {
    private static final String TAG = ChildView1.class.getSimpleName();

    public ChildView1(Context context) {
        super(context);
    }

    public ChildView1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        /**
         * 告诉父组件，不要拦截我的事件。调用该方法后，所有父控件的onInterceptTouchEvent都不会再被调用，直到下一次重新ACTION_DOWN
         * 在dispatchTouchEvent或onTouchEvent里调该方法都可以
         */
//        getParent().requestDisallowInterceptTouchEvent(true);

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

//        return super.onTouchEvent(event);
        return true;
//        return false;

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
    }
}
