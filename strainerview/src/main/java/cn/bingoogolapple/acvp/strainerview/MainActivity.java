package cn.bingoogolapple.acvp.strainerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
    private StrainerView mStrainerView;
    private Handler mHandler;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStrainerView = (StrainerView) findViewById(R.id.strainerview);
        mHandler = new Handler();
        mHandler.post(mHandleProgressTask);
    }

    private Runnable mHandleProgressTask = new Runnable() {
        @Override
        public void run() {
            if(mProgress == 101) {
                mProgress = 0;
            }
            mStrainerView.setData(mProgress);
            mProgress++;
            mHandler.postDelayed(mHandleProgressTask, 50);
        }
    };
}