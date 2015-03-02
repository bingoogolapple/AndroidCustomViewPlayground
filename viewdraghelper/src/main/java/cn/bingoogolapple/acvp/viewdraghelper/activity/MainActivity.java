package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_youtube:
                startActivity(new Intent(this,YouTubeActivity.class));
                break;
            case R.id.btn_main_swiperecyclerviewitem:
                startActivity(new Intent(this,SwipeRecyclerViewItemActivity.class));
                break;
        }
    }

}