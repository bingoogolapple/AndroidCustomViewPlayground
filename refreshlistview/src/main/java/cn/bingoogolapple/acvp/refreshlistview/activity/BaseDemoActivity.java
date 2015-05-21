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
import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlistview.widget.BGARefreshListView;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public abstract class BaseDemoActivity extends AppCompatActivity implements BGARefreshListView.BGARefreshListViewDelegate, AdapterView.OnItemClickListener, View.OnClickListener {
    protected BGARefreshListView mRefreshListView;
    protected List<RefreshModel> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        mRefreshListView = (BGARefreshListView) findViewById(R.id.rlv_demo_list);
        mRefreshListView.setDelegate(this);
        mRefreshListView.setOnItemClickListener(this);

        initDatas();
        initListView();
    }

    protected abstract void initListView();

    protected void initCustomHeaderView() {
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

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            mDatas.add(new RefreshModel("title" + i, "detail" + i));
        }
    }

    @Override
    public void onBGARefreshListViewBeginRefreshing() {
//        Toast.makeText(this, "正在刷新数据", Toast.LENGTH_SHORT).show();

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
                mRefreshListView.endRefreshing();
                List<RefreshModel> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    datas.add(new RefreshModel("newTitle" + i, "newDetail" + i));
                }
                onEndRefreshing(datas);
            }
        }.execute();
    }

    protected abstract void onEndRefreshing(List<RefreshModel> datas);

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

                List<RefreshModel> datas = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    datas.add(new RefreshModel("moreTitle" + i, "moreDetail" + i));
                }
                onEndLoadingMore(datas);
            }
        }.execute();
    }

    protected abstract void onEndLoadingMore(List<RefreshModel> datas);

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 因为加了HeaderView，position需要减一
        int newPosition = position - 1;
        if (mDatas != null && mDatas.size() > 0 && newPosition > -1 && newPosition < mDatas.size()) {
            RefreshModel refreshModel = mDatas.get(newPosition);
            Toast.makeText(this, "点击了" + refreshModel.mTitle, Toast.LENGTH_SHORT).show();
        }
    }
}