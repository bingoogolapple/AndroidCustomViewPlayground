package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.adapter.DMJSwipeRecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.engine.DataEngine;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGARefreshLayout;
import cn.bingoogolapple.acvp.refreshlayout.widget.Divider;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemChildClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class DMJSwipeRecyclerViewDemoActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnRVItemChildClickListener, BGAOnRVItemChildLongClickListener {
    private DMJSwipeRecyclerViewAdapter mAdapter;
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private RecyclerView mDataRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);

        initRefreshLayout();
        initRecyclerView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this, true));
        mRefreshLayout.addCustomHeaderView(DataEngine.getCustomHeaderOrFooterView(this));
    }

    private void initRecyclerView() {
        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);
        mDataRv.addItemDecoration(new Divider(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mAdapter = new DMJSwipeRecyclerViewAdapter(this);
        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);

        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnRVItemChildClickListener(this);
        mAdapter.setOnRVItemChildClickListener(this);
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mDatas.addAll(0, DataEngine.loadNewData());
                mAdapter.setDatas(mDatas);
            }
        }.execute();
    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mRefreshLayout.endRefreshing();
                mAdapter.addDatas(DataEngine.loadMoreData());
            }
        }.execute();
    }

    @Override
    public void onRVItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目 " + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目 " + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
        return true;
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