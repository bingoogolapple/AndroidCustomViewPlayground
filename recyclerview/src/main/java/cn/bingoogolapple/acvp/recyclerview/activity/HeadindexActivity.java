package cn.bingoogolapple.acvp.recyclerview.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.acvp.recyclerview.mode.Mode;
import cn.bingoogolapple.acvp.recyclerview.widget.BGARecyclerViewHolder;
import cn.bingoogolapple.acvp.recyclerview.widget.HorizontalDotDivider;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemClickListener;
import cn.bingoogolapple.acvp.recyclerview.widget.OnItemLongClickListener;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_headindex)
public class HeadindexActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = HeadindexActivity.class.getSimpleName();
    @BGAAView(R.id.rv_headindex_data)
    private RecyclerView mDataRv;
    @BGAAView(R.id.tv_headindex_title)
    private TextView mHeadindexTitleTv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<Mode> mDatas1;
    private List<Mode> mDatas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataRv.setLayoutManager(mLinearLayoutManager);
        mDataRv.addItemDecoration(new HorizontalDotDivider(this));
        mItemModeAdapter = new ItemModeAdapter(this, this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDatas1 = Mode.getHeadindexDatas1();
        mDatas2 = Mode.getHeadindexDatas2();
        mItemModeAdapter.setDatas(mDatas1, mDatas2);

        // 处理标题索引
        if (mItemModeAdapter.getItemCount() == 0) {
            mHeadindexTitleTv.setVisibility(View.GONE);
        } else {
            mHeadindexTitleTv.setVisibility(View.VISIBLE);
            mHeadindexTitleTv.setText(mItemModeAdapter.getHeadindexTitle(0));
        }
        mDataRv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mItemModeAdapter.getItemCount() == 0) {
                    mHeadindexTitleTv.setVisibility(View.GONE);
                } else {
                    mHeadindexTitleTv.setVisibility(View.VISIBLE);
                    int position = mLinearLayoutManager.findFirstVisibleItemPosition();
                    mHeadindexTitleTv.setText(mItemModeAdapter.getHeadindexTitle(position));
                }
            }
        });
    }

    @Override
    public void onItemClick(View v, int position) {
        Toast.makeText(this, "点击了条目" + mItemModeAdapter.getItem(position).attr1, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(View v, int position) {
        Toast.makeText(this, "长按了条目" + mItemModeAdapter.getItem(position).attr1, Toast.LENGTH_SHORT).show();
        return true;
    }

    private static class ItemModeAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_ITEM = 1000;
        private static final int VIEW_TYPE_TITLE1 = 1001;
        private static final int VIEW_TYPE_TITLE2 = 1002;

        protected OnItemClickListener mOnItemClickListener;
        protected OnItemLongClickListener mOnItemLongClickListener;
        protected List<Mode> mDatas1;
        protected List<Mode> mDatas2;

        public ItemModeAdapter(OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            mOnItemClickListener = onItemClickListener;
            mOnItemLongClickListener = onItemLongClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helloworld, parent, false);
                return new BGARecyclerViewHolder(itemView, mOnItemClickListener, mOnItemLongClickListener);
            } else if (viewType == VIEW_TYPE_TITLE1 || viewType == VIEW_TYPE_TITLE2) {
                View itemTitle = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_headindextitle, parent, false);
                return new BGARecyclerViewHolder(itemTitle, null, null);
            }
            return null;
        }

        @Override
        public int getItemCount() {
            if (mDatas1 != null && mDatas1.size() != 0 && mDatas2 != null && mDatas2.size() != 0) {
                return mDatas1.size() + mDatas2.size() + 2;
            } else if (mDatas1 != null && mDatas1.size() != 0) {
                return mDatas1.size() + 1;
            } else if (mDatas2 != null && mDatas2.size() != 0) {
                return mDatas2.size() + 1;
            } else {
                return 0;
            }
        }

        private Mode getItem(int position) {
            if (mDatas1 != null && mDatas1.size() != 0 && mDatas2 != null && mDatas2.size() != 0) {
                if (position <= mDatas1.size()) {
                    int newposition = position - 1;
                    return mDatas1.get(newposition);
                } else {
                    int newposition = position - mDatas1.size() - 2;
                    return mDatas2.get(newposition);
                }
            } else if (mDatas1 != null && mDatas1.size() != 0) {
                int newposition = position - 1;
                return mDatas1.get(newposition);
            } else if (mDatas2 != null && mDatas2.size() != 0) {
                int newposition = position - 1;
                return mDatas2.get(newposition);
            } else {
                return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mDatas1 != null && mDatas1.size() != 0 && mDatas2 != null && mDatas2.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_TITLE1;
                } else if (position == mDatas1.size() + 1) {
                    return VIEW_TYPE_TITLE2;
                }
                return VIEW_TYPE_ITEM;
            } else if (mDatas1 != null && mDatas1.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_TITLE1;
                }
                return VIEW_TYPE_ITEM;
            } else if (mDatas2 != null && mDatas2.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_TITLE2;
                }
                return VIEW_TYPE_ITEM;
            } else {
                return 0;
            }
        }

        public String getHeadindexTitle(int position) {
            if (mDatas1 != null && mDatas1.size() != 0 && mDatas2 != null && mDatas2.size() != 0) {
                if (position < mDatas1.size() + 1) {
                    return "类型1";
                }
                return "类型2";
            } else if (mDatas1 != null && mDatas1.size() != 0) {
                return "类型1";
            } else if (mDatas2 != null && mDatas2.size() != 0) {
                return "类型2";
            } else {
                return "";
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            BGARecyclerViewHolder viewHolder = (BGARecyclerViewHolder) holder;
            int itemType = getItemViewType(position);
            if (itemType == VIEW_TYPE_TITLE1) {
                viewHolder.setText(R.id.tv_item_headindextitle_title, "类型1");
            } else if (itemType == VIEW_TYPE_TITLE2) {
                viewHolder.setText(R.id.tv_item_headindextitle_title, "类型2");
            } else if (itemType == VIEW_TYPE_ITEM) {
                Mode mode = getItem(position);
                viewHolder.setText(R.id.tv_item_helloworld_attr1, mode.attr1).setText(R.id.tv_item_helloworld_attr2, mode.attr2);
            }
        }

        public void setDatas(List<Mode> datas1, List<Mode> datas2) {
            mDatas1 = datas1;
            mDatas2 = datas2;
            notifyDataSetChanged();
        }
    }

}