package cn.bingoogolapple.acvp.velocitytracker;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.OverScroller;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/12 12:32
 * 描述:
 */
public class BGAStickyNavRefreshLayout extends LinearLayout {
    private static final String TAG = BGAStickyNavRefreshLayout.class.getSimpleName();

    private View mHeaderView;
    private View mNavView;
    private View mContentView;

    private RecyclerView mRecyclerView;

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    private boolean mIsBeingDragged = false;
    private boolean mIsInControl = false;

    private float mLastTouchY;

    public BGAStickyNavRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mOverScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 3) {
            throw new IllegalStateException(BGAStickyNavRefreshLayout.class.getSimpleName() + "必须有且只有三个子控件");
        }

        mHeaderView = getChildAt(0);
        mNavView = getChildAt(1);
        mContentView = getChildAt(2);

        mRecyclerView = (RecyclerView) mContentView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mContentView.getLayoutParams().height = getMeasuredHeight() - mNavView.getMeasuredHeight();
    }

    @Override
    public void computeScroll() {
        if (mOverScroller.computeScrollOffset()) {
            scrollTo(0, mOverScroller.getCurrY());
            invalidate();
        }
    }

    public void fling(int velocityY) {
        mOverScroller.fling(0, getScrollY(), 0, velocityY, 0, 0, 0, getHeaderViewHeight());
        invalidate();
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }

        int headerViewHeight = getHeaderViewHeight();
        if (y > headerViewHeight) {
            y = headerViewHeight;
        }

        if (y != getScrollY()) {
            super.scrollTo(x, y);
        }
    }

    /**
     * 获取头部视图高度，包括topMargin和bottomMargin
     *
     * @return
     */
    private int getHeaderViewHeight() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mHeaderView.getLayoutParams();
        return mHeaderView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    /**
     * 头部视图是否已经完全隐藏
     *
     * @return
     */
    private boolean isHeaderViewCompleteInvisible() {
        // 0表示x，1表示y
        int[] location = new int[2];
        getLocationOnScreen(location);
        int contentOnScreenTopY = location[1] + getPaddingTop();

        mNavView.getLocationOnScreen(location);
        MarginLayoutParams params = (MarginLayoutParams) mNavView.getLayoutParams();
        int navViewTopOnScreenY = location[1] - params.topMargin;

        if (navViewTopOnScreenY == contentOnScreenTopY) {
            debug("头部视图完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
            return true;
        } else {
            debug("头部视图没有完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
            return false;
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        float currentTouchY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                if (isRecyclerViewToTop() && isHeaderViewCompleteInvisible() && differentY > 0 && !mIsInControl) {
                    mIsInControl = true;
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    MotionEvent ev2 = MotionEvent.obtain(ev);
                    dispatchTouchEvent(ev);

                    ev2.setAction(MotionEvent.ACTION_DOWN);
                    return dispatchTouchEvent(ev2);
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float currentTouchY = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                if (Math.abs(differentY) > mTouchSlop) {
                    mIsBeingDragged = true;

                    if (!isHeaderViewCompleteInvisible() || (isRecyclerViewToTop() && isHeaderViewCompleteInvisible() && differentY > 0)) {
                        initVelocityTrackerIfNotExists();
                        mVelocityTracker.addMovement(ev);
                        mLastTouchY = currentTouchY;
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                reset();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        initVelocityTrackerIfNotExists();
        mVelocityTracker.addMovement(event);
        float currentTouchY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                mLastTouchY = currentTouchY;
                return true;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                mLastTouchY = currentTouchY;
                if (!mIsBeingDragged && Math.abs(differentY) > mTouchSlop) {
                    mIsBeingDragged = true;
                }
                if (mIsBeingDragged) {
                    scrollBy(0, (int) -differentY);

                    if (isHeaderViewCompleteInvisible() && differentY < 0) {
                        event.setAction(MotionEvent.ACTION_DOWN);
                        dispatchTouchEvent(event);
                        mIsInControl = false;
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                }

                reset();
                break;
        }

        return super.onTouchEvent(event);
    }

    private void reset() {
        recycleVelocityTracker();
        mIsBeingDragged = false;
    }

    private boolean isRecyclerViewToTop() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
    }

    private static void debug(String msg) {
        Log.d(TAG, msg);
    }

}