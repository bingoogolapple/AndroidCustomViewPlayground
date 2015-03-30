package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.R;

public class XFermodeView extends View {
    private Bitmap mBitmap;
    private Bitmap mOut;
    private Paint mPaint;

    public XFermodeView(Context context) {
        this(context, null);
    }

    public XFermodeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XFermodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        // 禁用硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        // 先画的是dst，后画的是src
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        mOut = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mOut);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 画dst
        canvas.drawRoundRect(new RectF(0, 0, mBitmap.getWidth(), mBitmap.getHeight()), 50, 50, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 画src
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
        mPaint.setXfermode(null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(mOut, 0, 0, null);
    }
}
