package cn.bingoogolapple.acvp.refreshrecyclerview.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 22:35
 * 描述:
 */
public class BGARefreshLayoutTemp extends LinearLayout implements StickinessRefreshView.StickinessRefreshViewDelegate {
    private static final String TAG = BGARefreshLayoutTemp.class.getSimpleName();
    private Handler mHandler;

    private View mRefreshHeaderView;
    private int mRefreshHeaderViewHeight;
    private StickinessRefreshView mStickinessRefreshView;

    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private View mNormalView;

    private int mTouchDownY = -1;

    private float mInterceptTouchDownX;
    private float mInterceptTouchDownY;

    private int mMinRefreshHeaderViewMarginTop;
    private int mMaxRefreshHeaderViewMarginTop;



    private StickinessRefreshViewMoveTask mStickinessRefreshViewMoveTask;
    private RefreshHeaderViewMoveTask mRefreshHeaderViewMoveTask;

    private BGARefreshLayoutDelegate mDelegate;
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;

    public BGARefreshLayoutTemp(Context context) {
        this(context, null);
    }

    public BGARefreshLayoutTemp(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        mHandler = new Handler();

        initRefreshHeaderView();
    }

    private void initRefreshHeaderView() {
        mRefreshHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_header_stickiness, this, false);
        mRefreshHeaderView.measure(0, 0);
        mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();
        mMinRefreshHeaderViewMarginTop = -mRefreshHeaderViewHeight;

        mStickinessRefreshView = (StickinessRefreshView) mRefreshHeaderView.findViewById(R.id.stickinessRefreshView);
        mStickinessRefreshView.setDelegate(this);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mRefreshHeaderViewHeight);
        params.topMargin = mMinRefreshHeaderViewMarginTop;
        addView(mRefreshHeaderView, params);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) {
            throw new RuntimeException(BGARefreshLayoutTemp.class.getSimpleName() + "必须有且只有一个子控件");
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopMovement();
                mTouchDownY = (int)event.getY();
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
//                if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN) {
//                    int diffY = (int) (event.getY() - mTouchDownY);
//                    if (diffY > mRefreshHeaderViewHeight) {
//                        smoothToOriginalSpot(diffY);
//                    } else {
//                        smoothHideHeadView();
//                    }
//                }
                break;
        }
        return true;
    }

    private boolean handleActionMove(MotionEvent event) {
        // 如果处于正在刷新状态，则跳出switch语句，执行父控件的touch事件
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            return false;
        }

        if (mTouchDownY == -1) {
            mTouchDownY = (int) event.getY();
        }
        int diffY = (int) event.getY() - mTouchDownY;

        if (diffY > 0 && shouldHandleRefresh()) {

            switch (mCurrentRefreshStatus) {
                case PULL_DOWN:
                    if (diffY < 0) {
                        diffY = 0;
                    }
                    if (diffY <= mRefreshHeaderViewHeight) {
                        setRefreshHeaderViewMarginTop(diffY);
                        mStickinessRefreshView.setHeight(0);
                        setChildHeight(mRefreshHeaderView, mRefreshHeaderViewHeight);
                    } else {
                        setRefreshHeaderViewMarginTop(mRefreshHeaderViewHeight);
                        mStickinessRefreshView.setHeight(diffY - mRefreshHeaderViewHeight);
                        setChildHeight(mRefreshHeaderView, diffY);
                        if (mStickinessRefreshView.isExceedMaximumHeight()) {
                            mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                        }
                    }
                    break;
                case RELEASE_REFRESH:
                    smoothToOriginalSpot(diffY);
                    mCurrentRefreshStatus = RefreshStatus.REFRESHING;
                    break;
                case REFRESHING:
                    if (null != mDelegate) mDelegate.onBGARefreshLayoutBeginRefreshing();
                    break;
            }
            return true;
        }

        return false;
    }

    private boolean handleActionUp() {
        mTouchDownY = -1;
        boolean isReturnTrue = false;
        // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消耗ACTION_UP事件



        return isReturnTrue;
    }

    private void setRefreshHeaderViewMarginTop(int height) {
        LayoutParams params = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        params.topMargin = height - mRefreshHeaderViewHeight;
        mRefreshHeaderView.setLayoutParams(params);
    }

    private void setChildHeight(View child, int height) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        params.height = height;
        child.setLayoutParams(params);
    }

    private void smoothToOriginalSpot(int y) {
        mStickinessRefreshViewMoveTask = new StickinessRefreshViewMoveTask(y);
        mHandler.post(mStickinessRefreshViewMoveTask);
    }

    private void stopMovement() {
        mHandler.removeCallbacks(mStickinessRefreshViewMoveTask);
        mHandler.removeCallbacks(mRefreshHeaderViewMoveTask);
    }

    private void smoothHideHeadView() {
        mRefreshHeaderViewMoveTask = new RefreshHeaderViewMoveTask();
        mHandler.post(mRefreshHeaderViewMoveTask);
    }

    @Override
    public void onCirclingFullyStop() {
        smoothHideHeadView();
    }

    @Override
    public void onPullFullyStop() {

    }

    public void endRefreshing() {
        smoothHideHeadView();
        mStickinessRefreshView.stopCirclingAndReturn();
    }

    public void setDelegate(BGARefreshLayoutDelegate delegate) {
        mDelegate = delegate;
    }

    private class StickinessRefreshViewMoveTask implements Runnable {
        int mStartY;

        public StickinessRefreshViewMoveTask(int startY) {
            stopMovement();
            mStartY = startY;
        }

        @Override
        public void run() {
            mStartY += (mRefreshHeaderViewHeight - mStartY) * 0.5F;

            mStickinessRefreshView.setHeight(mStartY - mRefreshHeaderViewHeight);
            setChildHeight(mRefreshHeaderView, mStartY);

            if (mStartY != mRefreshHeaderViewHeight) {
                mHandler.postDelayed(mStickinessRefreshViewMoveTask, 20);
            } else {
                switch (mCurrentRefreshStatus) {
                    case REFRESHING:
                    case RELEASE_REFRESH:
                        mStickinessRefreshView.circling();
                        break;
                    case PULL_DOWN:
                        smoothHideHeadView();
                        break;
                }
            }
        }
    }

    public class RefreshHeaderViewMoveTask implements Runnable {
        LayoutParams params;

        public RefreshHeaderViewMoveTask() {
            params = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        }

        @Override
        public void run() {
            params.topMargin += (-mRefreshHeaderViewHeight - params.topMargin) * 0.5F;

            mRefreshHeaderView.setLayoutParams(params);

            if (params.topMargin != -mRefreshHeaderViewHeight) {
                mHandler.postDelayed(mRefreshHeaderViewMoveTask, 20);
            } else {
                mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
            }
        }
    }

    public interface BGARefreshLayoutDelegate {
        /**
         * 开始刷新
         */
        void onBGARefreshLayoutBeginRefreshing();

        /**
         * 开始加载更多
         */
        void onBGARefreshLayoutBeginLoadingMore();
    }

    public enum RefreshStatus {
        PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }
}