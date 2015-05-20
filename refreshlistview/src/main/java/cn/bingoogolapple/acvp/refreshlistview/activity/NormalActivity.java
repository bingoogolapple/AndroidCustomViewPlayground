package cn.bingoogolapple.acvp.refreshlistview.activity;

import android.view.View;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.adapter.AdapterViewAdapter;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:03
 * 描述:
 */
public class NormalActivity extends BaseNormalActivity {
    private static final String TAG = NormalActivity.class.getSimpleName();
    private AdapterViewAdapter mAdapter;

    @Override
    protected void setAdapter() {
        mAdapter = new AdapterViewAdapter(this, mDatas);
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
    }
}