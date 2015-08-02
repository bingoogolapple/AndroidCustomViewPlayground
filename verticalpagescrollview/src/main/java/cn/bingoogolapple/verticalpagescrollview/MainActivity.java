package cn.bingoogolapple.verticalpagescrollview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private VerticalPageScrollView mScrollView;

    private int[] ivIds = {R.mipmap.iv1, R.mipmap.iv2, R.mipmap.iv3, R.mipmap.iv4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScrollView = (VerticalPageScrollView) findViewById(R.id.scrollView);

        for (int ivId : ivIds) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(ivId);
            mScrollView.addView(iv);
        }
    }

}