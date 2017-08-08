package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

public class ChildView2 extends Button {
    private static final String TAG = ChildView2.class.getSimpleName();
    private boolean mIsNeedRequestDisallowIntercept = true;
    private boolean mIsHandledOnTouchListener = true;

    public ChildView2(Context context) {
        super(context);
    }

    public ChildView2(Context context, AttributeSet attrs) {
        super(context, attrs);

        /**
         * 如果给View设置了OnTouchListener，当onTouch返回false时，onTouchEvent会被调用；当onTouch返回true时，onTouchEvent不会被调用，那么如果
         * 设置了OnClickListener，onClick也不会被调用，因为OnClickListener的onClick是在onTouchEvent里面触发的
         */
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(TAG, "onTouch --> " + TouchEventUtil.getTouchAction(motionEvent.getAction()));
//                getParent().requestDisallowInterceptTouchEvent(mIsNeedRequestDisallowIntercept);
                return mIsHandledOnTouchListener;
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick");
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsNeedRequestDisallowIntercept = false;
                mIsHandledOnTouchListener = false;
            }
        }, 10000);
    }

    public ChildView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.i(TAG, "dispatchTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));

        /**
         * 告诉父组件，不要拦截我的事件。调用该方法后，所有父控件的onInterceptTouchEvent都不会再被调用，直到下一次重新ACTION_DOWN
         * 在dispatchTouchEvent或onTouchEvent里调该方法都可以
         */
//        getParent().requestDisallowInterceptTouchEvent(mIsNeedRequestDisallowIntercept);

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

//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
    }
}
