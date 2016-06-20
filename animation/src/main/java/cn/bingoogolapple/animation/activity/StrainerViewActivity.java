package cn.bingoogolapple.animation.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.ImageView;

import cn.bingoogolapple.animation.R;
import cn.bingoogolapple.bacvp.BaseActivity;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:16/6/20 下午5:22
 * 描述:
 */
public class StrainerViewActivity extends BaseActivity {
    private ImageView mStrainerView;
    private Drawable mOriginalDrawable;
    private Handler mHandler;
    private int mProgress = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_strainerview);
        mStrainerView = getViewById(R.id.strainerview);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mOriginalDrawable = getResources().getDrawable(R.mipmap.strainer_red);

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
            DrawableCompat.setTint(mOriginalDrawable, Color.parseColor("#00ff00"));
        } else if (value < 50) {
            DrawableCompat.setTint(mOriginalDrawable, Color.parseColor("#0000ff"));
        } else {
            DrawableCompat.setTint(mOriginalDrawable, Color.parseColor("#ff0000"));
        }

        int insetTop = mOriginalDrawable.getIntrinsicHeight() * (100 - value) / 100;
        InsetDrawable insetDrawable = new InsetDrawable(mOriginalDrawable, 0, insetTop, 0, 0);
        mStrainerView.setImageDrawable(insetDrawable);
    }
}
