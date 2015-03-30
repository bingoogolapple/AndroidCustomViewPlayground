package cn.bingoogolapple.acvp.imageprocessing.demo3;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

public class ZoomImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {
    private boolean mIsOnce = false;
    /**
     * 初始化是缩放的值
     */
    private float mInitScale;
    /**
     * 双击放大到达的值
     */
    private float mMidScale;
    /**
     * 放大的最大值
     */
    private float mMaxScale;
    private Matrix mMatrix;
    private ScaleGestureDetector mScaleGestureDetector;

    //----------------------自由移动-----------------
    private int mLastPointerCount;
    private float mLastX;
    private float mLastY;
    private int mTouchSlop;
    private boolean mIsCanDrag;
    private boolean mIsCheckLeftAndRight;
    private boolean mIsCheckTopAndBottom;
    //-----------------------双击放大与缩小----------------
    private GestureDetector mGestureDetector;
    private boolean mIsAutoScale;

    public ZoomImageView(Context context) {
        this(context, null);
    }

    public ZoomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setOnTouchListener(this);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (mIsAutoScale) {
                    return true;
                }
                float x = e.getX();
                float y = e.getY();
                if (getScale() < mMidScale) {
//                    mMatrix.postScale(mMidScale / getScale(), mMidScale / getScale(), x, y);
//                    setImageMatrix(mMatrix);
                    postDelayed(new AutoScaleRunnable(mMidScale, x, y), 20);
                } else {
//                    mMatrix.postScale(mInitScale / getScale(), mInitScale / getScale(), x, y);
//                    setImageMatrix(mMatrix);
                    postDelayed(new AutoScaleRunnable(mInitScale, x, y), 20);
                }
                mIsAutoScale = true;
                return true;
            }
        });
    }

    private class AutoScaleRunnable implements Runnable {
        private float mTargetScale;
        private float mX;
        private float mY;
        private final float BIGGER = 1.07f;
        private final float SMALL = 0.93f;
        private float tempScale;

        public AutoScaleRunnable(float targetScale, float x, float y) {
            mTargetScale = targetScale;
            mX = x;
            mY = y;
            if (getScale() < mTargetScale) {
                tempScale = BIGGER;
            }
            if (getScale() > mTargetScale) {
                tempScale = SMALL;
            }
        }

        @Override
        public void run() {
            mMatrix.postScale(tempScale, tempScale, mX, mY);
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);

            float currentScale = getScale();
            if ((tempScale > 1.0f && currentScale < mTargetScale) || (tempScale < 1.0f && currentScale > mTargetScale)) {
                postDelayed(this, 20);
            } else {
                float scale = mTargetScale / currentScale;
                mMatrix.postScale(scale, scale, mX, mY);
                checkBorderAndCenterWhenScale();
                setImageMatrix(mMatrix);

                mIsAutoScale = false;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        // 为了兼容低版本，不使用该方法
        // getViewTreeObserver().removeOnGlobalLayoutListener(this);
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onGlobalLayout() {
        if (!mIsOnce) {
            int width = getWidth();
            int height = getHeight();

            Drawable drawable = getDrawable();
            if (drawable == null) {
                return;
            }
            int drawableWidth = drawable.getIntrinsicWidth();
            int drawableHeight = drawable.getIntrinsicHeight();

            mInitScale = 1.0f;
            if (drawableWidth > width && drawableHeight < height) {
                mInitScale = width * 1.0f / drawableWidth;
            } else if (drawableHeight > height && drawableWidth < width) {
                mInitScale = height * 1.0f / drawableHeight;
            } else {
                mInitScale = Math.min(width * 1.0f / drawableWidth, height * 1.0f / drawableHeight);
            }

            mMaxScale = mInitScale * 4;
            mMidScale = mInitScale * 2;

            // 将图片移动至控件的中心
            int dx = width / 2 - drawableWidth / 2;
            int dy = height / 2 - drawableHeight / 2;

            mMatrix.postTranslate(dx, dy);
            mMatrix.postScale(mInitScale, mInitScale, width / 2, height / 2);
            setImageMatrix(mMatrix);

            mIsOnce = true;
        }
    }

    /**
     * 获取当前图片的缩放值
     *
     * @return
     */
    public float getScale() {
        float[] values = new float[9];
        mMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        // 缩放的区间mInitScale mMaxScale
        float scale = getScale();
        float scaleFactor = detector.getScaleFactor();
        if (getDrawable() == null) {
            return true;
        }

        // 缩放范围的控制
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }
            mMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
            checkBorderAndCenterWhenScale();
            setImageMatrix(mMatrix);
        }
        return true;
    }

    /**
     * 在缩放的时候进行边界控制以及位置的控制
     */
    private void checkBorderAndCenterWhenScale() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        int width = getWidth();
        int height = getHeight();
        // 缩放时进行边界检测，防止出现白边
        if (rectF.width() >= width) {
            if (rectF.left > 0) {
                deltaX = -rectF.left;
            }
            if (rectF.right < width) {
                deltaX = width - rectF.right;
            }
        }
        if (rectF.height() >= height) {
            if (rectF.top > 0) {
                deltaY = -rectF.top;
            }
            if (rectF.bottom < width) {
                deltaY = height - rectF.bottom;
            }
        }

        // 如果宽度小于控件的宽度或者高度小于控件的高度，则让其居中
        if (rectF.width() < width) {
            deltaX = width / 2.0f - rectF.right + rectF.width() / 2.0f;
        }
        if (rectF.height() < height) {
            deltaY = height / 2.0f - rectF.bottom + rectF.height() / 2.0f;
        }

        mMatrix.postTranslate(deltaX, deltaY);
    }

    /**
     * 得到放大缩小后的宽和高，以及l,t,r,b
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mMatrix;
        RectF rectF = new RectF();
        Drawable drawable = getDrawable();
        if (drawable != null) {
            rectF.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        // 这里必须返回true
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    private void handleIntercept() {
        RectF rectF = getMatrixRectF();
        if (rectF.width() > getWidth() + 0.01f || rectF.height() > getHeight() + 0.01f) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // 双击的时候，不允许移动
        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        mScaleGestureDetector.onTouchEvent(event);

        float x = 0;
        float y = 0;
        int pointerCount = event.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x /= pointerCount;
        y /= pointerCount;
        if (mLastPointerCount != pointerCount) {
            mIsCanDrag = false;
            mLastX = x;
            mLastY = y;
        }
        mLastPointerCount = pointerCount;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                handleIntercept();
                break;
            case MotionEvent.ACTION_MOVE:
                handleIntercept();
                float dx = x - mLastX;
                float dy = y - mLastY;
                if (!mIsCanDrag) {
                    mIsCanDrag = isMoveAction(dx, dy);
                }
                if (mIsCanDrag) {
                    RectF rectF = getMatrixRectF();
                    if (getDrawable() != null) {
                        mIsCheckLeftAndRight = mIsCheckTopAndBottom = true;
                        // 如果宽度小于控件宽度，不允许横向移动
                        if (rectF.width() < getWidth()) {
                            mIsCheckLeftAndRight = false;
                            dx = 0;
                        }
                        if (rectF.height() < getHeight()) {
                            mIsCheckTopAndBottom = false;
                            dy = 0;
                        }
                        mMatrix.postTranslate(dx, dy);
                        checkBorderWhenTranslate();
                        setImageMatrix(mMatrix);
                    }
                }

                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount = 0;
                break;
        }


        return true;
    }

    private void checkBorderWhenTranslate() {
        RectF rectF = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;
        int width = getWidth();
        int height = getHeight();
        if (rectF.top > 0 && mIsCheckTopAndBottom) {
            deltaY = -rectF.top;
        }
        if (rectF.bottom < height && mIsCheckTopAndBottom) {
            deltaY = height - rectF.bottom;
        }

        if (rectF.left > 0 && mIsCheckLeftAndRight) {
            deltaX = -rectF.left;
        }
        if (rectF.right < width && mIsCheckLeftAndRight) {
            deltaX = width - rectF.right;
        }
        mMatrix.postTranslate(deltaX, deltaY);
    }

    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx * dx + dy * dy) > mTouchSlop;
    }
}