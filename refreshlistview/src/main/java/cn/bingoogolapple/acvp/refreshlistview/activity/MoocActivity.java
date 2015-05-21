package cn.bingoogolapple.acvp.refreshlistview.activity;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.adapter.SwipeViewAdapter;
import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlistview.widget.BGAMoocRefreshViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:04
 * 描述:
 */
public class MoocActivity extends BaseDemoActivity {
    private SwipeViewAdapter mAdapter;

    @Override
    protected void initListView() {
        initCustomHeaderView();
        mRefreshListView.setRefreshViewHolder(new BGAMoocRefreshViewHolder(this));
        mAdapter = new SwipeViewAdapter(this, this, mDatas);
        mRefreshListView.setAdapter(mAdapter);
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
        RefreshModel refreshModel = (RefreshModel) v.getTag();
        Toast.makeText(this, "点击了删除" + refreshModel.mTitle, Toast.LENGTH_SHORT).show();
        mAdapter.remove(refreshModel);
        mAdapter.closeAllItems();
    }
}