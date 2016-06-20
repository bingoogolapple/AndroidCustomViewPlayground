package cn.bingoogolapple.animation.activity;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import cn.bingoogolapple.animation.R;
import cn.bingoogolapple.bacvp.BaseActivity;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/20 下午4:27
 * 描述:
 */
public class ChrysanthemumActivity extends BaseActivity {
    private ImageView mLoadingIv;
    private AnimationDrawable mAd;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_chrysanthemum);
        mLoadingIv = getViewById(R.id.iv_chrysanthemum);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mAd = (AnimationDrawable) mLoadingIv.getDrawable();
        mAd.start();
    }
}
