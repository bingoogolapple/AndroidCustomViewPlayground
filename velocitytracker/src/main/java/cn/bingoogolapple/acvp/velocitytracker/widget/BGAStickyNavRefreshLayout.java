package cn.bingoogolapple.acvp.velocitytracker.widget;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.OverScroller;
import android.widget.ScrollView;

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

    private RecyclerView mDirectRecyclerView;
    private AbsListView mDirectAbsListView;
    private ScrollView mDirectScrollView;
    private WebView mDirectWebView;
    private ViewPager mDirectViewPager;

    private View mNestedContentView;
    private RecyclerView mNestedRecyclerView;
    private AbsListView mNestedAbsListView;
    private ScrollView mNestedScrollView;
    private WebView mNestedWebView;

    private OverScroller mOverScroller;
    private VelocityTracker mVelocityTracker;
    private int mTouchSlop;
    private int mMaximumVelocity;
    private int mMinimumVelocity;

    private boolean mIsInControl = true;

    private float mLastDispatchY;
    private float mLastTouchY;

    public BGAStickyNavRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(VERTICAL);

        mOverScroller = new OverScroller(context);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
    }

    @Override
    public void setOrientation(@LinearLayoutCompat.OrientationMode int orientation) {
        if (VERTICAL == orientation) {
            super.setOrientation(VERTICAL);
        }
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

        if (mContentView instanceof AbsListView) {
            mDirectAbsListView = (AbsListView) mContentView;
        } else if (mContentView instanceof RecyclerView) {
            mDirectRecyclerView = (RecyclerView) mContentView;
        } else if (mContentView instanceof ScrollView) {
            mDirectScrollView = (ScrollView) mContentView;
        } else if (mContentView instanceof WebView) {
            mDirectWebView = (WebView) mContentView;
        } else if (mContentView instanceof ViewPager) {
            mDirectViewPager = (ViewPager) mContentView;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(mContentView, widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec) - getNavViewHeight(), MeasureSpec.EXACTLY));
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
     * 获取导航视图的高度，包括topMargin和bottomMargin
     *
     * @return
     */
    private int getNavViewHeight() {
        MarginLayoutParams layoutParams = (MarginLayoutParams) mNavView.getLayoutParams();
        return mNavView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
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
//            debug("头部视图完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
            return true;
        } else {
//            debug("头部视图没有完全隐藏  navViewTopOnScreenY = " + navViewTopOnScreenY + "   contentOnScreenTopY = " + contentOnScreenTopY);
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
                mLastDispatchY = currentTouchY;
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastDispatchY;
                mLastDispatchY = currentTouchY;
                if (isContentViewToTop() && isHeaderViewCompleteInvisible()) {
                    if (differentY >= 0 && !mIsInControl) {
                        mIsInControl = true;

                        return resetDispatchTouchEvent(ev);
                    }

                    if (differentY <= 0 && mIsInControl) {
                        mIsInControl = false;

                        return resetDispatchTouchEvent(ev);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean resetDispatchTouchEvent(MotionEvent ev) {
        MotionEvent newEvent = MotionEvent.obtain(ev);

        ev.setAction(MotionEvent.ACTION_CANCEL);
        dispatchTouchEvent(ev);

        newEvent.setAction(MotionEvent.ACTION_DOWN);
        return dispatchTouchEvent(newEvent);
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
                    if (!isHeaderViewCompleteInvisible() || (isContentViewToTop() && isHeaderViewCompleteInvisible() && mIsInControl)) {
                        mLastTouchY = currentTouchY;
                        return true;
                    }
                }
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
                break;
            case MotionEvent.ACTION_MOVE:
                float differentY = currentTouchY - mLastTouchY;
                mLastTouchY = currentTouchY;
                if (Math.abs(differentY) > 0) {
                    scrollBy(0, (int) -differentY);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                recycleVelocityTracker();
                if (!mOverScroller.isFinished()) {
                    mOverScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_UP:
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                int initialVelocity = (int) mVelocityTracker.getYVelocity();
                if ((Math.abs(initialVelocity) > mMinimumVelocity)) {
                    fling(-initialVelocity);
                }
                recycleVelocityTracker();
                break;
        }
        return true;
    }

    private boolean isContentViewToTop() {
        if (mDirectWebView != null && mDirectWebView.getScrollY() == 0) {
            return true;
        }

        // 内容是ScrollView，并且其scrollY为0时满足
        if (mDirectScrollView != null && mDirectScrollView.getScrollY() == 0) {
            return true;
        }

        if (mDirectAbsListView != null) {
            int firstChildTop = 0;
            if (mDirectAbsListView.getChildCount() > 0) {
                // 如果AdapterView的子控件数量不为0，获取第一个子控件的top
                firstChildTop = mDirectAbsListView.getChildAt(0).getTop() - mDirectAbsListView.getPaddingTop();
            }
            if (mDirectAbsListView.getFirstVisiblePosition() == 0 && firstChildTop == 0) {
                return true;
            }
        }

        if (mDirectRecyclerView != null) {
            int firstChildTop = 0;
            if (mDirectRecyclerView.getChildCount() > 0) {
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top

                // 解决item的topMargin不为0时不能触发下拉刷新
                MarginLayoutParams layoutParams = (MarginLayoutParams) mDirectRecyclerView.getChildAt(0).getLayoutParams();
                firstChildTop = mDirectRecyclerView.getChildAt(0).getTop() - layoutParams.topMargin - mDirectRecyclerView.getPaddingTop();
            }

            RecyclerView.LayoutManager manager = mDirectRecyclerView.getLayoutManager();
            if (manager == null) {
                return true;
            }
            if (manager.getItemCount() == 0) {
                return true;
            }

            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] == 0) {
                    return true;
                }
            }
        }
        if (mDirectViewPager != null) {
            return isViewPagerContentViewToTop();
        }
        return false;
    }

    private boolean isViewPagerContentViewToTop() {
        resetNestedContentView();

        if (mNestedWebView != null && mNestedWebView.getScrollY() == 0) {
            return true;
        }

        // 内容是ScrollView，并且其scrollY为0时满足
        if (mNestedScrollView != null && mNestedScrollView.getScrollY() == 0) {
            return true;
        }

        if (mNestedAbsListView != null) {
            int firstChildTop = 0;
            if (mNestedAbsListView.getChildCount() > 0) {
                // 如果AdapterView的子控件数量不为0，获取第一个子控件的top
                firstChildTop = mNestedAbsListView.getChildAt(0).getTop() - mNestedAbsListView.getPaddingTop();
            }
            if (mNestedAbsListView.getFirstVisiblePosition() == 0 && firstChildTop == 0) {
                return true;
            }
        }

        if (mNestedRecyclerView != null) {
            int firstChildTop = 0;
            if (mNestedRecyclerView.getChildCount() > 0) {
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top

                // 解决item的topMargin不为0时不能触发下拉刷新
                MarginLayoutParams layoutParams = (MarginLayoutParams) mNestedRecyclerView.getChildAt(0).getLayoutParams();
                firstChildTop = mNestedRecyclerView.getChildAt(0).getTop() - layoutParams.topMargin - mNestedRecyclerView.getPaddingTop();
            }

            RecyclerView.LayoutManager manager = mNestedRecyclerView.getLayoutManager();
            if (manager == null) {
                return true;
            }
            if (manager.getItemCount() == 0) {
                return true;
            }

            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private void resetNestedContentView() {
        int currentItem = mDirectViewPager.getCurrentItem();
        PagerAdapter adapter = mDirectViewPager.getAdapter();
        if (adapter instanceof FragmentPagerAdapter || adapter instanceof FragmentStatePagerAdapter) {
            Fragment item = (Fragment) adapter.instantiateItem(mDirectViewPager, currentItem);
            mNestedContentView = item.getView();

            mNestedAbsListView = null;
            mNestedRecyclerView = null;
            mNestedScrollView = null;
            mNestedWebView = null;

            if (mNestedContentView instanceof AbsListView) {
                mNestedAbsListView = (AbsListView) mNestedContentView;
            } else if (mNestedContentView instanceof RecyclerView) {
                mNestedRecyclerView = (RecyclerView) mNestedContentView;
            } else if (mNestedContentView instanceof ScrollView) {
                mNestedScrollView = (ScrollView) mNestedContentView;
            } else if (mNestedContentView instanceof WebView) {
                mNestedWebView = (WebView) mNestedContentView;
            }
        } else {
            throw new IllegalStateException(BGAStickyNavRefreshLayout.class.getSimpleName() + "的第三个子控件为ViewPager时，其adapter必须是FragmentPagerAdapter或者FragmentStatePagerAdapter");
        }
    }

    private static void debug(String msg) {
        Log.d(TAG, msg);
    }

}