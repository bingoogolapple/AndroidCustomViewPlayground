package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;

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
    private AdapterViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
        BGAMoocStyleRefreshViewHolder moocStyleRefreshViewHolder = new BGAMoocStyleRefreshViewHolder(this);
        moocStyleRefreshViewHolder.setUltimateColor(Color.rgb(0, 0, 255));
        moocStyleRefreshViewHolder.setOriginalBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.iqegg));
        mRefreshLayout.setRefreshViewHolder(moocStyleRefreshViewHolder);
        initCustomHeaderView();
    }

    @Override
    protected void initListView() {
        mAdapter = new AdapterViewAdapter(this);
        mAdapter.setDatas(mDatas);
        mDataLv.setAdapter(mAdapter);
    }

    @Override
    protected void onEndRefreshing(List<RefreshModel> datas) {
        mDatas.addAll(0, datas);
        mAdapter.setDatas(mDatas);
    }

    @Override
    public void onClick(View v) {
    }
}