package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bacvp.BaseActivity;

public class Demo1Activity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo1);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

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