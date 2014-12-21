package cn.bingoogolapple.acvp.dustboxview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;

public class MainActivity extends ActionBarActivity {
    private DustBoxView mDustBoxView;
    private Handler mHandler;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDustBoxView = (DustBoxView) findViewById(R.id.dustboxview);
        mHandler = new Handler();
        mHandler.post(mHandleProgressTask);
    }

    private Runnable mHandleProgressTask = new Runnable() {
        @Override
        public void run() {
            if(mProgress == 101) {
                mProgress = 0;
            }
            mDustBoxView.setData(mProgress);
            mProgress++;
            mHandler.postDelayed(mHandleProgressTask, 100);
        }
    };
}