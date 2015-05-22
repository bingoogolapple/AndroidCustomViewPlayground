package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.util.BGARecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.util.BGARecyclerViewHolder;
import cn.bingoogolapple.acvp.refreshlayout.util.OnItemClickListener;
import cn.bingoogolapple.acvp.refreshlayout.util.OnItemLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class NormalRecyclerViewDemoActivity extends BaseRecyclerViewDemoActivity implements OnItemClickListener, OnItemLongClickListener {
    private NormalRecyclerViewAdapter mAdapter;

    @Override
    protected void initRefreshLayout() {
        initCustomHeaderView();
    }

    @Override
    protected void initRecyclerView() {
        mAdapter = new NormalRecyclerViewAdapter(this);
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

    public class NormalRecyclerViewAdapter extends BGARecyclerViewAdapter<RefreshModel> {
        public NormalRecyclerViewAdapter(NormalRecyclerViewDemoActivity normalActivity) {
            super(normalActivity, normalActivity);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_list;
        }

        @Override
        public void fillData(BGARecyclerViewHolder viewHolder, int position, RefreshModel model) {
            viewHolder.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
        }
    }
}