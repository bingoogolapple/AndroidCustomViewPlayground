package cn.bingoogolapple.acvp.imageprocessing.demo1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import cn.bingoogolapple.acvp.imageprocessing.BaseActivity;
import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_pixel)
public class PixelActivity extends BaseActivity {
    @BGAAView(R.id.iv_pixel_icon1)
    private ImageView mIcon1;
    @BGAAView(R.id.iv_pixel_icon2)
    private ImageView mIcon2;
    @BGAAView(R.id.iv_pixel_icon3)
    private ImageView mIcon3;
    @BGAAView(R.id.iv_pixel_icon4)
    private ImageView mIcon4;

    private Bitmap mBitmap;

    @Override
    protected void processLogic() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.primarycolor);
        mIcon1.setImageBitmap(mBitmap);
        mIcon2.setImageBitmap(ImageHelper.handleImagePixelNegative(mBitmap));
        mIcon3.setImageBitmap(ImageHelper.handleImagePixelOldPhoto(mBitmap));
        mIcon4.setImageBitmap(ImageHelper.handleImagePixelRelief(mBitmap));
    }
}