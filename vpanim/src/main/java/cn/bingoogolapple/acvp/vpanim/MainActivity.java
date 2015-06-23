package cn.bingoogolapple.acvp.vpanim;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private HorizontalViewPager mViewPager;
    private static final int[] mImgIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3, R.mipmap.guide_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = (HorizontalViewPager) findViewById(R.id.viewpager);
//        mViewPager.setScrollable(false);

        mViewPager.setAdapter(new AnimPagerAdapter(this));
        mViewPager.setPageTransformer(true, new AlphaPageTransformer());
    }

    public void rotate(View v) {
        mViewPager.setAdapter(new AnimPagerAdapter(this));
        mViewPager.setPageTransformer(true, new RotatePageTransformer());
    }

    public void alpha(View v) {
        mViewPager.setAdapter(new AnimPagerAdapter(this));
        mViewPager.setPageTransformer(true, new AlphaPageTransformer());
    }

    public void zoom(View v) {
        mViewPager.setAdapter(new AnimPagerAdapter(this));
        mViewPager.setPageTransformer(true, new ZoomPageTransformer());
    }

    public void depth(View v) {
        mViewPager.setAdapter(new AnimPagerAdapter(this));
        mViewPager.setPageTransformer(true, new DepthPageTransformer());
    }

    private static final class AnimPagerAdapter extends PagerAdapter {
        private Context mContext;

        public AnimPagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return mImgIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(mImgIds[position]);
            // 这里采用了.9图片，所以用FIT_XY
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}