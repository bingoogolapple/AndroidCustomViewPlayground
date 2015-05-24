package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAMoocStyleRefreshViewHolder;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGARefreshLayout;
import cn.bingoogolapple.acvp.refreshlayout.widget.Divider;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnRVItemLongClickListener;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public abstract class BaseRecyclerViewDemoActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate, BGAOnRVItemClickListener, BGAOnRVItemLongClickListener {
    protected BGARefreshLayout mRefreshLayout;
    protected List<RefreshModel> mDatas;
    protected RecyclerView mDataRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_recyclerview_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGAMoocStyleRefreshViewHolder(this));

        mDataRv = (RecyclerView) findViewById(R.id.rv_recyclerview_data);
        mDataRv.setLayoutManager(new LinearLayoutManager(this));
        mDataRv.addItemDecoration(new Divider(this));

        initDatas();
        initRefreshLayout();
        initRecyclerView();
    }

    protected abstract void initRefreshLayout();

    protected abstract void initRecyclerView();

    protected void initCustomHeaderView() {
        List<View> datas = new ArrayList<>();
        datas.add(getLayoutInflater().inflate(R.layout.view_one, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_two, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_three, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_four, null));

        View customHeaderView = View.inflate(this, R.layout.view_custom_header, null);
        BGABanner banner = (BGABanner) customHeaderView.findViewById(R.id.banner);
        banner.setViewPagerViews(datas);
        mRefreshLayout.addCustomHeaderView(customHeaderView);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add(new RefreshModel("title" + i, "detail" + i));
        }
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
                List<RefreshModel> datas = new ArrayList<>();
                for (int i = 0; i < 2; i++) {
                    datas.add(new RefreshModel("newTitle" + i, "newDetail" + i));
                }
                onEndRefreshing(datas);
            }
        }.execute();
    }

    protected abstract void onEndRefreshing(List<RefreshModel> datas);

    @Override
    public void onRVItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目 " + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目 " + mDatas.get(position).mTitle, Toast.LENGTH_SHORT).show();
        return true;
    }
}