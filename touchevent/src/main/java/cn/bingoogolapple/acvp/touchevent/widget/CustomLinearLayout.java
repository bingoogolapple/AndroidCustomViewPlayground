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
        int width = getWidth() / getChildCount();
        int height = getHeight();
        int count = getChildCount();

        float eventX = event.getX();

        if (eventX < width) {
            event.setLocation(width / 2, event.getY());
            getChildAt(0).dispatchTouchEvent(event);
            return true;
        } else if (eventX > width && eventX < 2 * width) {
            float eventY = event.getY();
            if (eventY < height / 2) {
                event.setLocation(width / 2, event.getY());
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    try {
                        child.dispatchTouchEvent(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            } else if (eventY > height / 2) {
                event.setLocation(width / 2, event.getY());
                try {
                    getChildAt(1).dispatchTouchEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        } else if (eventX > 2 * width) {
            event.setLocation(width / 2, event.getY());
            getChildAt(2).dispatchTouchEvent(event);
            return true;
        }
        return true;
    }
}