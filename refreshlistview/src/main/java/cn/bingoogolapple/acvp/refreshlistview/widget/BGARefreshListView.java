package cn.bingoogolapple.acvp.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 10:34
 * 描述:
 */
public abstract class BGARefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = BGARefreshListView.class.getSimpleName();
    /**
     * 整个头部控件，下拉刷新控件mRefreshHeaderView和下拉刷新控件下方的自定义组件mCustomHeaderView的父控件
     */
    private LinearLayout mWholeHeaderView;
    /**
     * 下拉刷新控件
     */
    private View mRefreshHeaderView;
    /**
     * 下拉刷新控件下方的自定义控件（例如ListView顶部的轮播广告条）
     */
    private View mCustomHeaderView;
    /**
     * 上拉加载更多控件
     */
    private View mWholeFooterView;
    /**
     * 下拉刷新控件的高度
     */
    private int mRefreshHeaderViewHeight;
    /**
     * 上拉加载更多控件的高度
     */
    private int mWholeFooterViewHeight;
    /**
     * 当前刷新状态
     */
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
    /**
     * 是否处于正在加载更多状态
     */
    private boolean mIsLoadingMore = false;
    /**
     * 下拉刷新和上拉加载更多代理
     */
    private BGARefreshListViewDelegate mDelegate;
    /**
     * 自定义滚动监听器，方便使用者监听ListView的滚动
     */
    private OnScrollListener mCustomOnScrollListener;
    /**
     * 手指按下时，y轴方向的偏移量
     */
    private int mDownY = -1;
    /**
     * ListView在屏幕上的y值
     */
    private int mOnScreenY = -1;

    /**
     * 整个头部控件最小的paddingTop
     */
    private int mMinWholeHeaderViewPaddingTop;
    /**
     * 整个头部控件最大的paddingTop
     */
    private int mMaxWholeHeaderViewPaddingTop;

    public BGARefreshListView(Context context) {
        this(context, null);
    }

    public BGARefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGARefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWholeHeaderView();
        initWholeFooterView();
    }

    /**
     * 初始化整个头部控件
     */
    private void initWholeHeaderView() {
        mWholeHeaderView = new LinearLayout(getContext());
        mWholeHeaderView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        mWholeHeaderView.setOrientation(LinearLayout.VERTICAL);

        initRefreshHeaderView();

        addHeaderView(mWholeHeaderView);
    }

    /**
     * 初始化下拉刷新控件
     *
     * @return
     */
    private void initRefreshHeaderView() {
        mRefreshHeaderView = getRefreshHeaderView();
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mRefreshHeaderView);

            // 测量下拉刷新控件的高度
            mRefreshHeaderView.measure(0, 0);
            mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();
            mMinWholeHeaderViewPaddingTop = -mRefreshHeaderViewHeight;
            mMaxWholeHeaderViewPaddingTop = mRefreshHeaderViewHeight * 2 / 5;
            printLog("mMinWholeHeaderViewPaddingTop = " + mMinWholeHeaderViewPaddingTop);
            printLog("mMaxWholeHeaderViewPaddingTop = " + mMaxWholeHeaderViewPaddingTop);

            hiddenRefreshHeaderView();
        }
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    protected abstract View getRefreshHeaderView();

    private void initWholeFooterView() {
        mWholeFooterView = getWholeFooterView();
        if (mWholeFooterView != null) {
            mWholeFooterView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            addFooterView(mWholeFooterView);

            // 测量下拉刷新控件的高度
            mWholeFooterView.measure(0, 0);
            mWholeFooterViewHeight = mWholeFooterView.getMeasuredHeight();
            hiddenWholeFooterView();

            setOnScrollListener(this);
        }
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    protected abstract View getWholeFooterView();

    /**
     * 添加下拉刷新控件下方的自定义控件
     *
     * @param customHeaderView
     */
    public void addCustomHeaderView(View customHeaderView) {
        mCustomHeaderView = customHeaderView;
        if (mCustomHeaderView != null) {
            mCustomHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mCustomHeaderView);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mCustomOnScrollListener != null) {
            mCustomOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /**
         * SCROLL_STATE_IDLE 停止
         * SCROLL_STATE_FLING 正在飞
         * SCROLL_STATE_TOUCH_SCROLL 触摸滚动
         */
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                printLog("SCROLL_STATE_IDLE");
                break;
            case SCROLL_STATE_FLING:
                printLog("SCROLL_STATE_FLING");
                break;
            case SCROLL_STATE_TOUCH_SCROLL:
                printLog("SCROLL_STATE_TOUCH_SCROLL");
                break;
        }

        if ((scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) && getLastVisiblePosition() == getCount() - 1 && !mIsLoadingMore) {
            beginLoadingMore();
        }

        if (mCustomOnScrollListener != null) {
            mCustomOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    /**
     * 开始上拉加载更多
     */
    private void beginLoadingMore() {
        mIsLoadingMore = true;
        mWholeFooterView.setPadding(0, 0, 0, 0);
        setSelection(getCount());
        if (mDelegate != null) {
            mDelegate.onBGARefreshListViewBeginLoadingMore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 如果自定义头部控件没有完全显示，则跳出switch语句，执行父控件的touch事件
                if (isCustomHeaderViewNotCompleteVisible()) {
                    break;
                }
                // 如果处于正在刷新状态，则跳出switch语句，执行父控件的touch事件
                if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
                    break;
                }

                if (mDownY == -1) {
                    mDownY = (int) event.getY();
                }
                int moveY = (int) event.getY();
                int diffY = moveY - mDownY;

                // 如果是向下拉，并且当前可见的第一个条目的索引等于0，才处理整个头部控件的padding
                if (diffY > 0 && getFirstVisiblePosition() == 0) {
                    int paddingTop = mMinWholeHeaderViewPaddingTop + diffY;

                    if (paddingTop > 0 && mCurrentRefreshStatus != RefreshStatus.RELEASE_REFRESH) {
                        // 下拉刷新控件完全显示，并且当前状态没有处于释放开始刷新状态
                        mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                        handleRefreshStatusChanged();
                    } else if (paddingTop < 0) {
                        // 下拉刷新控件没有完全显示，并且当前状态没有处于下拉刷新状态
                        if (mCurrentRefreshStatus != RefreshStatus.PULL_DOWN) {
                            mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                            handleRefreshStatusChanged();
                        }
                        float scale = 1 - paddingTop * 1.0f / mMinWholeHeaderViewPaddingTop;
                        /**
                         * 往下滑
                         * paddingTop    mMinWholeHeaderViewPaddingTop ==> 0
                         * scale         0 ==> 1
                         * 往上滑
                         * paddingTop    0 ==> mMinWholeHeaderViewPaddingTop
                         * scale         1 ==> 0
                         */
                        handleScale(scale);
                    }

                    paddingTop = Math.min(paddingTop, mMaxWholeHeaderViewPaddingTop);
                    mWholeHeaderView.setPadding(0, paddingTop, 0, 0);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                mDownY = -1;

                printLog("mWholeHeaderView.getPaddingTop() = " + mWholeHeaderView.getPaddingTop());
                boolean isReturnTrue = false;
                // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消费手指抬起事件
                if (mWholeHeaderView.getPaddingTop() != mMinWholeHeaderViewPaddingTop) {
                    isReturnTrue = true;
                }

                if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN) {
                    // 处于下拉刷新状态，松手时隐藏下拉刷新控件
                    hiddenRefreshHeaderView();
                } else if (mCurrentRefreshStatus == RefreshStatus.RELEASE_REFRESH) {
                    // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
                    mWholeHeaderView.setPadding(0, 0, 0, 0);
                    mCurrentRefreshStatus = RefreshStatus.REFRESHING;
                    handleRefreshStatusChanged();

                    if (mDelegate != null) {
                        mDelegate.onBGARefreshListViewBeginRefreshing();
                    }
                }

                if (isReturnTrue) {
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    protected abstract void handleScale(float scale);

    /**
     * 处理下拉刷新控件状态变化
     */
    private void handleRefreshStatusChanged() {
        switch (mCurrentRefreshStatus) {
            case PULL_DOWN:
                changeToPullDown();
                break;
            case RELEASE_REFRESH:
                changeToReleaseRefresh();
                break;
            case REFRESHING:
                changeToRefreshing();
                break;
            default:
                break;
        }
    }

    /**
     * 进入下拉刷新状态
     */
    protected abstract void changeToPullDown();

    /**
     * 进入释放刷新状态
     */
    protected abstract void changeToReleaseRefresh();

    /**
     * 进入正在刷新状态
     */
    protected abstract void changeToRefreshing();

    /**
     * 自定义头部控件是否没有完全显示
     *
     * @return true表示没有完全显示，false表示完全显示
     */
    private boolean isCustomHeaderViewNotCompleteVisible() {
        if (mCustomHeaderView != null) {
            // 0表示x，1表示y
            int[] location = new int[2];
            if (mOnScreenY == -1) {
                getLocationOnScreen(location);
                mOnScreenY = location[1];
            }

            mCustomHeaderView.getLocationOnScreen(location);
            int customHeaderViewOnScreenY = location[1];
            if (mOnScreenY > customHeaderViewOnScreenY) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束下拉刷新和上拉加载更多
     */
    public void endRefreshing() {
        if (mIsLoadingMore) {
            mIsLoadingMore = false;
            hiddenWholeFooterView();
            onEndLoadingMore();
        } else {
            mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
            hiddenRefreshHeaderView();
            handleRefreshStatusChanged();
            onEndRefreshing();
        }
    }

    /**
     * 隐藏下拉刷新控件
     */
    private void hiddenRefreshHeaderView() {
        mWholeHeaderView.setPadding(0, mMinWholeHeaderViewPaddingTop, 0, 0);
    }

    /**
     * 隐藏上拉加载更多控件
     */
    private void hiddenWholeFooterView() {
        mWholeFooterView.setPadding(0, -mWholeFooterViewHeight, 0, 0);
    }

    /**
     * 结束上拉加载更多
     */
    protected abstract void onEndLoadingMore();

    /**
     * 结束下拉刷新
     */
    protected abstract void onEndRefreshing();

    /**
     * 设置下拉刷新和上拉加载更多代理
     *
     * @param delegate
     */
    public void setDelegate(BGARefreshListViewDelegate delegate) {
        mDelegate = delegate;
    }

    /**
     * 自定义滚动监听器，当需要监听ListView滚动状态时设置该监听器，不要通过setOnScrollListener(OnScrollListener l)方式设置滚动状态监听器
     *
     * @param onScrollListener
     */
    public void setCustomOnScrollListener(OnScrollListener onScrollListener) {
        mCustomOnScrollListener = onScrollListener;
    }

    /**
     * 刷新状态(下拉刷新，松开刷新，处于刷新状态)
     */
    public enum RefreshStatus {
        PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }

    public interface BGARefreshListViewDelegate {
        /**
         * 开始刷新
         */
        void onBGARefreshListViewBeginRefreshing();

        /**
         * 开始加载更多
         */
        void onBGARefreshListViewBeginLoadingMore();
    }

    protected void printLog(String msg) {
        Log.i(TAG, msg);
    }

}