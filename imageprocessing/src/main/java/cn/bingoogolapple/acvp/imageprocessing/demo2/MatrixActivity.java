package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.graphics.Matrix;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_matrix)
public class MatrixActivity extends BaseActivity {
    @BGAAView(R.id.imv_matrix_icon)
    private ImageMatrixView mIconImv;
    @BGAAView(R.id.gl_matrix_group)
    private GridLayout mGroupGl;

    private int mEtWidth;
    private int mEtHeight;

    private EditText[] mEts = new EditText[9];
    private float[] mImageMatrixs = new float[9];

    @Override
    protected void processLogic() {
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