package cn.bingoogolapple.acvp.refreshrecyclerview.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
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
    private StickinessRefreshView mStickinessRefreshView;

    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private View mNormalView;

    private float mDownX;
    private float mDownY;
    private int mMoveDistanceY;

    private int mRefreshHeaderViewHeight;


    private MoveRunnable moveRunnable;
    private HeadMoveRunnable headMoveRunnable;

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
        measureChild(mRefreshHeaderView);
        mStickinessRefreshView = (StickinessRefreshView) mRefreshHeaderView.findViewById(R.id.stickinessRefreshView);
        mStickinessRefreshView.setDelegate(this);

        mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mRefreshHeaderViewHeight);
        params.topMargin = -mRefreshHeaderViewHeight;
        addView(mRefreshHeaderView, params);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        initContent();
    }

    private void initContent() {
        int count = getChildCount();
        if (count != 2)
            throw new ArrayIndexOutOfBoundsException("this widget contain one child view at most and must contain one.");

        View view = getChildAt(1);
        if (view instanceof AdapterView<?>) {
            mAdapterView = (AdapterView<?>) view;
        } else if (view instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) view;
        } else if (view instanceof ScrollView) {
            mScrollView = (ScrollView) view;
        } else {
            mNormalView = view;
        }
    }

    private void measureChild(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (null == params) {
            params = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, params.width);
        int childHeightSpec;
        if (params.height > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        child.measure(childWidthSpec, childHeightSpec);
    }

    private void setChildHeight(View child, int height) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        params.height = height;
        child.setLayoutParams(params);
    }

    private void setHeadMargin(int height) {
        LayoutParams params = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        params.topMargin = height - mRefreshHeaderViewHeight;

        mRefreshHeaderView.setLayoutParams(params);
    }

    private boolean shouldRefreshStart() {
        if (null != mNormalView) {
            return true;
        }
        if (null != mScrollView) {
            if (mScrollView.getChildAt(0).getScrollY() == 0) return true;
        }
        if (null != mAdapterView) {
            int top = mAdapterView.getChildAt(0).getTop();
            int padding = mAdapterView.getPaddingTop();
            if (mAdapterView.getFirstVisiblePosition() == 0) {
                if (top == 0 || Math.abs(top - padding) <= 3) {
                    return true;
                }
            }
        }
        if (null != mRecyclerView) {
            int top = mRecyclerView.getChildAt(0).getTop();
            int padding = mRecyclerView.getPaddingTop();
            LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
            if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                if (top == 0 || Math.abs(top - padding) <= 3) {
                    return true;
                }
            }
        }
        return false;
    }

    private void smoothToOriginalSpot(int y) {
        moveRunnable = new MoveRunnable(y);

        mHandler.post(moveRunnable);
    }

    private void stopMovement() {
        mHandler.removeCallbacks(moveRunnable);
        mHandler.removeCallbacks(headMoveRunnable);
    }

    private void smoothHideHeadView() {
        headMoveRunnable = new HeadMoveRunnable();

        mHandler.post(headMoveRunnable);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mDownX = event.getRawX();
        mDownY = event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mMoveDistanceY = (int) mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mMoveDistanceY = (int) (event.getRawY() - mMoveDistanceY);
                if (Math.abs(event.getRawX() - mDownX) < Math.abs(mMoveDistanceY)) {
                    if (shouldRefreshStart() && mMoveDistanceY > 0) {
                        Log.d(TAG, "shouldRefreshStart");
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
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
                        Log.d(TAG, "IDLE");
                        if (shouldRefreshStart() && mCurrentRefreshStatus != RefreshStatus.REFRESHING && mMoveDistanceY > 0)
                            mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                        break;
                    case PULL_DOWN:
                        if (mMoveDistanceY < 0) {
                            mMoveDistanceY = 0;
                        }
                        if (mMoveDistanceY <= mRefreshHeaderViewHeight) {
                            setHeadMargin(mMoveDistanceY);
                            mStickinessRefreshView.setHeight(0);
                            setChildHeight(mRefreshHeaderView, mRefreshHeaderViewHeight);
                        } else {
                            setHeadMargin(mRefreshHeaderViewHeight);
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

    private class MoveRunnable implements Runnable {
        int startY;

        public MoveRunnable(int startY) {
            stopMovement();
            this.startY = startY;
        }

        @Override
        public void run() {
            startY += (mRefreshHeaderViewHeight - startY) * 0.5F;

            mStickinessRefreshView.setHeight(startY - mRefreshHeaderViewHeight);
            setChildHeight(mRefreshHeaderView, startY);

            if (startY != mRefreshHeaderViewHeight) {
                mHandler.postDelayed(moveRunnable, 20);
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

    public class HeadMoveRunnable implements Runnable {
        LayoutParams params;

        public HeadMoveRunnable() {
            params = (LayoutParams) mRefreshHeaderView.getLayoutParams();
        }

        @Override
        public void run() {
            params.topMargin += (-mRefreshHeaderViewHeight - params.topMargin) * 0.5F;

            mRefreshHeaderView.setLayoutParams(params);

            if (params.topMargin != -mRefreshHeaderViewHeight) {
                mHandler.postDelayed(headMoveRunnable, 20);
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