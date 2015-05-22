package cn.bingoogolapple.acvp.refreshrecyclerview.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;
import cn.bingoogolapple.acvp.refreshrecyclerview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.Divider;
import cn.bingoogolapple.acvp.refreshrecyclerview.widget.BGAMoocRefreshViewHolder;
import cn.bingoogolapple.acvp.refreshrecyclerview.widget.BGARefreshLayout;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public abstract class BaseRecyclerViewDemoActivity extends AppCompatActivity  implements BGARefreshLayout.BGARefreshLayoutDelegate {
    protected BGARefreshLayout mRefreshLayout;
    protected List<RefreshModel> mDatas;
    protected RecyclerView mDataRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_demo_refresh);
        mRefreshLayout.setDelegate(this);
        mRefreshLayout.setRefreshViewHolder(new BGAMoocRefreshViewHolder(this));

        mDataRv = (RecyclerView) findViewById(R.id.rv_demo_data);
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

}