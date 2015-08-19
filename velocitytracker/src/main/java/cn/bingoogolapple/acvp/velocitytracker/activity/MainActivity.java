package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.bingoogolapple.acvp.velocitytracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

}