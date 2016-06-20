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
    private Delegate mDelegate;

    public VerticalPageScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        initGestureDetector();
    }

    private void initGestureDetector() {
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                Log.i(TAG, "distanceY = " + distanceY);
                if (getScrollY() + distanceY >= 0 && getScrollY() + distanceY <= getHeight() * (getChildCount() - 1)) {
                    scrollBy(0, (int) distanceY);
                }
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
                setCurrentItem(tempItem);
                Log.i(TAG, "onFling");
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(TAG, "l = " + l + "  t = " + t + "  r = " + r + "  b = " + b + "  getWidth = " + getWidth() + "  getHeight = " + getHeight() + "  getMeasuredWidth = " + getMeasuredWidth() + "  getMeasuredHeight = " + getMeasuredHeight());
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.layout(0, i * getHeight(), getWidth(), getHeight() * (i + 1));
        }
    }

    private int mDownX;
    private int mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mGestureDetector.onTouchEvent(ev);
                mDownX = (int) ev.getX();
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int dX = Math.abs(endX - mDownX);
                int dY = Math.abs(endY - mDownY);
                if (dY > dX) {
                    result = true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                break;
        }
        return result;
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
                setCurrentItem(tempItem);
                Log.i(TAG, "ACTION_UP");
                break;
        }
        return true;
    }

    public void setCurrentItem(int tempItem) {
        setCurrentItem(tempItem, true);
    }

    public void setCurrentItem(int tempItem, boolean isSmooth) {
        if (tempItem < 0) {
            tempItem = 0;
        }
        if (tempItem > getChildCount() - 1) {
            tempItem = getChildCount() - 1;
        }
        if (mCurrentItem != tempItem && mDelegate != null) {
            mDelegate.onPageSelected(tempItem);
        }

        if (isSmooth) {
            int diff = Math.abs(mCurrentItem - tempItem);
            int duration = diff == 0 ? 250 : diff * 250;
            mCurrentItem = tempItem;

            int distanceY = getHeight() * mCurrentItem - getScrollY();
            mScroller.startScroll(0, getScrollY(), 0, distanceY, duration);
            invalidate();
        } else {
            mCurrentItem = tempItem;
            scrollTo(0, getHeight() * mCurrentItem);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void setDelegate(Delegate delegate) {
        mDelegate = delegate;
    }

    public interface Delegate {
        void onPageSelected(int position);
    }
}