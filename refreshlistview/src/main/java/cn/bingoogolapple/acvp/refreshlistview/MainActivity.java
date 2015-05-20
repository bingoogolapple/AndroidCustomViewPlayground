package cn.bingoogolapple.acvp.refreshlistview;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.widget.BGANormalRefreshListView;
import cn.bingoogolapple.acvp.refreshlistview.widget.BGARefreshListView;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements BGARefreshListView.BGARefreshListViewDelegate {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BGAAView(R.id.lv_main_list)
    private BGANormalRefreshListView mRefreshListView;
    private MyAdapter mAdapter;
    private List<String> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        initDatas();
        initListView();
    }

    private void initListView() {
        View customHeaderView = View.inflate(this, R.layout.view_custom_header, null);
        mRefreshListView.addCustomHeaderView(customHeaderView);
        mRefreshListView.setDelegate(this);
        mAdapter = new MyAdapter(this, mDatas);
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
        Toast.makeText(this, "正在刷新数据", Toast.LENGTH_SHORT).show();

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
                initDatas();
                mAdapter.setDatas(mDatas);
            }
        }.execute();
    }

    @Override
    public void onBGARefreshListViewBeginLoadingMore() {
        Toast.makeText(this, "正在加载更多数据", Toast.LENGTH_SHORT).show();

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
                for (int i = 0; i < 5; i++) {
                    datas.add("newdata" + i);
                }
                mAdapter.addMore(datas);
            }
        }.execute();
    }

    private static class ViewHolder {
        public TextView mNameTv;
    }

    private static class MyAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mDatas;

        public MyAdapter(Context context, List<String> datas) {
            mContext = context;
            mDatas = datas;
        }

        @Override
        public int getCount() {
            return mDatas == null ? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null ? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_list, null);
                viewHolder = new ViewHolder();
                viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.tv_list_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mNameTv.setText(mDatas.get(position));
            return convertView;
        }

        public void addMore(List<String> datas) {
            mDatas.addAll(datas);
            notifyDataSetChanged();
        }

        public void setDatas(List<String> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }
    }

}