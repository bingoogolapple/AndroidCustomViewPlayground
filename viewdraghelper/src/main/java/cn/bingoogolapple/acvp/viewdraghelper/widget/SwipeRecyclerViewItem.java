package cn.bingoogolapple.acvp.viewdraghelper.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import cn.bingoogolapple.acvp.viewdraghelper.R;

public class SwipeRecyclerViewItem extends RelativeLayout {
    private static final String TAG = HelloWorldView.class.getSimpleName();
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
    private Status mStatus = Status.Closed;
    // 顶部视图下一次layout时的left
    private int mTopLeft;

    private MarginLayoutParams mTopLp;
    private MarginLayoutParams mBottomLp;

    private float mDragOffset;

    public SwipeRecyclerViewItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initProperty();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeRecyclerViewItem);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        switch (attr) {
            case R.styleable.SwipeRecyclerViewItem_srvi_swipeDirection:
                // 默认向左滑动
                int leftSwipeDirection = typedArray.getInt(attr, mSwipeDirection.ordinal());

                if (leftSwipeDirection == SwipeDirection.Right.ordinal()) {
                    mSwipeDirection = SwipeDirection.Right;
                }
                break;
            case R.styleable.SwipeRecyclerViewItem_srvi_bottomMode:
                // 默认是拉出
                int pullOutBottomMode = typedArray.getInt(attr, mBottomModel.ordinal());

                if (pullOutBottomMode == BottomModel.LayDown.ordinal()) {
                    mBottomModel = BottomModel.LayDown;
                }
                break;
            case R.styleable.SwipeRecyclerViewItem_srvi_springDistance:
                // 弹簧距离，不能小于0，默认值为0
                mSpringDistance = typedArray.getDimensionPixelSize(attr, mSpringDistance);
                if (mSpringDistance < 0) {
                    throw new RuntimeException("srvi_springDistance不能小于0");
                }
                break;
            default:
                break;
        }
    }

    private void initProperty() {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mTopLeft = getPaddingLeft();

        Log.i(TAG, "mSpringDistance = " + mSpringDistance + " mTopLeft = " + mTopLeft);
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

        mTopLp = (MarginLayoutParams) mTopView.getLayoutParams();
        mBottomLp = (MarginLayoutParams) mBottomView.getLayoutParams();

        Log.i(TAG, "onFinishInflate");
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        measureChildren(widthMeasureSpec, heightMeasureSpec);
//        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(ViewCompat.resolveSizeAndState(maxWidth, widthMeasureSpec, 0), ViewCompat.resolveSizeAndState(maxHeight, heightMeasureSpec, 0));
//    }

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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = mBottomView.getWidth();




        int topTop = getPaddingTop() + mTopLp.topMargin;
        int topBottom = topTop + mTopView.getMeasuredHeight();
        int topRight = mTopLeft + mTopView.getMeasuredWidth();

        int bottomTop = getPaddingTop() + mBottomLp.topMargin;
        int bottomBottom = bottomTop + mBottomView.getMeasuredHeight();
        int bottomLeft = 0;
        int bottomRight = 0;

        Log.i(TAG, "height  " + getMeasuredHeight() + "  ---  topTop " + topTop + "  ---  topBottom  " + topBottom + "  ---  topHeight  " + mTopView.getMeasuredHeight());
        Log.i(TAG, "height  " + getMeasuredHeight() + "  ---  bottomTop " + bottomTop + "  ---  bottomBottom  " + bottomBottom + "  ---  bottomHeight  " + mBottomView.getMeasuredHeight());

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
                int minBottomLeft = getMeasuredWidth() - getPaddingRight() - mBottomView.getMeasuredWidth();
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
                bottomLeft = mTopLeft - mBottomView.getMeasuredWidth() - mTopLp.leftMargin - mBottomLp.rightMargin - mBottomLp.leftMargin;
                // 底部视图的left被允许的最大值
                int maxBottomLeft = getPaddingLeft();
                // 获取最终的left
                bottomLeft = Math.min(maxBottomLeft, bottomLeft);
                // 根据left计算right
                bottomRight = bottomLeft + mBottomView.getMeasuredWidth();
            }
        }

        mBottomView.layout(bottomLeft, bottomTop, bottomRight, bottomBottom);
        mTopView.layout(mTopLeft, topTop, topRight, topBottom);
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
        public int getViewVerticalDragRange(View child) {
            return 0;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 这里要返回控件的getPaddingTop() + mTopLp.topMargin，否则快速滑动松手时会上下跳动
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
            int minTopLeft = 0;
            int maxTopLeft = 0;
            if (mSwipeDirection == SwipeDirection.Left) {
                // 向左滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft() - (mDragRange + mSpringDistance);
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft();
            } else {
                // 向右滑动

                // 顶部视图的left被允许的最小值
                minTopLeft = getPaddingLeft();
                // 顶部视图的left被允许的最大值
                maxTopLeft = getPaddingLeft() + (mDragRange + mSpringDistance);
            }

            left = Math.min(Math.max(minTopLeft, left), maxTopLeft);
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mTopLeft = left;
            /**
             * 打开过程
             * mTopLeft                                         0 -->  mDragRange
             * mDragOffset = 1.0f * mTopLeft / mDragRange       0 -->  1.0
             */

            if (Math.abs(mTopLeft - getPaddingLeft()) > mDragRange) {
                mDragOffset = 1.0f;
            } else {
                mDragOffset = 1.0f * Math.abs(mTopLeft - getPaddingLeft()) / mDragRange;
            }

            float alpha = 0.3f + 0.7f * mDragOffset;
            ViewCompat.setAlpha(mBottomView, alpha);

            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalLeft = getPaddingLeft();
            // 默认是关闭
            if (mSwipeDirection == SwipeDirection.Left) {
                if ((xvel < 0 && Math.abs(xvel) > Math.abs(yvel)) || (xvel == 0 && mDragOffset > 0.5f)) {
                    finalLeft -= mDragRange;
                }
            } else {
                if ((xvel > 0 && xvel > yvel) || (xvel == 0 && mDragOffset > 0.5f)) {
                    finalLeft += mDragRange;
                }
            }
            mDragHelper.settleCapturedViewAt(finalLeft, getPaddingTop() + mTopLp.topMargin);

            // 要执行下面的代码，不然不会自动收缩完毕或展开完毕
            ViewCompat.postInvalidateOnAnimation(SwipeRecyclerViewItem.this);
        }

//        @Override
//        public void onViewReleased(View releasedChild, float xvel, float yvel) {
//            // 默认是关闭
//            if (mSwipeDirection == SwipeDirection.Left) {
//                if ((xvel < 0 && Math.abs(xvel) > Math.abs(yvel)) || (xvel == 0 && mDragOffset > 0.5f)) {
//                    open();
//                } else {
//                    close();
//                }
//            } else {
//                if ((xvel > 0 && xvel > yvel) || (xvel == 0 && mDragOffset > 0.5f)) {
//                    open();
//                } else {
//                    close();
//                }
//            }
//        }

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