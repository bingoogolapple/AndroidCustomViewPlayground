package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlayout.R;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void changeToNormalRecyclerViewDemo(View v) {
        startActivity(new Intent(this, NormalRecyclerViewDemoActivity.class));
    }

    public void changeToSwipeRecyclerViewDemo(View v) {
        startActivity(new Intent(this, DMJSwipeRecyclerViewDemoActivity.class));
    }

    public void changeToNormalListViewDemo(View v) {
        startActivity(new Intent(this, NormalListViewDemoActivity.class));
    }

    public void changeToSwipeListViewDemo(View v) {
        startActivity(new Intent(this, DMJSwipeListViewDemoActivity.class));
    }
    public void changeToScrollViewDemo(View v) {
        startActivity(new Intent(this, ScrollViewDemoActivity.class));
    }

    public void changeToNormalViewDemo(View v) {
        startActivity(new Intent(this, NormalViewDemoActivity.class));
    }

}