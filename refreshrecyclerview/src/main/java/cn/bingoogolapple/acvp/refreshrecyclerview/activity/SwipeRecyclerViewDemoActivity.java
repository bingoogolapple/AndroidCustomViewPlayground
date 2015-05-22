package cn.bingoogolapple.acvp.refreshrecyclerview.activity;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshrecyclerview.adapter.SwipeRecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshrecyclerview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.OnItemClickListener;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.OnItemLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class SwipeRecyclerViewDemoActivity extends BaseRecyclerViewDemoActivity implements OnItemClickListener, OnItemLongClickListener {
    private SwipeRecyclerViewAdapter mAdapter;
    @Override
    protected void initRefreshLayout() {
        initCustomHeaderView();
    }

    @Override
    protected void initRecyclerView() {
        mAdapter = new SwipeRecyclerViewAdapter(this);
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
    }

    @Override
    protected void onEndRefreshing(List<RefreshModel> datas) {
        mDatas.addAll(0, datas);
        mAdapter.setDatas(mDatas);
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "点击了" + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了" + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
        return false;
    }

}