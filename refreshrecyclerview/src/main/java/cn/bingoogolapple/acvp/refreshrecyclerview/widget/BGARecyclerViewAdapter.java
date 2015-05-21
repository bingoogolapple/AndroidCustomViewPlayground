package cn.bingoogolapple.acvp.refreshrecyclerview.widget;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * @param <M> 适配的数据类型
 */
public abstract class BGARecyclerViewAdapter<M> extends RecyclerView.Adapter<BGARecyclerViewHolder> {
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected List<M> mDatas;

    public BGARecyclerViewAdapter(List<M> datas) {
        this(datas, null);
    }

    public BGARecyclerViewAdapter(OnItemClickListener onItemClickListener) {
        this(null, onItemClickListener);
    }

    public BGARecyclerViewAdapter(List<M> datas, OnItemClickListener onItemClickListener) {
        this(datas, onItemClickListener, null);
    }

    public BGARecyclerViewAdapter(OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        this(null, onItemClickListener, onItemLongClickListener);
    }

    public BGARecyclerViewAdapter(List<M> datas, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        mDatas = datas;
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecyclerViewHolder viewHolder = new BGARecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayoutId(), parent, false), mOnItemClickListener, mOnItemLongClickListener);
        setListener(viewHolder);
        return viewHolder;
    }

    protected void setListener(BGARecyclerViewHolder viewHolder) {
    }

    public abstract int getLayoutId();

    public M getItemMode(int position) {
        return mDatas.get(position);
    }

    @Override
    public void onBindViewHolder(BGARecyclerViewHolder viewHolder, int position) {
        fillData(viewHolder, position, getItemMode(position));
    }

    public abstract void fillData(BGARecyclerViewHolder viewHolder, int position, M item);

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    public void setDatas(List<M> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, M m) {
        mDatas.add(position, m);
        notifyItemInserted(position);
    }

}