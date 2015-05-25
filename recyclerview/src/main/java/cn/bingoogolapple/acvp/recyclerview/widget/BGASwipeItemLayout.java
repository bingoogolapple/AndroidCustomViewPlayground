package cn.bingoogolapple.acvp.recyclerview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import cn.bingoogolapple.acvp.recyclerview.R;

public class BGASwipeItemLayout extends RelativeLayout {
    private static final String TAG = BGASwipeItemLayout.class.getSimpleName();
    private static final String INSTANCE_STATUS = "instance_status";
    private static final String STATUS_OPEN_CLOSE = "status_open_close";
    private static final int VEL_THRESHOLD = 400;
    private ViewDragHelper mDragHelper;
    // 顶部视图
    private View mTopView;
    // 底部视图
    private View mBottomView;
    // 拖动的弹簧距离
    private int mSpringDistance = 0;
    // 允许拖动的距离【注意：最终允许拖动的距离是 (mDragRange + mSpringDistance)】
    private int mDragRange;
    // 控件滑动方向（向左，向右），默认向左滑动
    private SwipeDirection mSwipeDirection = SwipeDirection.Left;
    // 移动过程中，底部视图的移动方式（拉出，被顶部视图遮住），默认是被顶部视图遮住
    private BottomModel mBottomModel = BottomModel.PullOut;
    // 滑动控件当前的状态（打开，关闭，正在移动），默认是关闭状态
    private Status mCurrentStatus = Status.Closed;
    // 滑动控件滑动前的状态
    private Status mPreStatus = mCurrentStatus;
    // 顶部视图下一次layout时的left
    private int mTopLeft;
    // 顶部视图外边距
    private MarginLayoutParams mTopLp;
    // 底部视图外边距
    private MarginLayoutParams mBottomLp;
    // 滑动比例，【关闭->展开  =>  0->1】
    private float mDragRatio;
    // 手动拖动打开和关闭代理
    private BGASwipeItemLayoutDelegate mDelegate;

    private float mInterceptTouchDownX = -1;
    private float mInterceptTouchDownY = -1;

