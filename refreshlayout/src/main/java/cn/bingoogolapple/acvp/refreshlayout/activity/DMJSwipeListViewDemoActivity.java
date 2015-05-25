package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.adapter.DMJSwipeAdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlayout.engine.DataEngine;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGANormalRefreshViewHolder;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGARefreshLayout;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:06
 * 描述:
 */
public class DMJSwipeListViewDemoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, View.OnClickListener, BGARefreshLayout.BGARefreshLayoutDelegate {
    private static final String TAG = NormalListViewDemoActivity.class.getSimpleName();
    private BGARefreshLayout mRefreshLayout;
    private List<RefreshModel> mDatas;
    private ListView mDataLv;
    private DMJSwipeAdapterViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initRefreshLayout();
        initListView();
    }

    private void initRefreshLayout() {
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_listview_refresh);
        mRefreshLayout.setDelegate(this);

        BGANormalRefreshViewHolder normalRefreshViewHolder = new BGANormalRefreshViewHolder(this, true);
        normalRefreshViewHolder.setPullDownRefreshText("自定义下拉刷新文本");
        normalRefreshViewHolder.setReleaseRefreshText("自定义松开更新文本");
        normalRefreshViewHolder.setRefreshingText("自定义正在刷新文本");
        normalRefreshViewHolder.setLoadMoreBackgroundDrawableRes(R.drawable.shape_refresh_bg);
        mRefreshLayout.setRefreshViewHolder(normalRefreshViewHolder);
        mRefreshLayout.addCustomHeaderView(DataEngine.getCustomHeaderOrFooterView(this));
    }

    private void initListView() {
        mDataLv = (ListView) findViewById(R.id.lv_listview_data);
        mDataLv.setOnItemClickListener(this);
        mDataLv.setOnItemLongClickListener(this);
        mAdapter = new DMJSwipeAdapterViewAdapter(this, this);
        mDatas = DataEngine.loadInitDatas();
        mAdapter.setDatas(mDatas);
        mDataLv.setAdapter(mAdapter);

        mDataLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.i(TAG, "滚动状态变化");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.i(TAG, "正在滚动");
            }
        });

        mDataLv.addFooterView(DataEngine.getCustomHeaderOrFooterView(this));
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
    public void onClick(View v) {
        if (v.getId() == R.id.tv_item_swipelist_delete) {
            mAdapter.removeItem((RefreshModel) v.getTag());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "点击了条目 " + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "长按了" + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
        return true;
    }

}