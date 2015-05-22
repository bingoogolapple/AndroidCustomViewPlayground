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
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import cn.bingoogolapple.acvp.refreshlayout.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 22:34
 * 描述:
 */
public class StickinessRefreshView extends View {
    private static final String TAG = StickinessRefreshView.class.getSimpleName();

    private int mCurrentWidth, mCurrentHeight;
    private RectF mTopBound;
    private RectF mBottomBound;
    private Rect mRotateDrawableBound;
    private Point mCenterPoint;

    private Paint mPaint;
    private Path mPath;

    private Drawable mRotateDrawable;

    private int mMinHeight;
    private int mMaxHeight;
    private int mMaxBottomHeight;
    private int mCurrentBottomHeight;

    /**
     * 是否正在旋转
     */
    private boolean mIsRotating = false;
    private boolean mIsRefreshing = false;
    /**
     * 当前旋转角度
     */
    private int mCurrentDegree = 0;

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

        mRotateDrawable = context.getResources().getDrawable(R.mipmap.icon_pullwidget);

        mMinHeight = context.getResources().getDimensionPixelOffset(R.dimen.minimum_height);
        mMaxBottomHeight = context.getResources().getDimensionPixelOffset(R.dimen.minimum_content_height);
        mMaxHeight = 3 * mMinHeight;
        mCurrentBottomHeight = mMaxBottomHeight;
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

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (heightMeasureSpec < mMinHeight) {
            mCurrentHeight = mMinHeight;
            setMeasuredDimension(mCurrentWidth, mCurrentHeight);
        }

        if (widthMeasureSpec < mMinHeight) {
            mCurrentWidth = mMinHeight;
            setMeasuredDimension(mCurrentWidth, mCurrentHeight);
        }

        mCurrentWidth = getMeasuredWidth();
        mCurrentHeight = getMeasuredHeight();

        measureDraw(mCurrentWidth, mCurrentHeight);
    }

    private void measureDraw(int width, int height) {
        mCenterPoint.x = width / 2;
        mCenterPoint.y = height / 2;

        mTopBound.left = mCenterPoint.x - mMaxBottomHeight / 2;
        mTopBound.top = (mMinHeight - mMaxBottomHeight) / 2;
        mTopBound.right = mTopBound.left + mMaxBottomHeight;
        mTopBound.bottom = mTopBound.top + mMaxBottomHeight;

        float scale = 1.0f - mCurrentBottomHeight * 1.0f / mMaxBottomHeight;
        int temp = (int) (Math.min(Math.max(scale, 0.5f), 1.0f) * mMaxBottomHeight);
        mBottomBound.left = mCenterPoint.x - temp / 2;
        mBottomBound.top = height - temp - (mMinHeight - temp) / 2;
        mBottomBound.right = mBottomBound.left + temp;
        mBottomBound.bottom = mBottomBound.top + (int)(mCurrentBottomHeight * 0.9f);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPath.reset();

        mTopBound.round(mRotateDrawableBound);
        mRotateDrawable.setBounds(mRotateDrawableBound);
        if (mIsRotating) {
            mPath.addOval(mTopBound, Path.Direction.CCW);
            canvas.drawPath(mPath, mPaint);

            canvas.save();
            canvas.rotate(mCurrentDegree, mRotateDrawable.getBounds().centerX(), mRotateDrawable.getBounds().centerY());
            mRotateDrawable.draw(canvas);
            canvas.restore();
        } else {
            // 移动到drawable左边缘的中间那个点
            mPath.moveTo(mTopBound.left, mMinHeight / 2);
            // 从drawable左边缘的中间那个点开始画半圆
            mPath.arcTo(mTopBound, 180, 180);
            mPath.quadTo(mBottomBound.right, mCenterPoint.y, mBottomBound.right, mCurrentHeight - mMinHeight / 2);
            mPath.arcTo(mBottomBound, 0, 180);
            mPath.quadTo(mBottomBound.left, mCenterPoint.y, mTopBound.left, mMinHeight / 2);
            canvas.drawPath(mPath, mPaint);

            mRotateDrawable.draw(canvas);
        }
    }

    public void setScale(float scale) {
        mCurrentBottomHeight = (int) (mMaxBottomHeight * scale);
        ViewCompat.postOnAnimation(this, new Runnable() {
            @Override
            public void run() {
                setCurrentHeight();
                postInvalidate();
            }
        });
    }

    private void setCurrentHeight() {
        mCurrentHeight = mCurrentBottomHeight + mMinHeight;
        mCurrentHeight = Math.max(Math.min(mCurrentHeight, mMaxHeight), mMinHeight);

        LayoutParams param = getLayoutParams();
        param.height = mCurrentHeight;
        setLayoutParams(param);
    }

    /**
     * 是否能切换到正在刷新状态
     *
     * @return
     */
    public boolean canChangeToRefreshing() {
        return mCurrentHeight >= mMaxHeight;
    }

    public void startRefreshing() {
        mIsRefreshing = true;
        startRefreshing2();
    }

    private void startRefreshing2() {
        ViewCompat.postOnAnimation(this, new Runnable() {
            @Override
            public void run() {
                mCurrentBottomHeight -= 8;
                if (mCurrentBottomHeight > 0) {
                    startRefreshing2();
                } else {
                    mIsRotating = true;
                    mCurrentBottomHeight = 0;
                    mCurrentDegree += 10;
                    if (mCurrentDegree > 360) {
                        mCurrentDegree = 0;
                    }
                    if (mIsRefreshing) {
//                        Log.i(TAG, "刷新旋转 " + mCurrentDegree);
                        startRefreshing2();
                    }
                }
                setCurrentHeight();
                postInvalidate();
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
                if (mCurrentBottomHeight > 0) {
                    smoothToIdle();

                    Log.i(TAG, "回到初始状态");
                } else {
                    mCurrentBottomHeight = 0;
                    mIsRotating = false;
                }
                setCurrentHeight();
                postInvalidate();
            }
        });
    }

}