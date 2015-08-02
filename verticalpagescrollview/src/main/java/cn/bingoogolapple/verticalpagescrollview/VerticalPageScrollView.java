package cn.bingoogolapple.verticalpagescrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/2 下午11:09
 * 描述:
 */
public class VerticalPageScrollView extends ViewGroup {
    private static final String TAG = VerticalPageScrollView.class.getSimpleName();
    private GestureDetector mGestureDetector;
    private int mStartY;
    private int mCurrentItem;
    private Scroller mScroller;

    public VerticalPageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        initGestureDetector();
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                scrollBy(0, (int) distanceY);
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                int tempItem = mCurrentItem;
                if (velocityY > 1000) {
                    tempItem--;
                } else if (velocityY < -1000) {
                    tempItem++;
                }
                moveTo(tempItem);
                Log.i(TAG, "onFling");
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "l = " + l + "  t = " + t + "  r = " + r + "  b = " + b + "  getWidth = " + getWidth() + "  getHeight = " + getHeight() + "  getMeasuredWidth = " + getMeasuredWidth() + "  getMeasuredHeight = " + getMeasuredHeight());
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0, i * getHeight(), getWidth(), getHeight() * (i + 1));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                int endY = (int) event.getY();
                int tempItem = mCurrentItem;
                if (endY - mStartY > getHeight() / 2) {
                    tempItem--;
                } else if (mStartY - endY > getHeight() / 2) {
                    tempItem++;
                }
                moveTo(tempItem);
                Log.i(TAG, "ACTION_UP");
                break;
        }
        return true;
    }

    private void moveTo(int tempItem) {
        if (tempItem < 0) {
            tempItem = 0;
        }
        if (tempItem > getChildCount() - 1) {
            tempItem = getChildCount() - 1;
        }
        mCurrentItem = tempItem;

        int distanceY = getHeight() * mCurrentItem - getScrollY();
        mScroller.startScroll(0, getScrollY(), 0, distanceY);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }
}