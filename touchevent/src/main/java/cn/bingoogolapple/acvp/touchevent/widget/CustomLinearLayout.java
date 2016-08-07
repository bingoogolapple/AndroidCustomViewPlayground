package cn.bingoogolapple.acvp.touchevent.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/8 下午10:33
 * 描述:
 */
public class CustomLinearLayout extends LinearLayout {

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int childCount = getChildCount();
        int childWidth = getWidth() / childCount;
        int childHeight = getHeight();

        float eventX = event.getX();

        if (eventX < childWidth) {
            event.setLocation(childWidth / 2, event.getY());
            getChildAt(0).dispatchTouchEvent(event);
        } else if (eventX > childWidth && eventX < 2 * childWidth) {
            float eventY = event.getY();
            if (eventY < childHeight / 2) {
                // y小于高度的二分之一时所有子控件联动
                event.setLocation(childWidth / 2, event.getY());
                for (int i = 0; i < childCount; i++) {
                    View child = getChildAt(i);
                    try {
                        child.dispatchTouchEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (eventY >= childHeight / 2) {
                // y大于等于高度的二分之一时只传递事件给第二个子控件
                event.setLocation(childWidth / 2, event.getY());
                try {
                    getChildAt(1).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (eventX > 2 * childWidth) {
            event.setLocation(childWidth / 2, event.getY());
            getChildAt(2).dispatchTouchEvent(event);
        }
        return true;
    }
}