package cn.bingoogolapple.acvp.imageprocessing.demo2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bacvp.BaseActivity;


public class Demo2Activity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo2);
    }

    public void matrix(View view) {
        startActivity(new Intent(this, MatrixActivity.class));
    }

    public void xFermode(View view) {
        startActivity(new Intent(this, XFermodeActivity.class));
    }

    public void shader(View view) {
        startActivity(new Intent(this, ShaderActivity.class));
    }

    public void mesh(View view) {
        startActivity(new Intent(this, MeshActivity.class));
    }

}