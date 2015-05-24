package cn.bingoogolapple.acvp.refreshlayout.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGARefreshLayout;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGAStickinessRefreshViewHolder;
import cn.bingoogolapple.bgabanner.BGABanner;


/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:22
 * 描述:
 */
public class ScrollViewDemoActivity extends AppCompatActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    private BGARefreshLayout mRefreshLayout;
    private TextView mClickableLabelTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollview);
        mRefreshLayout = (BGARefreshLayout) findViewById(R.id.rl_scrollview_refresh);
        mRefreshLayout.setDelegate(this);
        BGAStickinessRefreshViewHolder stickinessRefreshViewHolder = new BGAStickinessRefreshViewHolder(this);
        stickinessRefreshViewHolder.setStickinessColor(Color.parseColor("#11cd6e"));
        stickinessRefreshViewHolder.setRotateDrawable(getResources().getDrawable(R.mipmap.custom_stickiness_roate));
        mRefreshLayout.setRefreshViewHolder(stickinessRefreshViewHolder);

        mClickableLabelTv = (TextView) findViewById(R.id.tv_scrollview_clickablelabel);
        mClickableLabelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScrollViewDemoActivity.this, "点击了测试文本", Toast.LENGTH_SHORT).show();
            }
        });
    }

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
                mClickableLabelTv.setText("加载最新数据完成");
            }
        }.execute();
    }

}