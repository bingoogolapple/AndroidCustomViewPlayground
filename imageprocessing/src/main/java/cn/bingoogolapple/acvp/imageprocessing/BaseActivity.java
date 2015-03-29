package cn.bingoogolapple.acvp.imageprocessing;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import cn.bingoogolapple.bgaannotation.BGAA;

public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
        processLogic();
        setListener();
    }

    protected void processLogic() {
    }

    protected void setListener() {
    }

}