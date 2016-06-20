package cn.bingoogolapple.acvp.imageprocessing.demo3;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.bingoogolapple.acvp.imageprocessing.R;
import cn.bingoogolapple.bacvp.BaseActivity;

public class Demo3Activity extends BaseActivity {
    private ViewPager mContentVp;
    private int[] mImgs = new int[]{R.mipmap.img1, R.mipmap.img2, R.mipmap.img3};
    private ImageView[] mImageViews = new ImageView[mImgs.length];

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_demo3);
        mContentVp = getViewById(R.id.vp_demo3_content);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mContentVp.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                ZoomImageView imageView = new ZoomImageView(getApplicationContext());
                imageView.setImageResource(mImgs[position]);
                container.addView(imageView);
                mImageViews[position] = imageView;
                return imageView;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mImageViews[position]);
            }

            @Override
            public int getCount() {
                return mImgs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }
        });
    }
}