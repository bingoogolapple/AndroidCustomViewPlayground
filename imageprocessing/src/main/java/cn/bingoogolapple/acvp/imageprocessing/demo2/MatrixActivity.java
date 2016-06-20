package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bacvp.BaseActivity;


public class MatrixActivity extends BaseActivity {
    private ImageMatrixView mIconImv;
    private GridLayout mGroupGl;

    private int mEtWidth;
    private int mEtHeight;

    private EditText[] mEts = new EditText[9];
    private float[] mImageMatrixs = new float[9];

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_matrix);
        mIconImv = getViewById(R.id.imv_matrix_icon);
        mGroupGl = getViewById(R.id.gl_matrix_group);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mGroupGl.post(new Runnable() {
            @Override
            public void run() {
                // 在控件绘制完毕后获取宽高
                mEtWidth = mGroupGl.getWidth() / 3;
                mEtHeight = mGroupGl.getHeight() / 3;
                addEts();
                initMatrix();
            }
        });
    }

    private void addEts() {
        for (int i = 0; i < 9; i++) {
            EditText editText = new EditText(this);
            mEts[i] = editText;
            mGroupGl.addView(editText, mEtWidth, mEtHeight);
        }
    }

    /**
     * img1 0 0
     * 0 img1 0
     * 0 0 img1
     * <p/>
     * X
     * Y
     * img1
     */
    private void initMatrix() {
        for (int i = 0; i < 9; i++) {
            if (i % 4 == 0) {
                mEts[i].setText(String.valueOf(1));
            } else {
                mEts[i].setText(String.valueOf(0));
            }
        }
    }

    private void getMatrix() {
        for (int i = 0; i < 9; i++) {
            mImageMatrixs[i] = Float.valueOf(mEts[i].getText().toString().trim());
        }
    }

    private void setImageMatrix() {
        Matrix matrix = new Matrix();
//        matrix.setValues(mImageMatrixs);

//        matrix.setTranslate(100,100);

        matrix.setScale(2, 2);
        // 要想顺序显示，使用post方法
        matrix.postTranslate(100, 100);

        mIconImv.setImageMatrix(matrix);
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