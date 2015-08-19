package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.acvp.velocitytracker.R;
import cn.bingoogolapple.acvp.velocitytracker.widget.BGAStickyNavRefreshLayout;

public class ScrollViewActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ScrollViewActivity.class.getSimpleName();
    private BGAStickyNavRefreshLayout mStickyNavRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);

        initView();
        setListener();
    }

    private void initView() {
        mStickyNavRefreshLayout = (BGAStickyNavRefreshLayout) findViewById(R.id.stickyNavRefreshLayout);
    }

    private void setListener() {
        findViewById(R.id.retweet).setOnClickListener(this);
        findViewById(R.id.comment).setOnClickListener(this);
        findViewById(R.id.praise).setOnClickListener(this);
        findViewById(R.id.tv_scrollview_clickablelabel).setOnClickListener(this);
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

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}