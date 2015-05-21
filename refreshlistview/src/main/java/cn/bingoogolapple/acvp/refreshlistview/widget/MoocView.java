package cn.bingoogolapple.acvp.refreshlistview.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlistview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 10:43
 * 描述:
 */
public class MoocView extends View {
    private PorterDuffXfermode mXfermode;
    /**
     * 用来画临时图像的画笔
     */
    private Paint mPaint;
    /**
     * 用来画临时图像的画布
     */
    private Canvas mCanvas;
    /**
     * 原始的图片
     */
    private Bitmap mOriginalBitmap;
    /**
     * 原始的图片宽度
     */
    private int mOriginalBitmapWidth;
    /**
     * 原始的图片高度
     */
    private int mOriginalBitmapHeight;
    /**
     * 最终生成的图片
     */
    private Bitmap mUltimateBitmap;
    /**
     * 贝塞尔曲线路径
     */
    private Path mBezierPath;
    /**
     * 贝塞尔曲线控制点x
     */
    private float mBezierControlX;
    /**
     * 贝塞尔曲线控制点y
     */
    private float mBezierControlY;
    /**
     * 贝塞尔曲线原始的控制点y
     */
    private float mBezierControlOriginalY;
    /**
     * 当前波纹的y值
     */
    private float mWaveY;
    /**
     * 波纹原始的y值
     */
    private float mWaveOriginalY;
    /**
     * 贝塞尔曲线控制点x是否增加
     */
    private boolean mIsBezierControlXIncrease;
    /**
     * 是否正在刷新
     */
    private boolean mIsRefreshing = false;
    /**
     * 最终生成图片的填充颜色
     */
    private int mUltimateColor;


    public MoocView(Context context) {
        this(context, null);
    }

    public MoocView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MoocView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initAttrs(context, attrs);

        initPaint();
        initCanvas();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MoocView);

        BitmapDrawable originalBitmap = (BitmapDrawable) typedArray.getDrawable(R.styleable.MoocView_mv_originalImg);
        if (originalBitmap == null) {
            throw new RuntimeException(MoocView.class.getSimpleName() + "必须设置原始图片");
        }
        mOriginalBitmap = originalBitmap.getBitmap();

        mUltimateColor = typedArray.getColor(R.styleable.MoocView_mv_ultimateColor, Color.rgb(27, 128, 255));

        typedArray.recycle();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mUltimateColor);
    }

    private void initCanvas() {
        mOriginalBitmapWidth = mOriginalBitmap.getWidth();
        mOriginalBitmapHeight = mOriginalBitmap.getHeight();

        // 初始状态值
        mWaveOriginalY = 9 / 10F * mOriginalBitmapHeight;
        mWaveY = mWaveOriginalY;
        mBezierControlOriginalY = 11 / 10F * mOriginalBitmapHeight;
        mBezierControlY = mBezierControlOriginalY;

        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mBezierPath = new Path();

        mCanvas = new Canvas();
        mUltimateBitmap = Bitmap.createBitmap(mOriginalBitmapWidth, mOriginalBitmapHeight, Config.ARGB_8888);
        mCanvas.setBitmap(mUltimateBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawUltimateBitmap();
        // 将目标图绘制在当前画布上，起点为左边距，上边距的交点
        canvas.drawBitmap(mUltimateBitmap, getPaddingLeft(), getPaddingTop(), null);
        if (mIsRefreshing) {
            invalidate();
        }
    }

    private void drawUltimateBitmap() {
        mBezierPath.reset();
        mUltimateBitmap.eraseColor(Color.parseColor("#00ffffff"));

        if (mBezierControlX >= mOriginalBitmapWidth + 1 / 2 * mOriginalBitmapWidth) {
            mIsBezierControlXIncrease = false;
        } else if (mBezierControlX <= -1 / 2 * mOriginalBitmapWidth) {
            mIsBezierControlXIncrease = true;
        }

        mBezierControlX = mIsBezierControlXIncrease ? mBezierControlX + 10 : mBezierControlX - 10;
        if (mBezierControlY >= 0) {
            mBezierControlY -= 1;
            mWaveY -= 1;
        } else {
            mWaveY = mWaveOriginalY;
            mBezierControlY = mBezierControlOriginalY;
        }

        // 贝塞尔曲线的生成
        mBezierPath.moveTo(0, mWaveY);
        // 两个控制点通过controlX，controlY生成
        mBezierPath.cubicTo(mBezierControlX / 2, mWaveY - (mBezierControlY - mWaveY), (mBezierControlX + mOriginalBitmapWidth) / 2, mBezierControlY, mOriginalBitmapWidth, mWaveY);
        // 与下下边界闭合
        mBezierPath.lineTo(mOriginalBitmapWidth, mOriginalBitmapHeight);
        mBezierPath.lineTo(0, mOriginalBitmapHeight);
        // 进行闭合
        mBezierPath.close();

        mCanvas.drawBitmap(mOriginalBitmap, 0, 0, mPaint);
        mPaint.setXfermode(mXfermode);
        mCanvas.drawPath(mBezierPath, mPaint);
        mPaint.setXfermode(null);
    }

    public boolean isRefreshing() {
        return mIsRefreshing;
    }

    public void startRefreshing() {
        mIsRefreshing = true;
        reset();
    }

    public void stopRefreshing() {
        mIsRefreshing = false;
        reset();
    }

    private void reset() {
        mWaveY = mWaveOriginalY;
        mBezierControlY = mBezierControlOriginalY;
        mBezierControlX = 0;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize + getPaddingLeft() + getPaddingRight();
        } else {
            width = mOriginalBitmapWidth + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize + getPaddingTop() + getPaddingBottom();
        } else {
            height = this.mOriginalBitmapHeight + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);
    }

}