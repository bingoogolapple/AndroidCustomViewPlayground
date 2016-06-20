package cn.bingoogolapple.animation.activity;

import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import cn.bingoogolapple.animation.R;
import cn.bingoogolapple.bacvp.BaseActivity;

public class DustBoxViewActivity extends BaseActivity {
    private static final int MAX_PROGRESS = 10000;

    private ImageView mDustBoxView;
    private ClipDrawable mClipDrawable;
    private Handler mHandler;
    private int mProgress = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_dustboxview);
        mDustBoxView = getViewById(R.id.dustboxview);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mClipDrawable = (ClipDrawable) DrawableCompat.wrap(mDustBoxView.getDrawable());

        mHandler = new Handler();
        mHandler.post(mHandleProgressTask);
    }

    private Runnable mHandleProgressTask = new Runnable() {
        @Override
        public void run() {
            if (mProgress == 101) {
                mProgress = 0;
            }
            setData(mProgress);

            mProgress++;
            mHandler.postDelayed(mHandleProgressTask, 50);
        }
    };

    public void setData(int value) {
        if (value < 0 || value > 100) {
            throw new RuntimeException("value must be between 0 to 100");
        }
        if (value < 30) {
            DrawableCompat.setTint(mClipDrawable, Color.parseColor("#00ff00"));
        } else if (value < 50) {
            DrawableCompat.setTint(mClipDrawable, Color.parseColor("#0000ff"));
        } else {
            DrawableCompat.setTint(mClipDrawable, Color.parseColor("#ff0000"));
        }
        mClipDrawable.setLevel(value * MAX_PROGRESS * 42 / 5700);
    }
}