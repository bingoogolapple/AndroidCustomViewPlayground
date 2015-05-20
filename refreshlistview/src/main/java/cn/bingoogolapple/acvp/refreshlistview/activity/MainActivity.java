package cn.bingoogolapple.acvp.refreshlistview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 上午10:04
 * 描述:
 */
@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

    public void changeToNormal(View v) {
        startActivity(new Intent(this, NormalActivity.class));
    }

    public void changeToNormalSwipe(View v) {
        startActivity(new Intent(this, NormalSwipeActivity.class));
    }

}