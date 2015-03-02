package cn.bingoogolapple.acvp.viewdraghelper.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class YouTubeLayout extends ViewGroup {
    private final ViewDragHelper mDragHelper;
    private View mHeaderView;
    private View mFooterView;
    private int mDragRange;
    private int mHeaderViewTop;

    private float mDragOffset;

    private int mDownX;
    private int mDownY;

    public YouTubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        mHeaderView = getChildAt(0);
        mFooterView = getChildAt(1);
    }

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
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        mHeaderView.layout(0, mHeaderViewTop, r, mHeaderViewTop + mHeaderView.getMeasuredHeight());
        mFooterView.layout(0, mHeaderViewTop + mHeaderView.getMeasuredHeight(), r, mHeaderViewTop + b);
    }

    @Override
    public void computeScroll() {
        //这个是为了将事件执行到底
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * 展开
     */
    public void expand() {
        smoothSlideTo(0.0f);
    }

    /**
     * 收缩
     */
    public void shrink() {
        smoothSlideTo(1.0f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }

        final int currentX = (int) event.getX();
        final int currentY = (int) event.getY();
        boolean interceptTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = currentX;
                mDownY = currentY;
                interceptTap = mDragHelper.isViewUnder(mHeaderView, currentX, currentY);
                break;
            case MotionEvent.ACTION_MOVE:
                final float adx = Math.abs(currentX - mDownX);
                final float ady = Math.abs(currentY - mDownY);
                final int slop = mDragHelper.getTouchSlop();
                if (ady > slop && adx > ady) {
                    mDragHelper.cancel();
                    return false;
                }
                break;
        }
        return mDragHelper.shouldInterceptTouchEvent(event) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //同样是传递事件给ViewdragHelper
        mDragHelper.processTouchEvent(event);
        final int currentX = (int) event.getX();
        final int currentY = (int) event.getY();
        handleClickEvent(event, currentX, currentY);

        //判断当前的点击事件是在TopView或者是在BottomView上，如果在其上，则返回true
        //如果是在TopView上，事件则传递给ViewDragHelper处理，如果是在BottomView上，则自然的传递下去。如果是在其他地方，则返回false，将事件传递下去。
        return mDragHelper.isViewUnder(mHeaderView, currentX, currentY) || isInMySubView(mHeaderView, currentX, currentY) || isInMySubView(mFooterView, currentX, currentY);
    }

    //手势的放下，抬起的处理，是为了针对点击事件做的处理
    private void handleClickEvent(MotionEvent event, int currentX, int currentY) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = currentX;
                mDownY = currentY;
                break;
            case MotionEvent.ACTION_UP:
                int dx = currentX - mDownX;
                int dy = currentY - mDownY;
                // 判断为拖动的最小距离
                int touchSlop = mDragHelper.getTouchSlop();
                // 如果小于拖动的最小距离，说明是点击事件
                if ((Math.pow(dx, 2) + Math.pow(dy, 2)) < Math.pow(touchSlop, 2) && mDragHelper.isViewUnder(mHeaderView, currentX, currentY)) {
                    if (mDragOffset == 1.0f || mDragOffset < 0.5) {
                        expand();
                    } else {
                        shrink();
                    }
                }
                break;
        }
    }

    /**
     * 移动到指定的位置
     *
     * @param slideRatio 0表示移动到顶部，1表示移动到底部
     */
    private void smoothSlideTo(float slideRatio) {
        final int topBound = getPaddingTop();
        int finalTop = (int) (topBound + slideRatio * mDragRange);
        if (mDragHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    //判断点击事件是否在子View上
    private boolean isInMySubView(View subView, int currentX, int currentY) {
        int[] subViewLocation = new int[2];
        subView.getLocationOnScreen(subViewLocation);
        int[] myLocation = new int[2];
        this.getLocationOnScreen(myLocation);
        int screenX = myLocation[0] + currentX;
        int screenY = myLocation[1] + currentY;
        return screenX >= subViewLocation[0] && screenX < subViewLocation[0] + subView.getWidth() && screenY >= subViewLocation[1] && screenY < subViewLocation[1] + subView.getHeight();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - mHeaderView.getHeight() - mHeaderView.getPaddingBottom();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            return newTop;
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mHeaderViewTop = top;
            mDragOffset = (float) mHeaderViewTop / mDragRange;
            mHeaderView.setPivotX(mHeaderView.getWidth());
            mHeaderView.setPivotY(mHeaderView.getHeight());
            mHeaderView.setScaleX(1 - mDragOffset / 2);
            mHeaderView.setScaleY(1 - mDragOffset / 2);
            mFooterView.setAlpha(1 - mDragOffset);
            requestLayout();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            Log.i("bingo", "xvel = " + xvel + "   yvel = " + yvel);
            int finalTop = getPaddingTop();
            if (xvel > 0 || yvel > 0 || (yvel == 0 && mDragOffset > 0.5f)) {
                finalTop += mDragRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);
//            invalidate();
        }

    }

}