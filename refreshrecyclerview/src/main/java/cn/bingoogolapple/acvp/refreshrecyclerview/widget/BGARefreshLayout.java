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
public class BGARefreshLayout extends LinearLayout implements StickinessRefreshView.StickinessRefreshViewDelegate {
    private static final String TAG = BGARefreshLayout.class.getSimpleName();
    private Handler mHandler;

    private View mRefreshHeaderView;
    private int mRefreshHeaderViewHeight;
    private StickinessRefreshView mStickinessRefreshView;

    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private View mNormalView;

    private float mDownX;
    private float mDownY;
    private int mMoveDistanceY;

    private StickinessRefreshViewMoveTask mStickinessRefreshViewMoveTask;
    private RefreshHeaderViewMoveTask mRefreshHeaderViewMoveTask;

    private BGARefreshLayoutDelegate mDelegate;
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.IDLE;

    public BGARefreshLayout(Context context) {
        this(context, null);
    }

    public BGARefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(LinearLayout.VERTICAL);
        mHandler = new Handler();

        initRefreshHeaderView();
    }

    private void initRefreshHeaderView() {
        mRefreshHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_header_stickiness, this, false);
        mRefreshHeaderView.measure(0, 0);
        mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();

        mStickinessRefreshView = (StickinessRefreshView) mRefreshHeaderView.findViewById(R.id.stickinessRefreshView);
        mStickinessRefreshView.setDelegate(this);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mRefreshHeaderViewHeight);
        params.topMargin = -mRefreshHeaderViewHeight;
        addView(mRefreshHeaderView, params);
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

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mDownX = event.getRawX();
        mDownY = event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMoveDistanceY = (int) mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveDistanceY = (int) (event.getRawY() - mMoveDistanceY);
                if (Math.abs(event.getRawX() - mDownX) < Math.abs(mMoveDistanceY)) {
                    if (shouldHandleRefresh() && mMoveDistanceY > 0) {
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
                mMoveDistanceY = 0;
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveDistanceY = (int) (event.getY() - mDownY);
                switch (mCurrentRefreshStatus) {
                    case IDLE:
                        if (shouldHandleRefresh() && mCurrentRefreshStatus != RefreshStatus.REFRESHING && mMoveDistanceY > 0) {
                            mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                        }
                        break;
                    case PULL_DOWN:
                        if (mMoveDistanceY < 0) {
                            mMoveDistanceY = 0;
                        }
                        if (mMoveDistanceY <= mRefreshHeaderViewHeight) {
                            setRefreshHeaderViewMarginTop(mMoveDistanceY);
                            mStickinessRefreshView.setHeight(0);
                            setChildHeight(mRefreshHeaderView, mRefreshHeaderViewHeight);
                        } else {
                            setRefreshHeaderViewMarginTop(mRefreshHeaderViewHeight);
                            mStickinessRefreshView.setHeight(mMoveDistanceY - mRefreshHeaderViewHeight);
                            setChildHeight(mRefreshHeaderView, mMoveDistanceY);
                            if (mStickinessRefreshView.isExceedMaximumHeight()) {
                                mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                            }
                        }
                        break;
                    case RELEASE_REFRESH:
                        smoothToOriginalSpot(mMoveDistanceY);
                        mCurrentRefreshStatus = RefreshStatus.REFRESHING;
                        break;
                    case REFRESHING:
                        if (null != mDelegate) mDelegate.onBGARefreshLayoutBeginRefreshing();
                        break;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN) {
                    if (mMoveDistanceY > mRefreshHeaderViewHeight) {
                        smoothToOriginalSpot(mMoveDistanceY);
                    } else {
                        smoothHideHeadView();
                    }
                }
                break;
        }
        return true;
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
                mCurrentRefreshStatus = RefreshStatus.IDLE;
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
        IDLE, PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }
}