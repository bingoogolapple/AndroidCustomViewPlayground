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
public class BGARefreshLayout extends LinearLayout implements StickinessRefreshView.OnStateChangedListener {
    private static final String TAG = BGARefreshLayout.class.getSimpleName();

    private View mRefreshHeaderView;
    private StickinessRefreshView mStickinessRefreshView;

    private AdapterView<?> mAdapterView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private View mNormalView;

    private float dx, dy;
    private int moveDistance;
    private int headOriginalHeight;

    private Handler handler;
    private MoveRunnable moveRunnable;
    private HeadMoveRunnable headMoveRunnable;

    private BGARefreshLayoutDelegate mDelegate;
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.IDLE;

    private class MoveRunnable implements Runnable {
        int startY;

        public MoveRunnable(int startY) {
            stopMovement();
            this.startY = startY;
        }

        @Override
        public void run() {
            startY += (headOriginalHeight - startY) * 0.5F;

            mStickinessRefreshView.setHeight(startY - headOriginalHeight);
            setChildHeight(mRefreshHeaderView, startY);

            if (startY != headOriginalHeight) {
                handler.postDelayed(moveRunnable, 20);
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
            params.topMargin += (-headOriginalHeight - params.topMargin) * 0.5F;

            mRefreshHeaderView.setLayoutParams(params);

            if (params.topMargin != -headOriginalHeight) {
                handler.postDelayed(headMoveRunnable, 20);
            } else {
                mCurrentRefreshStatus = RefreshStatus.IDLE;
            }
        }
    }

    public BGARefreshLayout(Context context) {
        super(context);
        init(context);
    }

    public BGARefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater inflater = LayoutInflater.from(context);
        mRefreshHeaderView = inflater.inflate(R.layout.head_pullrefresh, this, false);
        measureChild(mRefreshHeaderView);
        mStickinessRefreshView = (StickinessRefreshView) mRefreshHeaderView.findViewById(R.id.pull_widget);
        mStickinessRefreshView.setOnStateChangeListener(this);

        headOriginalHeight = mRefreshHeaderView.getMeasuredHeight();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, headOriginalHeight);
        params.topMargin = -headOriginalHeight;
        addView(mRefreshHeaderView, params);

        handler = new Handler();
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
        params.topMargin = height - headOriginalHeight;

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

        handler.post(moveRunnable);
    }

    private void stopMovement() {
        handler.removeCallbacks(moveRunnable);
        handler.removeCallbacks(headMoveRunnable);
    }

    private void smoothHideHeadView() {
        headMoveRunnable = new HeadMoveRunnable();

        handler.post(headMoveRunnable);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        dx = event.getRawX();
        dy = event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                moveDistance = (int) dy;
                break;
            case MotionEvent.ACTION_MOVE:
                moveDistance = (int) (event.getRawY() - moveDistance);
                if (Math.abs(event.getRawX() - dx) < Math.abs(moveDistance)) {
                    if (shouldRefreshStart() && moveDistance > 0) {
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
        Log.d(TAG, "MODE:" + mCurrentRefreshStatus);
        Log.d(TAG, "TOUCH:" + event.toString());
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                stopMovement();
                moveDistance = 0;
                dx = event.getX();
                dy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                moveDistance = (int) (event.getY() - dy);
                switch (mCurrentRefreshStatus) {
                    case IDLE:
                        Log.d(TAG, "IDLE");
                        if (shouldRefreshStart() && mCurrentRefreshStatus != RefreshStatus.REFRESHING && moveDistance > 0)
                            mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                        break;
                    case PULL_DOWN:
                        if (moveDistance < 0) {
                            moveDistance = 0;
                        }
                        Log.d(TAG, "DRAGGING");
                        if (moveDistance <= headOriginalHeight) {
                            setHeadMargin((int) moveDistance);
                            mStickinessRefreshView.setHeight(0);
                            setChildHeight(mRefreshHeaderView, headOriginalHeight);
                        } else {
                            setHeadMargin(headOriginalHeight);
                            mStickinessRefreshView.setHeight((int) moveDistance - headOriginalHeight);
                            setChildHeight(mRefreshHeaderView, (int) moveDistance);
                            if (mStickinessRefreshView.isExceedMaximumHeight()) {
                                mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                            }
                        }
                        break;
                    case RELEASE_REFRESH:
                        smoothToOriginalSpot(moveDistance);
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
                    if (moveDistance > headOriginalHeight) {
                        smoothToOriginalSpot(moveDistance);
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