package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_helloworld)
public class HelloWorldActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_helloworld_topview:
                Toast.makeText(this, "点击了TopView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_helloworld_bottomview:
                Toast.makeText(this, "点击了BottomView", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}