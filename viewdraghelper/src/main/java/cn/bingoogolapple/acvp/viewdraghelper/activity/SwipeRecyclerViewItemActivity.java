package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_swiperecyclerviewitem)
public class SwipeRecyclerViewItemActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }
}