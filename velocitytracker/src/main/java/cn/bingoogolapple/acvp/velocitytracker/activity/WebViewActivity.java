package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bingoogolapple.acvp.velocitytracker.R;

public class WebViewActivity extends BaseActivity {
    private WebView mContentWv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_webview);
        mContentWv = (WebView) findViewById(R.id.wv_webview_content);
    }

    @Override
    protected void setListener() {
        findViewById(R.id.retweet).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);
        findViewById(R.id.praise).setOnClickListener(this);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mContentWv.setWebViewClient(new WebViewClient());
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.loadUrl("http://www.imooc.com");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.retweet) {
            showToast("点击了转发");
        } else if (v.getId() == R.id.comment) {
            showToast("点击了评论");
        } else if (v.getId() == R.id.praise) {
            showToast("点击了赞");
        }
    }

}