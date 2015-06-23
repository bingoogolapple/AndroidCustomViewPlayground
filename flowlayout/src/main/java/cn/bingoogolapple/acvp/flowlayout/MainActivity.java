package cn.bingoogolapple.acvp.flowlayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

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
            mFlowLayout.addView(getLabel(mVals[i]), new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        }
    }

    private TextView getLabel(String text) {
        TextView label = new TextView(this);
        label.setTextColor(Color.WHITE);
        label.setBackgroundResource(R.drawable.selector_tag);
        label.setGravity(Gravity.CENTER);
        int padding = FlowLayout.dp2px(this, 5);
        label.setPadding(padding, padding, padding, padding);
        label.setText(text);
        return label;
    }

    public void onClick(View view) {
        mFlowLayout.addView(getLabel(mVals[new Random().nextInt(mVals.length)]), new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT, ViewGroup.MarginLayoutParams.WRAP_CONTENT));
        mFlowLayout.requestLayout();
    }
}