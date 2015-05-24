package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.support.v7.widget.GridLayoutManager;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.adapter.RecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalRecyclerViewDemoActivity extends BaseRecyclerViewDemoActivity {
    private RecyclerViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
        initCustomHeaderView();
    }

    @Override
    protected void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(gridLayoutManager);

        mAdapter = new RecyclerViewAdapter(this);
        mAdapter.setDatas(mDatas);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mDataRv.setAdapter(mAdapter);
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

}