package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.velocitytracker.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    public void normalRecyclerViewDemo(View v) {
        startActivity(new Intent(this, NormalRecyclerViewActivity.class));
    }

    public void normalListViewDemo(View v) {
        startActivity(new Intent(this, NormalListViewActivity.class));
    }

    public void swipeRecyclerViewDemo(View v) {
        startActivity(new Intent(this, SwipeRecyclerViewActivity.class));
    }

    public void swipeListViewDemo(View v) {
        startActivity(new Intent(this, SwipeListViewActivity.class));
    }

    public void scrollViewDemo(View v) {
        startActivity(new Intent(this, ScrollViewActivity.class));
    }

    public void webViewDemo(View v) {
        startActivity(new Intent(this, WebViewActivity.class));
    }

    public void viewPagerDemo(View v) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

}