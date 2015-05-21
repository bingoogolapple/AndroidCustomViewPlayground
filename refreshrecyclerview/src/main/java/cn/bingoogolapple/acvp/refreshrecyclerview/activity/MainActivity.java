package cn.bingoogolapple.acvp.refreshrecyclerview.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;
import cn.bingoogolapple.acvp.refreshrecyclerview.adapter.RefreshModeAdapter;
import cn.bingoogolapple.acvp.refreshrecyclerview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshrecyclerview.widget.BGARefreshLayout;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.Divider;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.OnItemClickListener;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.OnItemLongClickListener;

public class MainActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshRl;
    private RecyclerView mDataRv;
    protected List<RefreshModel> mDatas;
    private RefreshModeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRefreshRl = (BGARefreshLayout) findViewById(R.id.rl_demo_refresh);
        mRefreshRl.setDelegate(this);
        mDataRv = (RecyclerView) findViewById(R.id.rv_demo_data);
        mDataRv.setLayoutManager(new LinearLayoutManager(this));
        mDataRv.addItemDecoration(new Divider(this));
        mAdapter = new RefreshModeAdapter(this);
        initDatas();
        mAdapter.setDatas(mDatas);
        mDataRv.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mDatas.add(new RefreshModel("title" + i, "detail" + i));
        }
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

    @Override
    public void onBGARefreshLayoutBeginRefreshing() {
        Log.i(TAG, "开始刷新");
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
                mRefreshRl.endRefreshing();
                List<RefreshModel> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    datas.add(new RefreshModel("newTitle" + i, "newDetail" + i));
                }
                mDatas.addAll(0, datas);
                mAdapter.setDatas(mDatas);
            }
        }.execute();
    }

    @Override
    public void onBGARefreshLayoutBeginLoadingMore() {
        Toast.makeText(this, "开始加载", Toast.LENGTH_SHORT).show();
    }
}