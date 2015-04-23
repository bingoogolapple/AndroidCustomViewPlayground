package cn.bingoogolapple.acvp.viewdraghelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import cn.bingoogolapple.acvp.viewdraghelper.R;

public class SwipeRecyclerViewItem extends ViewGroup {
    private static final String TAG = HelloWorldView.class.getSimpleName();
    private final ViewDragHelper mDragHelper;
    private View mTopView;
    private View mBottomView;
    private int mDragRange;
    // 拖动的弹簧距离
    private int mSpringDistance = 0;

    private float mDragOffset;
    private int mTopViewLeft;
    private Status mStatus = Status.Closed;

    // 默认向左滑动
    private SwipeDirection mSwipeDirection = SwipeDirection.Left;

    private BottomModel mBottomModel = BottomModel.LayDown;

    public SwipeRecyclerViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        Log.i(TAG, "mSpringDistance = " + mSpringDistance);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerViewItem);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    public void initAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.SwipeRecyclerViewItem_srvi_swipeDirection) {
            // 默认向左滑动
            int swipeDirection = typedArray.getInt(attr, mSwipeDirection.ordinal());
            if (swipeDirection == SwipeDirection.Right.ordinal()) {
                mSwipeDirection = SwipeDirection.Right;
            }
        } else if (attr == R.styleable.SwipeRecyclerViewItem_srvi_bottomMode) {
            int bottomMode = typedArray.getInt(attr, mBottomModel.ordinal());
            if (bottomMode == BottomModel.PullOut.ordinal()) {
                mBottomModel = BottomModel.PullOut;
            }
        } else if (attr == R.styleable.SwipeRecyclerViewItem_srvi_springDistance) {
            mSpringDistance = typedArray.getDimensionPixelSize(attr, mSpringDistance);
            if (mSpringDistance < 0) {
                throw new RuntimeException("srvi_springDistance不能小于0");
            }
        }
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() != 2) {
            throw new RuntimeException(HelloWorldView.class.getSimpleName() + "必须有且只有两个子控件");
        }
        mTopView = getChildAt(1);
        mBottomView = getChildAt(0);
        // 避免底部视图被隐藏时还能获取焦点被点击
        mBottomView.setVisibility(INVISIBLE);
        Log.i(TAG, "onFinishInflate");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndStateCompact(maxWidth, widthMeasureSpec, 0), resolveSizeAndStateCompact(maxHeight, heightMeasureSpec, 0));
    }

    public static int resolveSizeAndStateCompact(int size, int measureSpec, int childMeasuredState) {
        int result = size;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
            case MeasureSpec.AT_MOST:
                if (specSize < size) {
                    result = specSize | MEASURED_STATE_TOO_SMALL;
                } else {
                    result = size;
                }
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result | (childMeasuredState & MEASURED_STATE_MASK);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = mBottomView.getWidth();
        if (mSwipeDirection == SwipeDirection.Left) {
            mTopView.layout(mTopViewLeft, getPaddingTop(), mTopViewLeft + mTopView.getMeasuredWidth(), mTopView.getMeasuredHeight());

            if (mBottomModel == BottomModel.LayDown) {
                mBottomView.layout(r - mBottomView.getMeasuredWidth() - getPaddingRight(), getPaddingTop(), r, mBottomView.getMeasuredHeight());
            } else {
                int bottomLeft = mTopViewLeft + mTopView.getMeasuredWidth();
                int minBottomLeft = getMeasuredWidth() - getPaddingRight() - mBottomView.getMeasuredWidth();
                bottomLeft = bottomLeft < minBottomLeft ? minBottomLeft : bottomLeft;
                mBottomView.layout(bottomLeft, getPaddingTop(), bottomLeft + mBottomView.getMeasuredWidth(), mBottomView.getMeasuredHeight());
            }
        } else {
            mTopView.layout(mTopViewLeft, getPaddingTop(), mTopViewLeft + mTopView.getMeasuredWidth(), mTopView.getMeasuredHeight());

            if (mBottomModel == BottomModel.LayDown) {
                mBottomView.layout(getPaddingLeft(), getPaddingTop(), mBottomView.getMeasuredWidth(), mBottomView.getMeasuredHeight());
            } else {
                int bottomLeft = mTopViewLeft - mBottomView.getMeasuredWidth();
                int maxBottomLeft = getPaddingLeft();
                bottomLeft = bottomLeft > maxBottomLeft ? maxBottomLeft : bottomLeft;
                mBottomView.layout(bottomLeft, getPaddingTop(), bottomLeft + mBottomView.getMeasuredWidth(), mBottomView.getMeasuredHeight());
            }
        }
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    public void open() {
        smoothSlideTo(1.0f);
    }

    public void close() {
        smoothSlideTo(0.0f);
    }

    private void smoothSlideTo(float slideOffset) {
        int left = getPaddingLeft();
        if (mSwipeDirection == SwipeDirection.Left) {
            left = (int) (left - slideOffset * mDragRange);
        } else {
            left = (int) (left + slideOffset * mDragRange);
        }

        if (mDragHelper.smoothSlideViewTo(mTopView, left, mTopView.getTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public boolean isOpened() {
        return getStatus() == Status.Opened;
    }

    public boolean isClose() {
        return getStatus() == Status.Closed;
    }

    public boolean isMoving() {
        return getStatus() == Status.Moving;
    }

    public Status getStatus() {
        return mStatus;
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTopView;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Log.i(TAG, "onEdgeTouched edgeFlags=" + edgeFlags + " pointerId=" + pointerId);
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            Log.i(TAG, "onEdgeDragStarted edgeFlags=" + edgeFlags + " pointerId=" + pointerId);
            mDragHelper.captureChildView(mTopView, pointerId);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange + mSpringDistance;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mSwipeDirection == SwipeDirection.Left) {
                return Math.min(Math.max(-(mDragRange + mSpringDistance), left), 0);
            } else {
                return Math.min(Math.max(getPaddingLeft(), left), (mDragRange + mSpringDistance));
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTopViewLeft = left;
            /**
             * 打开过程
             * mTopViewLeft                                         0 -->  mDragRange
             * mDragOffset = 1.0f * mTopViewLeft / mDragRange       0 -->  1.0
             */

            if (mTopViewLeft > mDragRange) {
                mDragOffset = 1.0f;
            } else {
                mDragOffset = 1.0f * Math.abs(mTopViewLeft) / mDragRange;
            }

            float alpha = 0.3f + 0.7f * mDragOffset;
            ViewCompat.setAlpha(mBottomView, alpha);

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalLeft = getPaddingLeft();
            if (mSwipeDirection == SwipeDirection.Left) {
                if ((xvel < 0 && Math.abs(xvel) > Math.abs(yvel)) || (xvel == 0 && mDragOffset > 0.5f)) {
                    finalLeft -= mDragRange;
                }
            } else {
                if ((xvel > 0 && xvel > yvel) || (xvel == 0 && mDragOffset > 0.5f)) {
                    finalLeft += mDragRange;
                }
            }
            mDragHelper.settleCapturedViewAt(finalLeft, releasedChild.getTop());


            // 要执行下面的代码，不然不会自动收缩完毕或展开完毕
            ViewCompat.postInvalidateOnAnimation(SwipeRecyclerViewItem.this);
        }

        /**
         * 当拖拽到状态改变时回调
         *
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:
                    /**
                     * A view is currently being dragged. The position is currently changing as a result
                     * of user input or simulated user input.
                     */
                    Log.i(TAG, "开始拖动");
                    mBottomView.setVisibility(VISIBLE);
                    mStatus = Status.Moving;
                    break;
                case ViewDragHelper.STATE_SETTLING:
                    /**
                     * A view is currently settling into place as a result of a fling or
                     * predefined non-interactive motion.
                     */
                    // 此时还没移动到要被放置的位置
                    Log.i(TAG, "fling完毕后被放置到一个位置" + mTopView.getLeft());
                    mBottomView.setVisibility(VISIBLE);
                    mStatus = Status.Moving;
                    break;
                case ViewDragHelper.STATE_IDLE:
                    // A view is not currently being dragged or animating as a result of a fling/snap.
                    Log.i(TAG, "view没有被拖拽或者fling/snap结束" + mTopView.getLeft());
                    if (mTopView.getLeft() == getPaddingLeft()) {
                        mBottomView.setVisibility(INVISIBLE);
                        mStatus = Status.Closed;
                        Log.i(TAG, "处于关闭状态");
                    } else {
                        Log.i(TAG, "处于打开状态");
                        mStatus = Status.Opened;
                    }
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
}