package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.R;

public class MeshView extends View {
    private final int WIDTH = 200;
    private final int HEIGHT = 200;
    private int COUNT = (WIDTH + 1) * (HEIGHT + 1);
    private float[] verts = new float[COUNT * 2];
    private float[] origs = new float[COUNT * 2];
    private Bitmap mBitmap;
    private float K = 1;

    public MeshView(Context context) {
        this(context, null);
    }

    public MeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.primarycolor);
        float bmWidth = mBitmap.getWidth();
        float bmHeight = mBitmap.getHeight();
        int index = 0;
        for (int i = 0; i < HEIGHT + 1; i++) {
            float fy = bmHeight * i / HEIGHT;
            for (int j = 0; j < WIDTH + 1; j++) {
                float fx = bmWidth * j / WIDTH;
                origs[index * 2 + 0] = verts[index * 2 + 0] = fx;
                origs[index * 2 + 1] = verts[index * 2 + 1] = fy + 100;
                index++;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < HEIGHT + 1; i++) {
            for (int j = 0; j < WIDTH + 1; j++) {
                verts[(i * (WIDTH + 1) + j) * 2 + 0] += 0;
                float offsetY = (float) Math.sin((float) j / WIDTH * 2 * Math.PI + K * 2 * Math.PI);
                verts[(i * (WIDTH + 1) + j) * 2 + 1] = origs[(i * (WIDTH + 1) + j) * 2 + 1] + offsetY * 40;
            }
        }
        K += 0.02f;
        canvas.drawBitmapMesh(mBitmap, WIDTH, HEIGHT, verts, 0, null, 0, null);
        invalidate();
    }
}