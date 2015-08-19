package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import cn.bingoogolapple.acvp.velocitytracker.R;
import cn.bingoogolapple.acvp.velocitytracker.widget.BGAStickyNavRefreshLayout;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = WebViewActivity.class.getSimpleName();
    private BGAStickyNavRefreshLayout mStickyNavRefreshLayout;
    private WebView mContentWv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        mStickyNavRefreshLayout = (BGAStickyNavRefreshLayout) findViewById(R.id.stickyNavRefreshLayout);
        mContentWv = (WebView) findViewById(R.id.wv_webview_content);
    }

    private void setListener() {
        findViewById(R.id.retweet).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);
        findViewById(R.id.praise).setOnClickListener(this);
    }

    private void processLogic() {
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

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}