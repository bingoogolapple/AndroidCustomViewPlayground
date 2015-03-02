package cn.bingoogolapple.acvp.flowlayout;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends ActionBarActivity {
    private FlowLayout mFlowLayout;
    private String[] mVals = new String[]{"Hello", "AndroidAndroidAndroid", "Welcome", "Button", "HelloHello", "AndroidWelcome", "Welcome", "Button", "HelloWelcome", "Android", "WelcomeWelcome", "Button"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout = (FlowLayout) findViewById(R.id.flowlayout);
        initData();
    }

    public void initData() {
        for (int i = 0; i < mVals.length; i++) {
            Button button = new Button(this);
            ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT);
            button.setText(mVals[i]);
            mFlowLayout.addView(button, lp);
        }
    }
}