package cn.bingoogolapple.acvp.refreshlistview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.widget.NormalRefreshListView;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @BGAAView(R.id.lv_main_list)
    private NormalRefreshListView mList;
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
        mList.addCustomHeaderView(customHeaderView);
        mAdapter = new MyAdapter(this, mDatas);
        mList.setAdapter(mAdapter);
    }

    private void initDatas() {
        mDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mDatas.add("name" + i);
        }
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
            return mDatas == null? 0 : mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas == null? null : mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mContext,R.layout.item_list, null);
                viewHolder = new ViewHolder();
                viewHolder.mNameTv = (TextView) convertView.findViewById(R.id.tv_list_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.mNameTv.setText(mDatas.get(position));
            return convertView;
        }
    }

}