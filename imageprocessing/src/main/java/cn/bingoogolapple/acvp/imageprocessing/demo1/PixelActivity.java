package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bacvp.BaseActivity;


public class PixelActivity extends BaseActivity {
    private ImageView mIcon1;
    private ImageView mIcon2;
    private ImageView mIcon3;
    private ImageView mIcon4;

    private Bitmap mBitmap;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_pixel);
        mIcon1 = getViewById(R.id.iv_pixel_icon1);
        mIcon2 = getViewById(R.id.iv_pixel_icon2);
        mIcon3 = getViewById(R.id.iv_pixel_icon3);
        mIcon4 = getViewById(R.id.iv_pixel_icon4);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.img3);
        mIcon1.setImageBitmap(mBitmap);
        mIcon2.setImageBitmap(ImageHelper.handleImagePixelNegative(mBitmap));
        mIcon3.setImageBitmap(ImageHelper.handleImagePixelOldPhoto(mBitmap));
        mIcon4.setImageBitmap(ImageHelper.handleImagePixelRelief(mBitmap));
    }
}