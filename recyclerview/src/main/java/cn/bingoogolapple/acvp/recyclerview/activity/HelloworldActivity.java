package cn.bingoogolapple.acvp.recyclerview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.acvp.recyclerview.mode.Mode;
import cn.bingoogolapple.acvp.recyclerview.widget.BGAEmptyView;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewAdapter;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewHolder;
import cn.bingoogolapple.acvp.recyclerview.widget.HorizontalDotDivider;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemClickListener;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemLongClickListener;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_helloworld)
public class HelloworldActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = HelloworldActivity.class.getSimpleName();
    @BGAAView(R.id.ev_helloworld_root)
    private BGAEmptyView mRootEv;
    @BGAAView(R.id.srl_helloworld_container)
    private SwipeRefreshLayout mContainerSrl;
    @BGAAView(R.id.rv_helloworld_data)
    private RecyclerView mDataRv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<Mode> mDatas;
    private int mSelectedItemIndex = -1;
    private View mSelectedItemView;

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
        mDataRv.addItemDecoration(new HorizontalDotDivider(this));
        mItemModeAdapter = new ItemModeAdapter(this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDatas = Mode.getHelloworldDatas();
        mItemModeAdapter.setDatas(mDatas);
        mDataRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                mSelectedItemIndex = -1;
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        Log.i(TAG, "开始拖拽");
                        break;
                    case RecyclerView.SCROLL_STATE_IDLE:
                        Log.i(TAG, "停止滚动");
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        Log.i(TAG, "开始飞");
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                Log.i(TAG, "dx = " + dx + "   dy = " + dy);
                resetDeleteItemStatus();
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_helloworld_add:
                add();
                break;
            case R.id.btn_helloworld_delete:
                delete();
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
        resetDeleteItemStatus();
        mSelectedItemIndex = position;
        mSelectedItemView = v;
        mSelectedItemView.setBackgroundColor(Color.RED);
        return true;
    }

    @Override
    public void onRefresh() {
        resetDeleteItemStatus();
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
        resetDeleteItemStatus();
    }

    private void resetDeleteItemStatus() {
        mSelectedItemIndex = -1;
        if (mSelectedItemView != null) {
            mSelectedItemView.setBackgroundColor(Color.WHITE);
            mSelectedItemView = null;
        }
    }

    private void delete() {
        if (mSelectedItemIndex > -1 && mSelectedItemIndex < mDatas.size()) {
            mDatas.remove(mSelectedItemIndex);
            resetDeleteItemStatus();
            if (mDatas.size() == 0) {
                mRootEv.showEmptyView();
            }
            mItemModeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (mSelectedItemIndex > -1) {
            resetDeleteItemStatus();
        } else {
            super.onBackPressed();
        }
    }

    private static class ItemModeAdapter extends BGARecyclerViewAdapter<Mode> {
        public ItemModeAdapter(HelloworldActivity helloworldActivity) {
            super(helloworldActivity, helloworldActivity);
        }

        @Override
        public int getLayoutId() {
            return R.layout.item_helloworld;
        }

        @Override
        public void fillData(BGARecyclerViewHolder viewHolder, int position, Mode item) {
            viewHolder.setText(R.id.tv_item_helloworld_attr1, item.attr1).setText(R.id.tv_item_helloworld_attr2, item.attr2);
        }
    }
}