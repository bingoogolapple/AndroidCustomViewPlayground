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

@BGAALayout(R.layout.activity_download)
public class DownloadActivity extends AppCompatActivity implements OnItemClickListener, OnItemLongClickListener {
    private static final String TAG = DownloadActivity.class.getSimpleName();
    @BGAAView(R.id.rv_download_data)
    private RecyclerView mDataRv;
    @BGAAView(R.id.tv_download_title)
    private TextView mHeadindexTitleTv;

    private ItemModeAdapter mItemModeAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    private List<Mode> mDownloadingDatas;
    private List<Mode> mDownloadedDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        mDataRv.setLayoutManager(mLinearLayoutManager);
        mDataRv.addItemDecoration(new HorizontalDotDivider(this));
        mItemModeAdapter = new ItemModeAdapter(this, this);
        mDataRv.setAdapter(mItemModeAdapter);

        mDownloadingDatas = Mode.getDownloadingDatas();
        mDownloadedDatas = Mode.getDownloadedDatas();
        mItemModeAdapter.setDatas(mDownloadingDatas, mDownloadedDatas);

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

    private static class DownloadingViewHolder extends BGARecyclerViewHolder {
        public DownloadingViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
            setOnClickListener(R.id.btn_item_downloading_pause);
        }

    }

    private static class DownloadedViewHolder extends BGARecyclerViewHolder {
        public DownloadedViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
            setOnClickListener(R.id.btn_item_downloaded_delete);
        }
    }

    private static class DownloadTitleViewHolder extends BGARecyclerViewHolder {
        public DownloadTitleViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            super(itemView, onItemClickListener, onItemLongClickListener);
        }
    }

    private static class ItemModeAdapter extends RecyclerView.Adapter {
        private static final int VIEW_TYPE_DOWNLOADING = 1000;
        private static final int VIEW_TYPE_DOWNLOADED = 1001;
        private static final int VIEW_TYPE_DOWNLOADING_TITLE = 1002;
        private static final int VIEW_TYPE_DOWNLOADED_TITLE = 1003;

        protected OnItemClickListener mOnItemClickListener;
        protected OnItemLongClickListener mOnItemLongClickListener;
        protected List<Mode> mDownloadingDatas;
        protected List<Mode> mDownloadedDatas;

        public ItemModeAdapter(OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
            mOnItemClickListener = onItemClickListener;
            mOnItemLongClickListener = onItemLongClickListener;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case VIEW_TYPE_DOWNLOADING:
                    View downloadingView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloading, parent, false);
                    return new DownloadingViewHolder(downloadingView, mOnItemClickListener, mOnItemLongClickListener);
                case VIEW_TYPE_DOWNLOADED:
                    View downloadedView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloaded, parent, false);
                    return new DownloadedViewHolder(downloadedView, mOnItemClickListener, mOnItemLongClickListener);
                case VIEW_TYPE_DOWNLOADING_TITLE:
                case VIEW_TYPE_DOWNLOADED_TITLE:
                    View itemTitle = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_downloadtitle, parent, false);
                    return new DownloadTitleViewHolder(itemTitle, null, null);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (getItemViewType(position)) {
                case VIEW_TYPE_DOWNLOADING_TITLE:
                    DownloadTitleViewHolder downloadingTitleViewHolder = (DownloadTitleViewHolder) holder;
                    downloadingTitleViewHolder.setText(R.id.tv_downloadtitle_title, "正在下载");
                    break;
                case VIEW_TYPE_DOWNLOADING:
                    Mode downloadingItem = getItem(position);
                    DownloadingViewHolder downloadingViewHolder = (DownloadingViewHolder) holder;
                    downloadingViewHolder.setText(R.id.tv_item_downloading_attr1, downloadingItem.attr1).setText(R.id.tv_item_downloading_attr2, downloadingItem.attr2);
                    break;
                case VIEW_TYPE_DOWNLOADED_TITLE:
                    DownloadTitleViewHolder downloadedTitleViewHolder = (DownloadTitleViewHolder) holder;
                    downloadedTitleViewHolder.setText(R.id.tv_downloadtitle_title, "下载完成");
                    break;
                case VIEW_TYPE_DOWNLOADED:
                    Mode downloadedItem = getItem(position);
                    DownloadedViewHolder downloadedViewHolder = (DownloadedViewHolder) holder;
                    downloadedViewHolder.setText(R.id.tv_item_downloaded_attr1, downloadedItem.attr1).setText(R.id.tv_item_downloaded_attr2, downloadedItem.attr2);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (mDownloadingDatas != null && mDownloadingDatas.size() != 0 && mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                return mDownloadingDatas.size() + mDownloadedDatas.size() + 2;
            } else if (mDownloadingDatas != null && mDownloadingDatas.size() != 0) {
                return mDownloadingDatas.size() + 1;
            } else if (mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                return mDownloadedDatas.size() + 1;
            } else {
                return 0;
            }
        }

        private Mode getItem(int position) {
            if (mDownloadingDatas != null && mDownloadingDatas.size() != 0 && mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                if (position <= mDownloadingDatas.size()) {
                    int newposition = position - 1;
                    return mDownloadingDatas.get(newposition);
                } else {
                    int newposition = position - mDownloadingDatas.size() - 2;
                    return mDownloadedDatas.get(newposition);
                }
            } else if (mDownloadingDatas != null && mDownloadingDatas.size() != 0) {
                int newposition = position - 1;
                return mDownloadingDatas.get(newposition);
            } else if (mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                int newposition = position - 1;
                return mDownloadedDatas.get(newposition);
            } else {
                return null;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mDownloadingDatas != null && mDownloadingDatas.size() != 0 && mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_DOWNLOADING_TITLE;
                } else if (position == mDownloadingDatas.size() + 1) {
                    return VIEW_TYPE_DOWNLOADED_TITLE;
                } else if (position < mDownloadingDatas.size() + 1) {
                    return VIEW_TYPE_DOWNLOADING;
                }
                return VIEW_TYPE_DOWNLOADED;
            } else if (mDownloadingDatas != null && mDownloadingDatas.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_DOWNLOADING_TITLE;
                }
                return VIEW_TYPE_DOWNLOADING;
            } else if (mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                if (position == 0) {
                    return VIEW_TYPE_DOWNLOADED_TITLE;
                }
                return VIEW_TYPE_DOWNLOADED;
            } else {
                return 0;
            }
        }

        public String getHeadindexTitle(int position) {
            if (mDownloadingDatas != null && mDownloadingDatas.size() != 0 && mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                if (position < mDownloadingDatas.size() + 1) {
                    return "正在下载";
                }
                return "下载完成";
            } else if (mDownloadingDatas != null && mDownloadingDatas.size() != 0) {
                return "正在下载";
            } else if (mDownloadedDatas != null && mDownloadedDatas.size() != 0) {
                return "下载完成";
            } else {
                return "";
            }
        }

        public void setDatas(List<Mode> datas1, List<Mode> datas2) {
            mDownloadingDatas = datas1;
            mDownloadedDatas = datas2;
            notifyDataSetChanged();
        }
    }

}