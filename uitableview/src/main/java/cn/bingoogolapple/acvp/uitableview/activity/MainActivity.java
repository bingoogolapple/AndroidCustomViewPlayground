package cn.bingoogolapple.acvp.uitableview.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.uitableview.R;
import cn.bingoogolapple.acvp.uitableview.mode.ContentMode;
import cn.bingoogolapple.acvp.uitableview.widget.BGAViewHolder;
import cn.bingoogolapple.acvp.uitableview.widget.ItemDivider;
import cn.bingoogolapple.acvp.uitableview.widget.OnItemClickListener;
import cn.bingoogolapple.acvp.uitableview.widget.OnItemLongClickListener;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BGAAView(R.id.rv_main_data)
    private RecyclerView mDataRv;
    @BGAAView(R.id.tv_main_title)
    private TextView mHeadindexTitleTv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<ContentMode> mDatas1;
    private List<ContentMode> mDatas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataRv.setLayoutManager(mLinearLayoutManager);
        mDataRv.addItemDecoration(new ItemDivider(this, R.mipmap.list_divider));
        mItemModeAdapter = new ItemModeAdapter(this, this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDatas1 = ContentMode.getHeadindexDatas1();
        mDatas2 = ContentMode.getHeadindexDatas2();
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

    private static class ItemModeViewHolder extends BGAViewHolder<ContentMode> {
        @BGAAView(R.id.tv_item_main_content_attr1)
        private TextView mAttr1;
        @BGAAView(R.id.tv_item_main_content_attr2)
        private TextView mAttr2;

        public ItemModeViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
            BGAA.injectViewField2Obj(this, itemView);
        }

        @Override
        public void fillData(ContentMode item, int position) {
            mAttr1.setText(item.attr1);
            mAttr2.setText(item.attr2);
        }
    }

    private static class ItemTitleViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTv;

        public ItemTitleViewHolder(View itemView) {
            super(itemView);
            mTitleTv = (TextView) itemView;
        }

        public void setTitle(String title) {
            mTitleTv.setText(title);
        }

        public void setTitle(int titleResId) {
            mTitleTv.setText(titleResId);
        }
    }


    private static class ItemModeAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_ITEM = 1000;
        private static final int VIEW_TYPE_TITLE1 = 1001;
        private static final int VIEW_TYPE_TITLE2 = 1002;

        protected OnItemClickListener mOnItemClickListener;
        protected OnItemLongClickListener mOnItemLongClickListener;
        protected List<ContentMode> mDatas1;
        protected List<ContentMode> mDatas2;

        public ItemModeAdapter(OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            mOnItemClickListener = onItemClickListener;
            mOnItemLongClickListener = onItemLongClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM) {
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_content, parent, false);
                return new ItemModeViewHolder(itemView, mOnItemClickListener, mOnItemLongClickListener);
            } else if (viewType == VIEW_TYPE_TITLE1 || viewType == VIEW_TYPE_TITLE2) {
                View itemTitle = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_title, parent, false);
                return new ItemTitleViewHolder(itemTitle);
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

        private ContentMode getItem(int position) {
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
            int itemType = getItemViewType(position);
            if (itemType == VIEW_TYPE_TITLE1) {
                ItemTitleViewHolder itemTitleViewHolder = (ItemTitleViewHolder) holder;
                itemTitleViewHolder.setTitle("类型1");
            } else if (itemType == VIEW_TYPE_TITLE2) {
                ItemTitleViewHolder itemTitleViewHolder = (ItemTitleViewHolder) holder;
                itemTitleViewHolder.setTitle("类型2");
            } else if (itemType == VIEW_TYPE_ITEM) {
                ItemModeViewHolder itemModeViewHolder = (ItemModeViewHolder) holder;
                itemModeViewHolder.fillData(getItem(position), position);
            }
        }

        public void setDatas(List<ContentMode> datas1, List<ContentMode> datas2) {
            mDatas1 = datas1;
            mDatas2 = datas2;
            notifyDataSetChanged();
        }
    }

}