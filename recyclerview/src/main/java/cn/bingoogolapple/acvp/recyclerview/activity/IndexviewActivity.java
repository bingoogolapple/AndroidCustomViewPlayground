package cn.bingoogolapple.acvp.recyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_indexview)
public class IndexviewActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

}