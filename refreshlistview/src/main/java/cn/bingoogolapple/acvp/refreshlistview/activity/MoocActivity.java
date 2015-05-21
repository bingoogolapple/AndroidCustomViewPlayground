package cn.bingoogolapple.acvp.refreshlistview.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.acvp.refreshlistview.adapter.AdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlistview.widget.BGAMoocRefreshListView;
import cn.bingoogolapple.acvp.refreshlistview.widget.BGARefreshListView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public class MoocActivity extends AppCompatActivity implements BGARefreshListView.BGARefreshListViewDelegate, AdapterView.OnItemClickListener, View.OnClickListener {
    private BGAMoocRefreshListView mRefreshListView;
    private List<String> mDatas;
    private AdapterViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mooc);
        mRefreshListView = (BGAMoocRefreshListView) findViewById(R.id.mrlv_mooc_list);
        initDatas();
        initListView();
    }

    private void initListView() {
        initCustomHeaderView();
        mRefreshListView.setDelegate(this);
        mRefreshListView.setOnItemClickListener(this);
        setAdapter();
    }

    private void initCustomHeaderView() {
        List<View> datas = new ArrayList<>();
        datas.add(getLayoutInflater().inflate(R.layout.view_one, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_two, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_three, null));
        datas.add(getLayoutInflater().inflate(R.layout.view_four, null));

        View customHeaderView = View.inflate(this, R.layout.view_custom_header, null);
        BGABanner banner = (BGABanner) customHeaderView.findViewById(R.id.banner);
        banner.setViewPagerViews(datas);
        mRefreshListView.addCustomHeaderView(customHeaderView);
    }

    protected void setAdapter() {
        mAdapter = new AdapterViewAdapter(this, mDatas);
        mRefreshListView.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add("name" + i);
        }
    }

    @Override
    public void onBGARefreshListViewBeginRefreshing() {
//        Toast.makeText(this, "正在刷新数据", Toast.LENGTH_SHORT).show();

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
                mRefreshListView.endRefreshing();
                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    datas.add("NewLoadMoreData" + i);
                }
                onEndRefreshing(datas);
            }
        }.execute();
    }

    protected void onEndRefreshing(List<String> datas) {
        mDatas.addAll(0, datas);
        mAdapter.setDatas(mDatas);
    }

    @Override
    public void onBGARefreshListViewBeginLoadingMore() {
//        Toast.makeText(this, "正在加载更多数据", Toast.LENGTH_SHORT).show();

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
                mRefreshListView.endRefreshing();

                List<String> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    datas.add("NewLoadMoreData" + i);
                }
                onEndLoadingMore(datas);
            }
        }.execute();
    }

    protected void onEndLoadingMore(List<String> datas) {
        mAdapter.addDatas(datas);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int newPosition = position - 1;
        if (mDatas != null && mDatas.size() > 0 && newPosition > -1 && newPosition < mDatas.size()) {
            Toast.makeText(this, "点击了" + mDatas.get(newPosition), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {

    }
}