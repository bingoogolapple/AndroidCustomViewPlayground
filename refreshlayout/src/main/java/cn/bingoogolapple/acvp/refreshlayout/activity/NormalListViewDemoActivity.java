package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.view.View;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.adapter.AdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalListViewDemoActivity extends BaseListViewDemoActivity {
    private AdapterViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
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