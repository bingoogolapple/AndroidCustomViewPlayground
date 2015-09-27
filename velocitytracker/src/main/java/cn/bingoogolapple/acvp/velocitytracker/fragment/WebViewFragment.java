package cn.bingoogolapple.acvp.velocitytracker.fragment;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.bingoogolapple.acvp.velocitytracker.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/9/27 下午12:53
 * 描述:
 */
public class WebViewFragment extends BaseFragment {
    private WebView mContentWv;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_webview);
        mContentWv = getViewById(R.id.wv_webview_content);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mContentWv.setWebViewClient(new WebViewClient());
        mContentWv.getSettings().setJavaScriptEnabled(true);
        mContentWv.loadUrl("http://www.imooc.com");
    }

    @Override
    protected void onUserVisible() {
    }
}