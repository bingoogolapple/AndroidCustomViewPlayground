package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_colormatrix)
public class ColorMatrixActivity extends BaseActivity {
    @BGAAView(R.id.iv_colormatrix_icon)
    private ImageView mIconIv;
    @BGAAView(R.id.gl_colormatrix_group)
    private GridLayout mGroupGl;


    private Bitmap mBitmap;
    private int mEtWidth;
    private int mEtHeight;

    private EditText[] mEts = new EditText[20];
    private float[] mColorMatrixs = new float[20];

    @Override
    protected void processLogic() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        mIconIv.setImageBitmap(mBitmap);
        mGroupGl.post(new Runnable() {
            @Override
            public void run() {
                // 在控件绘制完毕后获取宽高
                mEtWidth = mGroupGl.getWidth() / 5;
                mEtHeight = mGroupGl.getHeight() / 4;
                addEts();
                initMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i < 20; i++) {
            EditText editText = new EditText(this);
            mEts[i] = editText;
            mGroupGl.addView(editText, mEtWidth, mEtHeight);
        }
    }

    /**
     * 通常用于初始化颜色矩阵
     *
     * img1 0 0 0 0
     * 0 img1 0 0 0
     * 0 0 img1 0 0
     * 0 0 0 img1 0
     *
     * 颜色矩阵分量
     * R
     * G
     * B
     * A
     * img1
     */
    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }

    private void getMatrix() {
        for (int i = 0; i < 20; i++) {
            mColorMatrixs[i] = Float.valueOf(mEts[i].getText().toString().trim());
        }
    }

    private void setImageMatrix() {
        Bitmap bmp = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        ColorMatrix colorMatrix = new ColorMatrix(mColorMatrixs);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(mBitmap, 0, 0, paint);

        mIconIv.setImageBitmap(bmp);
    }

    public void change(View view) {
        getMatrix();
        setImageMatrix();
    }

    public void reset(View view) {
        initMatrix();
        getMatrix();
        setImageMatrix();
    }

}