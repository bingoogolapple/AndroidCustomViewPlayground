package cn.bingoogolapple.acvp.refreshlistview.activity;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.adapter.SwipeViewAdapter;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:04
 * 描述:
 */
public class NormalSwipeActivity extends BaseNormalActivity {
    private SwipeViewAdapter mAdapter;
    @Override
    protected void setAdapter() {
        mAdapter = new SwipeViewAdapter(this, this, mDatas);
        mRefreshListView.setAdapter(mAdapter);
    }

    @Override
    protected void onEndRefreshing(List<String> datas) {
        mDatas.addAll(0, datas);
        mAdapter.setDatas(mDatas);
    }

    @Override
    protected void onEndLoadingMore(List<String> datas) {
        mAdapter.addDatas(datas);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "点击了删除" + v.getTag(), Toast.LENGTH_SHORT).show();
        mAdapter.remove((String) v.getTag());
        mAdapter.closeAllItems();
    }
}