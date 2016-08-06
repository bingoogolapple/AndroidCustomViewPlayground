package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.bacvp.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_youtube:
                startActivity(new Intent(this, YouTubeActivity.class));
                break;
        }
    }

}