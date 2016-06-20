package cn.bingoogolapple.acvp.imageprocessing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.imageprocessing.demo1.Demo1Activity;
import cn.bingoogolapple.acvp.imageprocessing.demo2.Demo2Activity;
import cn.bingoogolapple.acvp.imageprocessing.demo3.Demo3Activity;
import cn.bingoogolapple.bacvp.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    public void demo1(View view) {
        startActivity(new Intent(this, Demo1Activity.class));
    }

    public void demo2(View view) {
        startActivity(new Intent(this, Demo2Activity.class));
    }

    public void demo3(View view) {
        startActivity(new Intent(this, Demo3Activity.class));
    }
}