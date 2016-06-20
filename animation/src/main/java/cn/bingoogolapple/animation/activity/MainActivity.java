package cn.bingoogolapple.animation.activity;

import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.animation.R;
import cn.bingoogolapple.bacvp.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    public void chrysanthemum(View v) {
        forwardActivity(ChrysanthemumActivity.class);
    }

    public void dustboxview(View v) {
        forwardActivity(DustBoxViewActivity.class);
    }

    public void strainerview(View v) {
        forwardActivity(StrainerViewActivity.class);
    }
}
