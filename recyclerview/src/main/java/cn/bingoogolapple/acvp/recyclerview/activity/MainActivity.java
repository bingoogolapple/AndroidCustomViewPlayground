package cn.bingoogolapple.acvp.recyclerview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import cn.bingoogolapple.acvp.recyclerview.R;
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
            case R.id.btn_main_helloworld:
                startActivity(new Intent(this, HelloworldActivity.class));
                break;
            case R.id.btn_main_headindex:
                startActivity(new Intent(this, HeadindexActivity.class));
                break;
            case R.id.btn_main_indexview:
                startActivity(new Intent(this, IndexviewActivity.class));
                break;
        }
    }

}