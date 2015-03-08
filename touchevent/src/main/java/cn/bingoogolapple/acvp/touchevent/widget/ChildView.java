package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.bingoogolapple.acvp.touchevent.util.TouchEventUtil;

public class ChildView extends View {
    private static final String TAG = ChildView.class.getSimpleName();

    public ChildView(Context context) {
        super(context);
    }

    public ChildView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG, "onTouchEvent --> " + TouchEventUtil.getTouchAction(event.getAction()));
//        return super.onTouchEvent(event);
        // 如果在ACTION_DOWN事件中没有返回true，则不会收到后续的ACTION_MOVE和ACTION_UP事件
        // 如果在ACTION_DOWN事件中返回true，则父视图的onTouchEvent方法将不会被调用
        return true;
    }
}
