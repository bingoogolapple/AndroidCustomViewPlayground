package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.velocitytracker.R;

public class ScrollViewActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_scrollview);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.retweet).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);
        findViewById(R.id.praise).setOnClickListener(this);
        findViewById(R.id.tv_scrollview_clickablelabel).setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.retweet) {
            showToast("点击了转发");
        } else if (v.getId() == R.id.comment) {
            showToast("点击了评论");
        } else if (v.getId() == R.id.praise) {
            showToast("点击了赞");
        } else if (v.getId() == R.id.tv_scrollview_clickablelabel) {
            showToast("点击了测试文本");
        }
    }

}