package cn.bingoogolapple.acvp.viewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SwipeRecyclerViewItem extends ViewGroup {
    private final ViewDragHelper mDragHelper;
    private View mTopView;
    private View mBottomView;
    private int mDragRange;
    private int mSpringDistance = 30;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private float mDragOffset;
    private int mLeft;

    public SwipeRecyclerViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    }

    @Override
    protected void onFinishInflate() {
        mTopView = getChildAt(1);
        mBottomView = getChildAt(0);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mTopView;
        }

        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
            Toast.makeText(getContext(), "edgeTouched " + edgeFlags + " " + pointerId, Toast.LENGTH_SHORT).show();
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return mDragRange + mSpringDistance * 2;
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            mDragHelper.captureChildView(mTopView, pointerId);
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            Log.i("bingo", "left = " + left);
            final int leftBound = -mDragRange - mSpringDistance;
            final int rightBound = mSpringDistance;
            final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

            if(newLeft == leftBound) {
                smoothSlideTo(1.0f);
            } else if(newLeft == mSpringDistance) {
                smoothSlideTo(0.0f);
            }
            return newLeft;
        }


        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mLeft = left;
            mDragOffset = (float) mLeft / mDragRange;
//            mTopView.setPivotX(mTopView.getWidth());
//            mTopView.setPivotY(mTopView.getHeight());
//            mTopView.setScaleX(1 - mDragOffset / 2);
//            mTopView.setScaleY(1 - mDragOffset / 2);
//            mBottomView.setAlpha(1 - mDragOffset);
//            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int left = getPaddingLeft();
            if (yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                left += mDragRange;
            }
            mDragHelper.settleCapturedViewAt(left, releasedChild.getTop());
        }

        /**
         * 当拖拽到状态改变时回调
         *
         * @params 新的状态
         */
        @Override
        public void onViewDragStateChanged(int state) {
            switch (state) {
                case ViewDragHelper.STATE_DRAGGING:  // 正在被拖动
                    break;
                case ViewDragHelper.STATE_IDLE:  // view没有被拖拽或者 正在进行fling/snap
                    break;
                case ViewDragHelper.STATE_SETTLING: // fling完毕后被放置到一个位置
                    break;
            }
            super.onViewDragStateChanged(state);
        }
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(resolveSizeAndStateCompact(maxWidth, widthMeasureSpec, 0),
                resolveSizeAndStateCompact(maxHeight, heightMeasureSpec, 0));
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if ((action != MotionEvent.ACTION_DOWN)) {
            mDragHelper.cancel();
            return super.onInterceptTouchEvent(ev);
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDragHelper.isViewUnder(mTopView, (int) x, (int) y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float adx = Math.abs(x - mInitialMotionX);
                final float ady = Math.abs(y - mInitialMotionY);
                final int slop = mDragHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    return false;
                }
            }
        }
        return mDragHelper.shouldInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        boolean isHeaderViewUnder = mDragHelper.isViewUnder(mTopView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDragHelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isHeaderViewUnder) {
                    if (mDragOffset == 0) {
                        smoothSlideTo(1f);
                    } else {
                        smoothSlideTo(0f);
                    }
                }
                break;
            }
        }

        return isHeaderViewUnder && isViewHit(mTopView, (int) x, (int) y) || isViewHit(mBottomView, (int) x, (int) y);
    }

    public boolean smoothSlideTo(float slideOffset) {
        final int leftBound = getPaddingLeft();
        int left = (int) (leftBound + slideOffset * mDragRange);
        if (mDragHelper.smoothSlideViewTo(mTopView, left, mTopView.getTop())) {
            ViewCompat.postInvalidateOnAnimation(this);
            return true;
        }
        return false;
    }

    private boolean isViewHit(View view, int x, int y) {
        int[] viewLocation = new int[2];
        view.getLocationOnScreen(viewLocation);
        int[] parentLocation = new int[2];
        this.getLocationOnScreen(parentLocation);
        int screenX = parentLocation[0] + x;
        int screenY = parentLocation[1] + y;
        return screenX >= viewLocation[0] && screenX < viewLocation[0] + view.getWidth() &&
                screenY >= viewLocation[1] && screenY < viewLocation[1] + view.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = mBottomView.getWidth();
        mTopView.layout(0, 0, r, mBottomView.getMeasuredHeight());
        mBottomView.layout(r - mBottomView.getMeasuredHeight(), 0, r, mBottomView.getMeasuredHeight());
    }

}