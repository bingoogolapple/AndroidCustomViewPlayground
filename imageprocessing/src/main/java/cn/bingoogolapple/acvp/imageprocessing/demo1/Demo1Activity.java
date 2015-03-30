package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.content.Intent;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_demo1)
public class Demo1Activity extends BaseActivity {

    public void img3(View view) {
        startActivity(new Intent(this, PrimaryColorActivity.class));
    }

    public void colorMatrix(View view) {
        startActivity(new Intent(this, ColorMatrixActivity.class));
    }

    public void pixel(View view) {
        startActivity(new Intent(this, PixelActivity.class));
    }

}