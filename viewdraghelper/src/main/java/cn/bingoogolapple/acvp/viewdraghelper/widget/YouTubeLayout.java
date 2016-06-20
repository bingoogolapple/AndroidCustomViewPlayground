package cn.bingoogolapple.acvp.viewdraghelper.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

public class YouTubeLayout extends ViewGroup {
    private final ViewDragHelper mDragHelper;
    private View mHeaderView;
    private View mFooterView;
    private int mDragRange;
    private int mHeaderViewTop;

    private float mDragOffset;

    private ScaleCallback mScaleCallback;

    public YouTubeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YouTubeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new RuntimeException(YouTubeLayout.class.getSimpleName() + "必须有且只有两个子控件");
        }
        mHeaderView = getChildAt(0);
        mFooterView = getChildAt(1);
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
        mDragRange = getHeight() - mHeaderView.getHeight();
        mHeaderView.layout(0, mHeaderViewTop, r, mHeaderViewTop + mHeaderView.getMeasuredHeight());
        mFooterView.layout(0, mHeaderViewTop + mHeaderView.getMeasuredHeight(), r, mHeaderViewTop + b);
    }

    @Override
    public void computeScroll() {
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

    /**
     * 移动到指定的位置
     *
     * @param slideRatio 0表示移动到顶部，1表示移动到底部
     */
    private void smoothSlideTo(float slideRatio) {
        int topBound = getPaddingTop();
        int finalTop = (int) (topBound + slideRatio * mDragRange);
        if (mDragHelper.smoothSlideViewTo(mHeaderView, mHeaderView.getLeft(), finalTop)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mHeaderView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            int bottomBound = getHeight() - mHeaderView.getHeight();
            return Math.min(Math.max(top, getPaddingTop()), bottomBound);
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return mDragRange;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            mHeaderViewTop = top;
            /**
             * 滑动过程
             *
             * 1.收缩过程
             * mHeaderViewTop                                         0 --> mDragRange
             * mDragOffset = 1.0f * mHeaderViewTop / mDragRange       0 --> 1.0
             * scale = 1 - 0.4f * mDragOffset                         1 --> 0.6
             */

            mDragOffset = 1.0f * mHeaderViewTop / mDragRange;

            float scale = 1 - 0.4f * mDragOffset;

            animWithNineOld(scale);
//            animWithViewCompat(scale);

            if (mScaleCallback != null) {
                mScaleCallback.onScale(mDragOffset);
            }
            requestLayout();
        }

        private void animWithViewCompat(float scale) {
            ViewCompat.setPivotX(YouTubeLayout.this, YouTubeLayout.this.getWidth());
            ViewCompat.setPivotY(YouTubeLayout.this, YouTubeLayout.this.getHeight());

            ViewCompat.setPivotX(mHeaderView, mHeaderView.getWidth());
            // 乘以0.9，使缩放结束时底部留一些间隙
            ViewCompat.setPivotY(mHeaderView, mHeaderView.getHeight() * 0.9f);

            if (mHeaderViewTop == mDragRange) {
                // 收缩完毕时，缩放YouTubeLayout的宽度，还原mHeaderView的宽度
                ViewCompat.setScaleX(YouTubeLayout.this, scale);
                ViewCompat.setScaleX(mHeaderView, 1.0f);
            } else {
                // 滑动过程中，还原YouTubeLayout的宽度，缩放mHeaderView的宽度
                ViewCompat.setScaleX(YouTubeLayout.this, 1.0f);
                ViewCompat.setScaleX(mHeaderView, scale);
            }
            // 滑动过程和滑动完毕时，都要缩放mHeaderView的高度
            ViewCompat.setScaleY(mHeaderView, scale);

            ViewCompat.setAlpha(mFooterView, 1 - mDragOffset);
        }

        private void animWithNineOld(float scale) {
            ViewHelper.setPivotX(YouTubeLayout.this, YouTubeLayout.this.getWidth());
            ViewHelper.setPivotY(YouTubeLayout.this, YouTubeLayout.this.getHeight());

            ViewHelper.setPivotX(mHeaderView, mHeaderView.getWidth());
            // 乘以0.9，使缩放结束时底部留一些间隙
            ViewHelper.setPivotY(mHeaderView, mHeaderView.getHeight() * 0.9f);

            if (mHeaderViewTop == mDragRange) {
                // 收缩完毕时，缩放YouTubeLayout的宽度，还原mHeaderView的宽度
                ViewHelper.setScaleX(YouTubeLayout.this, scale);
                ViewHelper.setScaleX(mHeaderView, 1.0f);
            } else {
                // 滑动过程中，还原YouTubeLayout的宽度，缩放mHeaderView的宽度
                ViewHelper.setScaleX(YouTubeLayout.this, 1.0f);
                ViewHelper.setScaleX(mHeaderView, scale);
            }
            // 滑动过程和滑动完毕时，都要缩放mHeaderView的高度
            ViewHelper.setScaleY(mHeaderView, scale);

            ViewHelper.setAlpha(mFooterView, 1 - mDragOffset);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int finalTop = getPaddingTop();
            if ((yvel > 0 && yvel > xvel) || (yvel == 0 && mDragOffset > 0.5f)) {
                finalTop += mDragRange;
            }
            mDragHelper.settleCapturedViewAt(releasedChild.getLeft(), finalTop);

            // 要执行下面的代码，不然不会自动收缩完毕或展开完毕
            ViewCompat.postInvalidateOnAnimation(YouTubeLayout.this);
        }

    }

    public void setScaleCallback(ScaleCallback scaleCallback) {
        mScaleCallback = scaleCallback;
    }

    public interface ScaleCallback {
        public void onScale(float scale);
    }

}