    public BGASwipeItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGASwipeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initProperty();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGASwipeItemLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        switch (attr) {
            case R.styleable.BGASwipeItemLayout_bga_sil_swipeDirection:
                // 默认向左滑动
                int leftSwipeDirection = typedArray.getInt(attr, mSwipeDirection.ordinal());

                if (leftSwipeDirection == SwipeDirection.Right.ordinal()) {
                    mSwipeDirection = SwipeDirection.Right;
                }
                break;
            case R.styleable.BGASwipeItemLayout_bga_sil_bottomMode:
                // 默认是拉出
                int pullOutBottomMode = typedArray.getInt(attr, mBottomModel.ordinal());

                if (pullOutBottomMode == BottomModel.LayDown.ordinal()) {
                    mBottomModel = BottomModel.LayDown;
                }
                break;
            case R.styleable.BGASwipeItemLayout_bga_sil_springDistance:
                // 弹簧距离，不能小于0，默认值为0
                mSpringDistance = typedArray.getDimensionPixelSize(attr, mSpringDistance);
                if (mSpringDistance < 0) {
                    throw new RuntimeException("bga_sil_springDistance不能小于0");
                }
                break;
            default:
                break;
        }
    }

    private void initProperty() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    public void setDelegate(BGASwipeItemLayoutDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() != 2) {
            throw new RuntimeException(BGASwipeItemLayout.class.getSimpleName() + "必须有且只有两个子控件");
        }
        mTopView = getChildAt(1);
        mBottomView = getChildAt(0);
        // 避免底部视图被隐藏时还能获取焦点被点击
        mBottomView.setVisibility(INVISIBLE);

        mTopLp = (MarginLayoutParams) mTopView.getLayoutParams();
        mBottomLp = (MarginLayoutParams) mBottomView.getLayoutParams();
        mTopLeft = getPaddingLeft() + mTopLp.leftMargin;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
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
                if (mInterceptTouchDownX == -1) {
                    mInterceptTouchDownX = (int) event.getRawX();
                }
                if (mInterceptTouchDownY == -1) {
                    mInterceptTouchDownY = (int) event.getRawY();
                }

                int interceptTouchMoveDistanceX = (int) (event.getRawX() - mInterceptTouchDownX);
                if (Math.abs(event.getRawY() - mInterceptTouchDownY) < Math.abs(interceptTouchMoveDistanceX) || isMoving()) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    super.onInterceptTouchEvent(event);
                    return true;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 重置
                mInterceptTouchDownX = -1;
                mInterceptTouchDownY = -1;

                mDragHelper.cancel();
                if (isMoving()) {
                    return true;
                }
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (isMoving()) {
                    event.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(event);
                    return true;
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (isMoving()) {
                    return true;
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = mBottomView.getMeasuredWidth() + mBottomLp.leftMargin + mBottomLp.rightMargin;

        int topTop = getPaddingTop() + mTopLp.topMargin;
        int topBottom = topTop + mTopView.getMeasuredHeight();
        int topRight = mTopLeft + mTopView.getMeasuredWidth();

        int bottomTop = getPaddingTop() + mBottomLp.topMargin;
        int bottomBottom = bottomTop + mBottomView.getMeasuredHeight();
        int bottomLeft;
        int bottomRight;

        if (mSwipeDirection == SwipeDirection.Left) {
            // 向左滑动

            if (mBottomModel == BottomModel.LayDown) {
                // 遮罩，位置固定不变（先计算right，然后根据right计算left）

                bottomRight = r - getPaddingRight() - mBottomLp.rightMargin;
                bottomLeft = bottomRight - mBottomView.getMeasuredWidth();
            } else {
                // 拉出，位置随顶部视图的位置改变

                // 根据顶部视图的left计算底部视图的left
                bottomLeft = mTopLeft + mTopView.getMeasuredWidth() + mTopLp.rightMargin + mBottomLp.leftMargin;

                // 底部视图的left被允许的最小值
                int minBottomLeft = r - getPaddingRight() - mBottomView.getMeasuredWidth() - mBottomLp.rightMargin;
                // 获取最终的left
                bottomLeft = Math.max(bottomLeft, minBottomLeft);
                // 根据left计算right
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        } else {
            // 向右滑动

            if (mBottomModel == BottomModel.LayDown) {
                // 遮罩，位置固定不变（先计算left，然后根据left计算right）

                bottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            } else {
                // 拉出，位置随顶部视图的位置改变

                // 根据顶部视图的left计算底部视图的left
                bottomLeft = mTopLeft - mDragRange;
                // 底部视图的left被允许的最大值
                int maxBottomLeft = getPaddingLeft() + mBottomLp.leftMargin;
                // 获取最终的left
                bottomLeft = Math.min(maxBottomLeft, bottomLeft);
                // 根据left计算right
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        }

        mBottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);
        mTopView.layout(mTopLeft, topTop, topRight, topBottom);
    }

    public void openWithAnim() {
        smoothSlideTo(1);
    }

    public void closeWithAnim() {
        smoothSlideTo(0);
    }

    public void open() {
        slideTo(1);
    }

    public void close() {
        slideTo(0);
    }

    public View getTopView() {
        return mTopView;
    }

    public View getBottomView() {
        return mBottomView;
    }

    /**
     * 打开或关闭滑动控件
     *
     * @param isOpen 1表示打开，0表示关闭
     */
    private void smoothSlideTo(int isOpen) {
        if (mDragHelper.smoothSlideViewTo(mTopView, getCloseOrOpenTopViewFinalLeft(isOpen), getPaddingTop() + mTopLp.topMargin)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 打开或关闭滑动控件
     *
     * @param isOpen 1表示打开，0表示关闭
     */
    private void slideTo(int isOpen) {
        if (isOpen == 1) {
            mBottomView.setVisibility(VISIBLE);
            ViewCompat.setAlpha(mBottomView, 1.0f);
            mCurrentStatus = Status.Opened;
        } else {
            mBottomView.setVisibility(INVISIBLE);
            mCurrentStatus = Status.Closed;
        }
        mPreStatus = mCurrentStatus;
        mTopLeft = getCloseOrOpenTopViewFinalLeft(isOpen);
        requestLayout();
    }

    private int getCloseOrOpenTopViewFinalLeft(int isOpen) {
        int left = getPaddingLeft() + mTopLp.leftMargin;
        if (mSwipeDirection == SwipeDirection.Left) {
            left = left - isOpen * mDragRange;
        } else {
            left = left + isOpen * mDragRange;
        }
        return left;
    }

    public boolean isOpened() {
        return getStatus() == Status.Opened;
    }

    public boolean isClosed() {
        return getStatus() == Status.Closed;
    }

    public boolean isMoving() {
        return getStatus() == Status.Moving;
    }

    public Status getStatus() {
        return mCurrentStatus;
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putInt(STATUS_OPEN_CLOSE, mCurrentStatus.ordinal());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            if (bundle.getInt(STATUS_OPEN_CLOSE) == Status.Opened.ordinal()) {
                open();
            } else {
                close();
            }
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        // 在响应打开和关闭结束时，是否要通知代理（只有是手动拖动打开和关闭时才通知代理）
        private boolean mIsNeedNotify = false;

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTopView;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 这里要返回控件的getPaddingTop() + mTopLp.topMargin，否则有margin和padding快速滑动松手时会上下跳动
            return getPaddingTop() + mTopLp.topMargin;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange + mSpringDistance;
        }

        /**
         *
         * @param child
         * @param left ViewDragHelper帮我们计算的当前所捕获的控件的left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            int minTopLeft;
            int maxTopLeft;
            if (mSwipeDirection == SwipeDirection.Left) {
                // 向左滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin - (mDragRange + mSpringDistance);
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin;
            } else {
                // 向右滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft() + mTopLp.leftMargin;
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft() + mTopLp.leftMargin + (mDragRange + mSpringDistance);
            }

            return Math.min(Math.max(minTopLeft, left), maxTopLeft);
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTopLeft = left;

            // 此时顶部视图水平方向偏移量的绝对值
            int topViewHorizontalOffset = Math.abs(mTopLeft - (getPaddingLeft() + mTopLp.leftMargin));
            if (topViewHorizontalOffset > mDragRange) {
                mDragRatio = 1.0f;
            } else {
                mDragRatio = 1.0f * topViewHorizontalOffset / mDragRange;
            }

            // 处理底部视图的透明度
            float alpha = 0.1f + 0.9f * mDragRatio;
            ViewCompat.setAlpha(mBottomView, alpha);

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // 默认关闭，接下来再判断为打开时的条件
            int finalLeft = getPaddingLeft() + mTopLp.leftMargin;

            if (mSwipeDirection == SwipeDirection.Left) {
                // 向左滑动为打开，向右滑动为关闭

                if (xvel < -VEL_THRESHOLD || (xvel < VEL_THRESHOLD && mDragRatio > 0.5f)) {
                    finalLeft -= mDragRange;
                }
            } else {
                // 向左滑动为关闭，向右滑动为打开

                if (xvel > VEL_THRESHOLD || (xvel > -VEL_THRESHOLD && mDragRatio > 0.5f)) {
                    finalLeft += mDragRange;
                }
            }
            mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop() + mTopLp.topMargin);

            // 要执行下面的代码，不然不会自动收缩完毕或展开完毕
            ViewCompat.postInvalidateOnAnimation(BGASwipeItemLayout.this);
        }

        /**
         * 当拖拽状态改变时回调
         *
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                // 步骤1：开始拖动
                case ViewDragHelper.STATE_DRAGGING:
                    mBottomView.setVisibility(VISIBLE);
                    mCurrentStatus = Status.Moving;
                    mIsNeedNotify = true;
                    break;
                // 步骤2：fling松开手或者直接设置视图到某个位置
                case ViewDragHelper.STATE_SETTLING:
                    mBottomView.setVisibility(VISIBLE);
                    mCurrentStatus = Status.Moving;
                    break;
                // 步骤3：视图完成移动到步骤2中设置的位置，并停止移动
                case ViewDragHelper.STATE_IDLE:
                    if (mTopView.getLeft() == getPaddingLeft() + mTopLp.leftMargin) {
                        mBottomView.setVisibility(INVISIBLE);
                        mCurrentStatus = Status.Closed;
                        if (mIsNeedNotify && mDelegate != null && mPreStatus != mCurrentStatus) {
                            mDelegate.onClosed(BGASwipeItemLayout.this);
                        }
                    } else {
                        mCurrentStatus = Status.Opened;
                        if (mIsNeedNotify && mDelegate != null && mPreStatus != mCurrentStatus) {
                            mDelegate.onOpened(BGASwipeItemLayout.this);
                        }
                    }
                    mPreStatus = mCurrentStatus;
                    mIsNeedNotify = false;
                    break;
            }
        }
    };

    public enum SwipeDirection {
        Left, Right
    }

    public enum BottomModel {
        PullOut, LayDown
    }

    public enum Status {
        Opened, Closed, Moving
    }

    public interface BGASwipeItemLayoutDelegate {
        void onOpened(BGASwipeItemLayout swipeItemLayout);

        void onClosed(BGASwipeItemLayout swipeItemLayout);
    }
}