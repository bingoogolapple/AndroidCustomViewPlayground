package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

/**
 * 如果ViewGroup的dispatchTouchEvent方法返回true或者false，则直接消费事件，不会传递到ViewGroup里的其他控件，并且ViewGroup的onTouchEvent和onInterceptTouchEvent方法也不会被调用。
 * 必须调用super.dispatchTouchEvent(event)时，ViewGroup的onTouchEvent方法才有可能会被调用「ViewGroup里的其他控件都不处理该事件时」
 */
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
//        return true;
//        return false;

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return super.dispatchTouchEvent(event);
//        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        /**
         * 如果onInterceptTouchEvent返回了true，那么本次touch事件之后的所有action都不会再向深层的View传递，通通都会传给
         * 父层View的onTouchEvent，就是说父层已经截获了这次touch事件，「之后的action也不必询问onInterceptTouchEvent，在
         * 这次的touch事件之后发出的action时onInterceptTouchEvent不会再次调用，并且所有子控件都会收到ACTION_CANCEL事件」，直到下一次重新ACTION_DOWN
         */
        Log.i(TAG, "onInterceptTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        return super.onInterceptTouchEvent(event);
//        return true;
//        return false;

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return super.onInterceptTouchEvent(event);
//        } else {
//            return true;
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        return super.onTouchEvent(event);
//        return true;
//        return false;

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
    }
}