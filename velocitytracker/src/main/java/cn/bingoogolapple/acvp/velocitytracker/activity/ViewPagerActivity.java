package cn.bingoogolapple.acvp.velocitytracker.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import cn.bingoogolapple.acvp.velocitytracker.R;
import cn.bingoogolapple.acvp.velocitytracker.fragment.ListViewFragment;
import cn.bingoogolapple.acvp.velocitytracker.fragment.RecyclerViewFragment;
import cn.bingoogolapple.acvp.velocitytracker.fragment.ScrollViewFragment;
import cn.bingoogolapple.acvp.velocitytracker.fragment.WebViewFragment;
import cn.bingoogolapple.bgaindicator.BGAFixedIndicator;

public class ViewPagerActivity extends BaseActivity {
    private ViewPager mContentVp;
    private BGAFixedIndicator mIndicator;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_viewpager);
        mIndicator = (BGAFixedIndicator) findViewById(R.id.indicator);
        mContentVp = (ViewPager) findViewById(R.id.vp_viewpager_content);
    }

    @Override
    protected void setListener() {
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        mContentVp.setAdapter(new ContentViewPagerAdapter(getSupportFragmentManager(), this));
        mIndicator.initData(0, mContentVp);
    }

    private static class ContentViewPagerAdapter extends FragmentPagerAdapter {
        private Class[] mFragments;
        private String[] mTitles;
        private Context mContext;

        public ContentViewPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            mContext = context;
            mFragments = new Class[4];
            mFragments[0] = RecyclerViewFragment.class;
            mFragments[1] = ListViewFragment.class;
            mFragments[2] = ScrollViewFragment.class;
            mFragments[3] = WebViewFragment.class;

            mTitles = new String[4];
            mTitles[0] = "RecyclerView";
            mTitles[1] = "ListView";
            mTitles[2] = "ScrollView";
            mTitles[3] = "WebView";
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            return Fragment.instantiate(mContext, mFragments[position].getName());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }
    }

}