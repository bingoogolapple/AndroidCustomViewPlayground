package cn.bingoogolapple.acvp.uitableview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

}