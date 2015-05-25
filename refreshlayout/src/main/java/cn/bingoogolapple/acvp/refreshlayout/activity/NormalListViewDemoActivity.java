package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.adapter.AdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalListViewDemoActivity extends BaseListViewDemoActivity {
    private static final String TAG = NormalListViewDemoActivity.class.getSimpleName();
    private AdapterViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this, true);
        moocStyleRefreshViewHolder.setUltimateColor(Color.rgb(0, 0, 255));
        moocStyleRefreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iqegg));
        moocStyleRefreshViewHolder.setLoadMoreBackgroundColorRes(android.R.color.holo_blue_light);
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        initCustomHeaderView();
    }

    @Override
    protected void initListView() {
        mAdapter = new AdapterViewAdapter(this);
        mAdapter.setDatas(mDatas);
        mDataLv.setAdapter(mAdapter);

        mDataLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(TAG, "滚动状态变化");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "正在滚动");
            }
        });
    }

    @Override
    protected void onEndRefreshing(List<RefreshModel> datas) {
        mDatas.addAll(0, datas);
        mAdapter.setDatas(mDatas);
    }

    @Override
    protected void onEndLoadingMore(List<RefreshModel> datas) {
        mAdapter.addDatas(datas);
    }

    @Override
    public void onClick(View v) {
    }
}