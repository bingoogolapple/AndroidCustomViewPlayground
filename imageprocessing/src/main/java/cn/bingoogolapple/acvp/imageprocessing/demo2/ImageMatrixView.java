package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.R;

public class ImageMatrixView extends View {
    private Bitmap mBitmap;
    private Matrix mMatrix;

    public ImageMatrixView(Context context) {
        this(context, null);
    }

    public ImageMatrixView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageMatrixView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        setImageMatrix(new Matrix());
    }

    public void setImageMatrix(Matrix matrix) {
        mMatrix = matrix;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        canvas.drawBitmap(mBitmap, mMatrix, null);
    }
}