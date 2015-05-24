package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.adapter.SwipeRecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemChildClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemChildLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class SwipeRecyclerViewDemoActivity extends BaseRecyclerViewDemoActivity implements BGAOnRVItemChildClickListener, BGAOnRVItemChildLongClickListener {
    private SwipeRecyclerViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
//        initCustomHeaderView();
    }

    @Override
    protected void initRecyclerView() {
        mDataRv.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new SwipeRecyclerViewAdapter(this);
        mAdapter.setDatas(mDatas);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnRVItemChildClickListener(this);
        mAdapter.setOnRVItemChildClickListener(this);
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

    @Override
    public void onRVItemChildClick(View v, int position) {
        mAdapter.removeItem(position);
    }

    @Override
    public boolean onRVItemChildLongClick(View v, int position) {
        Toast.makeText(this, "长按了删除 " + position, Toast.LENGTH_SHORT).show();
        return true;
    }
}