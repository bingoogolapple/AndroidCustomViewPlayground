package cn.bingoogolapple.acvp.refreshlayout.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlayout.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 22:34
 * 描述:
 */
public class StickinessRefreshView extends View {
    private static final String TAG = StickinessRefreshView.class.getSimpleName();

    private RectF mTopBound;
    private RectF mBottomBound;
    private Rect mRotateDrawableBound;
    private Point mCenterPoint;

    private Paint mPaint;
    private Path mPath;

    private Drawable mRotateDrawable;
    /**
     * 旋转图片的大小
     */
    private int mRotateDrawableSize;

    private int mMaxBottomHeight;
    private int mCurrentBottomHeight = 0;

    /**
     * 是否正在旋转
     */
    private boolean mIsRotating = false;
    private boolean mIsRefreshing = false;
    /**
     * 当前旋转角度
     */
    private int mCurrentDegree = 0;

    private int mEdge = 0;
    private int mTopSize = 0;

    private int mMoveYDistance;

    public StickinessRefreshView(Context context) {
        this(context, null);
    }

    public StickinessRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StickinessRefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initBounds();
        initPaint();
        initRotateDrawable();
        initSize();
    }

    private void initBounds() {
        mTopBound = new RectF();
        mBottomBound = new RectF();
        mRotateDrawableBound = new Rect();
        mCenterPoint = new Point();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(0xFF999999);
        mPath = new Path();
    }

    private void initRotateDrawable() {
        mRotateDrawable = getContext().getResources().getDrawable(R.mipmap.icon_pullwidget);
    }

    private void initSize() {
        mEdge = dp2px(getContext(), 5);
        mRotateDrawableSize = dp2px(getContext(), 30);
        mTopSize = mRotateDrawableSize + 2 * mEdge;

        mMaxBottomHeight = 2 * mRotateDrawableSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = mTopSize + getPaddingLeft() + getPaddingRight();
        int height = mTopSize + getPaddingTop() + getPaddingBottom() + mMoveYDistance;

        setMeasuredDimension(width, height);
        measureDraw();
    }

    private void measureDraw() {
        mCenterPoint.x = getMeasuredWidth() / 2;
        mCenterPoint.y = getMeasuredHeight() / 2;

        mTopBound.left = mCenterPoint.x - mTopSize / 2;
        if (mCurrentBottomHeight == 0) {
            mTopBound.top = getMeasuredHeight() - getBottomPaddingAndTopSize();
        } else {
            mTopBound.top = getPaddingTop();
        }
        mTopBound.right = mTopBound.left + mTopSize;
        mTopBound.bottom = mTopBound.top + mTopSize;

        float scale = 1.0f - mCurrentBottomHeight * 1.0f / mMaxBottomHeight;
        scale = Math.min(Math.max(scale, 0.3f), 1.0f);
        int mBottomSize = (int) (mTopSize * scale);

        mBottomBound.left = mCenterPoint.x - mBottomSize / 2;
        mBottomBound.right = mBottomBound.left + mBottomSize;
        mBottomBound.bottom = mTopBound.bottom + mCurrentBottomHeight;
        mBottomBound.top = mBottomBound.bottom - mBottomSize;
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        mTopBound.round(mRotateDrawableBound);
        mRotateDrawable.setBounds(mRotateDrawableBound);
        if (mIsRotating) {
            mPath.addOval(mTopBound, Path.Direction.CW);
            canvas.drawPath(mPath, mPaint);
            canvas.save();
            canvas.rotate(mCurrentDegree, mRotateDrawable.getBounds().centerX(), mRotateDrawable.getBounds().centerY());
            mRotateDrawable.draw(canvas);
            canvas.restore();
        } else {
            // 移动到drawable左边缘的中间那个点
            mPath.moveTo(mTopBound.left, mTopBound.bottom - mTopSize / 2);
            // 从drawable左边缘的中间那个点开始画半圆
            mPath.arcTo(mTopBound, 180, 180);
            // 二阶贝塞尔曲线，第一个是控制点，第二个是终点
//            mPath.quadTo(mTopBound.right - mTopSize / 8, mTopBound.bottom, mBottomBound.right, mBottomBound.bottom - mBottomBound.height() / 2);

            // mCurrentBottomHeight   0 ==> mMaxBottomHeight
            // scale 0 ==> 1
            float scale = mCurrentBottomHeight * 1.0f / mMaxBottomHeight;
            // scale 0.2 ==> 1
            scale = Math.max(scale, 0.2f);

            float bottomControlXOffset = mTopSize * ((4 + scale * scale * 15) / 32);
            float bottomControlY = mTopBound.bottom / 2 + mCenterPoint.y / 15;
            // 三阶贝塞尔曲线，前两个是控制点，最后一个点是终点
            mPath.cubicTo(mTopBound.right - mTopSize / 8, mTopBound.bottom, mTopBound.right - bottomControlXOffset, bottomControlY, mBottomBound.right, mBottomBound.bottom - mBottomBound.height() / 2);

            mPath.arcTo(mBottomBound, 0, 180);

//            mPath.quadTo(mTopBound.left + mTopSize / 8, mTopBound.bottom, mTopBound.left, mTopBound.bottom - mTopSize / 2);
            mPath.cubicTo(mTopBound.left + bottomControlXOffset, bottomControlY, mTopBound.left + mTopSize / 8, mTopBound.bottom, mTopBound.left, mTopBound.bottom - mTopSize / 2);

            canvas.drawPath(mPath, mPaint);

            mRotateDrawable.draw(canvas);
        }
    }

    public int getTotalHeight() {
        return mTopSize + getPaddingTop() + getPaddingBottom() + mMaxBottomHeight;
    }

    public int getBottomPaddingAndTopSize() {
        return mTopSize + getPaddingBottom();
    }

    public void setMoveYDistance(int moveYDistance) {
        int bottomHeight = moveYDistance - getBottomPaddingAndTopSize() - getPaddingTop();
        if (bottomHeight > 0) {
            mCurrentBottomHeight = bottomHeight;
        } else {
            mCurrentBottomHeight = 0;
        }
        mMoveYDistance = Math.min(moveYDistance, mMaxBottomHeight);
        Log.i(TAG, "mMoveYDistance = " + mMoveYDistance);
        requestLayout();
    }

    /**
     * 是否能切换到正在刷新状态
     *
     * @return
     */
    public boolean canChangeToRefreshing() {
        return mCurrentBottomHeight >= mMaxBottomHeight * 0.98f;
    }

    public void startRefreshing() {
        mIsRefreshing = true;
        smoothToRefreshing();
    }

    private void smoothToRefreshing() {
        ViewCompat.postOnAnimation(this, new Runnable() {
            @Override
            public void run() {
                mCurrentBottomHeight -= 8;
                mMoveYDistance -=8;
                if (mCurrentBottomHeight > 0) {
                    requestLayout();
                    smoothToRefreshing();
                } else {
                    mIsRotating = true;
                    mCurrentBottomHeight = 0;
                    mMoveYDistance = 0;
                    mCurrentDegree += 10;
                    if (mCurrentDegree > 360) {
                        mCurrentDegree = 0;
                    }
                    if (mIsRefreshing) {
                        smoothToRefreshing();
                    }
                }
                invalidate();
            }
        });
    }

    public void stopRefresh() {
        mIsRotating = true;
        mIsRefreshing = false;
        postInvalidate();
    }

    public void smoothToIdle() {
        ViewCompat.postOnAnimation(this, new Runnable() {
            @Override
            public void run() {
                mCurrentBottomHeight -= 8;
                mMoveYDistance -=8;
                if (mMoveYDistance > 0) {
                    smoothToIdle();
                } else {
                    mCurrentBottomHeight = 0;
                    mMoveYDistance = 0;
                    mIsRotating = false;
                }
                invalidate();
            }
        });
    }

    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}