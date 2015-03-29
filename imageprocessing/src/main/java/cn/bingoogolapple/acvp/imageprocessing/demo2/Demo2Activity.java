package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.content.Intent;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_demo2)
public class Demo2Activity extends BaseActivity {

    public void matrix(View view) {
        startActivity(new Intent(this, MatrixActivity.class));
    }

    public void xFermode(View view) {
        startActivity(new Intent(this, XFermodeActivity.class));
    }

    public void shader(View view) {
        startActivity(new Intent(this, ShaderActivity.class));
    }

}