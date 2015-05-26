package cn.bingoogolapple.androidcommon.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * @param <M> 适配的数据类型
 */
public abstract class BGARecyclerViewAdapter<M> extends RecyclerView.Adapter<BGARecyclerViewHolder> {
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<M> mDatas;
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;
    private BGAOnRVItemClickListener mOnRVItemClickListener;
    private BGAOnRVItemLongClickListener mOnRVItemLongClickListener;

    public BGARecyclerViewAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecyclerViewHolder viewHolder = new BGARecyclerViewHolder(LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false), mOnRVItemClickListener, mOnRVItemLongClickListener);
        viewHolder.setOnItemChildClickListener(mOnItemChildClickListener);
        viewHolder.setOnItemChildLongClickListener(mOnItemChildLongClickListener);
        setListener(viewHolder);
        return viewHolder;
    }

    protected abstract void setListener(BGARecyclerViewHolder viewHolder);

    @Override
    public void onBindViewHolder(BGARecyclerViewHolder viewHolder, int position) {
        fillData(viewHolder, position, getItemMode(position));
    }

    protected abstract void fillData(BGARecyclerViewHolder viewHolder, int position, M model);

    public void setOnRVItemClickListener(BGAOnRVItemClickListener onRVItemClickListener) {
        mOnRVItemClickListener = onRVItemClickListener;
    }

    public void setOnRVItemLongClickListener(BGAOnRVItemLongClickListener onRVItemLongClickListener) {
        mOnRVItemLongClickListener = onRVItemLongClickListener;
    }

    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public M getItemMode(int position) {
        return mDatas.get(position);
    }

    public void setDatas(List<M> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<M> datas) {
        if (mDatas == null) {
            mDatas = datas;
        } else {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(M model) {
        mDatas.remove(model);
        notifyDataSetChanged();
    }

    public void addItem(int position, M model) {
        mDatas.add(position, model);
        notifyItemInserted(position);
    }
}