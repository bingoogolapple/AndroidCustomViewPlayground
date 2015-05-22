package cn.bingoogolapple.acvp.refreshrecyclerview.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 22:35
 * 描述:
 */
public class BGARefreshLayout extends LinearLayout {
    private static final String TAG = BGARefreshLayout.class.getSimpleName();

    private BGARefreshViewHolder mRefreshViewHolder;
    /**
     * 整个头部控件，下拉刷新控件mRefreshHeaderView和下拉刷新控件下方的自定义组件mCustomHeaderView的父控件
     */
    private LinearLayout mWholeHeaderView;
    /**
     * 下拉刷新控件
     */
    private View mRefreshHeaderView;
    /**
     * 下拉刷新控件下方的自定义控件
     */
    private View mCustomHeaderView;
    /**
     * 下拉刷新控件的高度
     */
    private int mRefreshHeaderViewHeight;
    /**
     * 当前刷新状态
     */
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
    /**
     * 下拉刷新和上拉加载更多代理
     */
    private BGARefreshLayoutDelegate mDelegate;
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


    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private View mNormalView;

    private float mInterceptTouchDownX;
    private float mInterceptTouchDownY;


    public BGARefreshLayout(Context context) {
        this(context, null);
    }

    public BGARefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);

        initWholeHeaderView();
    }

    /**
     * 初始化整个头部控件
     */
    private void initWholeHeaderView() {
        mWholeHeaderView = new LinearLayout(getContext());
        mWholeHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mWholeHeaderView.setOrientation(LinearLayout.VERTICAL);
        addView(mWholeHeaderView);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) {
            throw new RuntimeException(BGARefreshLayout.class.getSimpleName() + "必须有且只有一个子控件");
        }

        View contentView = getChildAt(1);
        if (contentView instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) contentView;
        } else if (contentView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) contentView;
        } else if (contentView instanceof ScrollView) {
            mScrollView = (ScrollView) contentView;
        } else {
            mNormalView = contentView;
        }
    }

    public void setRefreshViewHolder(BGARefreshViewHolder refreshViewHolder) {
        mRefreshViewHolder = refreshViewHolder;
        initRefreshHeaderView();
    }

    /**
     * 初始化下拉刷新控件
     *
     * @return
     */
    private void initRefreshHeaderView() {
        mRefreshHeaderView = mRefreshViewHolder.getRefreshHeaderView();
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mRefreshHeaderView, 0);

            // 测量下拉刷新控件的高度
            mRefreshHeaderView.measure(0, 0);
            mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();
            mMinWholeHeaderViewPaddingTop = -mRefreshHeaderViewHeight;
            mMaxWholeHeaderViewPaddingTop = (int) (mRefreshHeaderViewHeight * mRefreshViewHolder.getSpringDistanceScale());

            mWholeHeaderView.setPadding(0, mMinWholeHeaderViewPaddingTop, 0, 0);
        }
    }

    /**
     * 添加下拉刷新控件下方的自定义控件
     *
     * @param customHeaderView
     */
    public void addCustomHeaderView(View customHeaderView) {
        mCustomHeaderView = customHeaderView;
        if (mCustomHeaderView != null) {
            mCustomHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mCustomHeaderView);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceptTouchDownX = event.getRawX();
                mInterceptTouchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int interceptTouchMoveDistanceY = (int) (event.getRawY() - mInterceptTouchDownY);
                if (Math.abs(event.getRawX() - mInterceptTouchDownX) < Math.abs(interceptTouchMoveDistanceY)) {
                    if (shouldHandleRefresh() && interceptTouchMoveDistanceY > 0) {
                        return true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    private boolean shouldHandleRefresh() {
        if (null != mNormalView) {
            return true;
        }

        if (null != mScrollView && mScrollView.getChildAt(0).getScrollY() == 0) {
            return true;
        }

        if (null != mAdapterView) {
            int firstChildTop = mAdapterView.getChildAt(0).getTop();
            int contentViewPaddingTop = mAdapterView.getPaddingTop();
            if (mAdapterView.getFirstVisiblePosition() == 0) {
                if (firstChildTop == 0 || Math.abs(firstChildTop - contentViewPaddingTop) <= 3) {
                    return true;
                }
            }
        }

        if (null != mRecyclerView) {
            int firstChildTop = mRecyclerView.getChildAt(0).getTop();
            int contentViewPaddingTop = mRecyclerView.getPaddingTop();
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                if (firstChildTop == 0 || Math.abs(firstChildTop - contentViewPaddingTop) <= 3) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != mRefreshHeaderView) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = (int)event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (handleActionMove(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (handleActionUp()) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 处理手指滑动事件
     *
     * @param event
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionMove(MotionEvent event) {
        // 如果自定义头部控件没有完全显示，则跳出switch语句，执行父控件的touch事件
        if (isCustomHeaderViewNotCompleteVisible()) {
            return false;
        }
        // 如果处于正在刷新状态，则跳出switch语句，执行父控件的touch事件
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            return false;
        }

        if (mDownY == -1) {
            mDownY = (int) event.getY();
        }
        int diffY = (int) event.getY() - mDownY;

        diffY = (int) (diffY / mRefreshViewHolder.getPaddingTopScale());

        // 如果是向下拉，并且当前可见的第一个条目的索引等于0，才处理整个头部控件的padding
        if (diffY > 0 && shouldHandleRefresh()) {
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
                mRefreshViewHolder.handleScale(scale);
            }

            paddingTop = Math.min(paddingTop, mMaxWholeHeaderViewPaddingTop);
            mWholeHeaderView.setPadding(0, paddingTop, 0, 0);

            // ACTION_DOWN时没有消耗掉事件，子控件会处于按下状态，这里设置ACTION_CANCEL，使子控件取消按下状态
            event.setAction(MotionEvent.ACTION_CANCEL);
            super.onTouchEvent(event);

            return true;
        }
        return false;
    }

    /**
     * 处理手指抬起事件
     *
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionUp() {
        mDownY = -1;

        boolean isReturnTrue = false;
        // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消耗ACTION_UP事件
        if (mWholeHeaderView.getPaddingTop() != mMinWholeHeaderViewPaddingTop) {
            isReturnTrue = true;
        }

        if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN) {
            // 处于下拉刷新状态，松手时隐藏下拉刷新控件
            hiddenRefreshHeaderView();
        } else if (mCurrentRefreshStatus == RefreshStatus.RELEASE_REFRESH) {
            // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
            changeRefreshHeaderViewToZero();
            mCurrentRefreshStatus = RefreshStatus.REFRESHING;
            handleRefreshStatusChanged();

            if (mDelegate != null) {
                mDelegate.onBGARefreshLayoutBeginRefreshing();
            }
        }

        return isReturnTrue;
    }

    /**
     * 处理下拉刷新控件状态变化
     */
    private void handleRefreshStatusChanged() {
        switch (mCurrentRefreshStatus) {
            case PULL_DOWN:
                mRefreshViewHolder.changeToPullDown();
                break;
            case RELEASE_REFRESH:
                mRefreshViewHolder.changeToReleaseRefresh();
                break;
            case REFRESHING:
                mRefreshViewHolder.changeToRefreshing();
                break;
            default:
                break;
        }
    }

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
        mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
        hiddenRefreshHeaderView();
        handleRefreshStatusChanged();
        mRefreshViewHolder.onEndRefreshing();
    }

    /**
     * 隐藏下拉刷新控件，带动画
     */
    private void hiddenRefreshHeaderView() {
        ViewCompat.postOnAnimation(mWholeHeaderView, new Runnable() {
            @Override
            public void run() {
                if (mWholeHeaderView.getPaddingTop() > mMinWholeHeaderViewPaddingTop) {
                    mWholeHeaderView.setPadding(0, mWholeHeaderView.getPaddingTop() - 8, 0, 0);
                    hiddenRefreshHeaderView();
                } else {
                    mWholeHeaderView.setPadding(0, mMinWholeHeaderViewPaddingTop, 0, 0);
                }
            }
        });
    }

    /**
     * 设置下拉刷新控件的paddingTop到0，带动画
     */
    private void changeRefreshHeaderViewToZero() {
        ViewCompat.postOnAnimation(mWholeHeaderView, new Runnable() {
            @Override
            public void run() {
                if (mWholeHeaderView.getPaddingTop() > 0) {
                    mWholeHeaderView.setPadding(0, mWholeHeaderView.getPaddingTop() - 8, 0, 0);
                    changeRefreshHeaderViewToZero();
                } else {
                    mWholeHeaderView.setPadding(0, 0, 0, 0);
                }
            }
        });
    }

    public void setDelegate(BGARefreshLayoutDelegate delegate) {
        mDelegate = delegate;
    }

    public interface BGARefreshLayoutDelegate {
        /**
         * 开始刷新
         */
        void onBGARefreshLayoutBeginRefreshing();
    }

    public enum RefreshStatus {
        PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }
}