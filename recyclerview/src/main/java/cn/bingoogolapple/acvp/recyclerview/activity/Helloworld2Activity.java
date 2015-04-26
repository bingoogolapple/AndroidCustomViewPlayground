package cn.bingoogolapple.acvp.recyclerview.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.acvp.recyclerview.mode.Mode;
import cn.bingoogolapple.acvp.recyclerview.widget.BGAEmptyView;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewAdapter;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewHolder;
import cn.bingoogolapple.acvp.recyclerview.widget.ItemDivider;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemChildClickListener;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemClickListener;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemLongClickListener;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_helloworld2)
public class Helloworld2Activity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = Helloworld2Activity.class.getSimpleName();
    @BGAAView(R.id.ev_helloworld2_root)
    private BGAEmptyView mRootEv;
    @BGAAView(R.id.srl_helloworld2_container)
    private SwipeRefreshLayout mContainerSrl;
    @BGAAView(R.id.rv_helloworld2_data)
    private RecyclerView mDataRv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<Mode> mDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        mContainerSrl.setOnRefreshListener(this);
        mContainerSrl.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataRv.setLayoutManager(mLinearLayoutManager);
        mDataRv.addItemDecoration(new ItemDivider(this, R.mipmap.list_divider));
        mItemModeAdapter = new ItemModeAdapter(this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDatas = Mode.getHelloworldDatas();
        mItemModeAdapter.setDatas(mDatas);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_helloworld2_add:
                add();
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目" + mItemModeAdapter.getItemMode(position).attr1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目" + mItemModeAdapter.getItemMode(position).attr1, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                add();
            }
        }, 2000);
    }

    private void add() {
        mDatas.add(new Mode("attr1 " + mDatas.size(), "attr2 " + mDatas.size()));
        mItemModeAdapter.notifyDataSetChanged();
        mContainerSrl.setRefreshing(false);
        mRootEv.showContentView();
    }

    private static class ItemModeAdapter extends BGARecyclerViewAdapter<Mode> {
        private Helloworld2Activity mHelloworld2Activity;
        public ItemModeAdapter(Helloworld2Activity helloworld2Activity) {
            super(helloworld2Activity, helloworld2Activity);
            mHelloworld2Activity = helloworld2Activity;
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_helloworld2;
        }

        @Override
        protected void setListener(BGARecyclerViewHolder viewHolder) {
            viewHolder.setOnClickListener(R.id.btn_item_helloworld2_delete);
            viewHolder.setOnItemChildClickListener(new OnItemChildClickListener() {
                @Override
                public void onItemChildClick(View v, int position) {
                    if (v.getId() == R.id.btn_item_helloworld2_delete) {
                        Toast.makeText(mHelloworld2Activity, "删除" + getItemMode(position).attr1, Toast.LENGTH_SHORT).show();
                        removeItem(position);
                    }
                }
            });
        }

        @Override
        public void fillData(BGARecyclerViewHolder viewHolder, int position, Mode item) {
            viewHolder.setText(R.id.tv_item_helloworld2_attr1, item.attr1).setText(R.id.tv_item_helloworld2_attr2, item.attr2);
        }
    }
}