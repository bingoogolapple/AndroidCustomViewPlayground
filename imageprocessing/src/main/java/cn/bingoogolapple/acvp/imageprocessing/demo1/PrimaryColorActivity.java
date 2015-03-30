package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.SeekBar;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_primarycolor)
public class PrimaryColorActivity extends BaseActivity implements SeekBar.OnSeekBarChangeListener {
    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;
    @BGAAView(R.id.iv_img3_icon)
    private ImageView mIconIv;
    @BGAAView(R.id.sb_img3_hue)
    private SeekBar mHueSb;
    @BGAAView(R.id.sb_img3_saturation)
    private SeekBar mSaturationSb;
    @BGAAView(R.id.sb_img3_lum)
    private SeekBar mLumSb;
    private float mHue = 0.0f;
    private float mSaturation = 1.0f;
    private float mLum = 1.0f;
    private Bitmap mBitmap;

    @Override
    protected void processLogic() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        mIconIv.setImageBitmap(mBitmap);
        mHueSb.setMax(MAX_VALUE);
        mSaturationSb.setMax(MAX_VALUE);
        mLumSb.setMax(MAX_VALUE);

        mHueSb.setProgress(MID_VALUE);
        mSaturationSb.setProgress(MID_VALUE);
        mLumSb.setProgress(MID_VALUE);
    }

    @Override
    protected void setListener() {
        mHueSb.setOnSeekBarChangeListener(this);
        mSaturationSb.setOnSeekBarChangeListener(this);
        mLumSb.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sb_img3_hue:
                // 表达式并不固定，此为经验值
                mHue = (progress - MID_VALUE) * 1.0f / MID_VALUE * 180;
                break;
            case R.id.sb_img3_saturation:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.sb_img3_lum:
                mLum = progress * 1.0f / MID_VALUE;
                break;
        }
        mIconIv.setImageBitmap(ImageHelper.handleImageEffect(mBitmap, mHue, mSaturation, mLum));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}