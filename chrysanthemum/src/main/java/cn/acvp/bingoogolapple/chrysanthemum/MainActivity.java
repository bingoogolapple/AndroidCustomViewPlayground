package cn.acvp.bingoogolapple.chrysanthemum;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;

public class MainActivity extends ActionBarActivity {
    private ImageView mLoadingIv;
    private AnimationDrawable mAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingIv = (ImageView) findViewById(R.id.iv_main_loading);
        mAd = (AnimationDrawable) mLoadingIv.getDrawable();
        mAd.start();
    }

}