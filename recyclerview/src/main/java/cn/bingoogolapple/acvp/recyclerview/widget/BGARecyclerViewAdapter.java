package cn.bingoogolapple.acvp.recyclerview.widget;

import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * @param <M>  适配的数据类型
 * @param <VH> ViewHolder的类型
 */
public abstract class BGARecyclerViewAdapter<M, VH extends BGAViewHolder> extends RecyclerView.Adapter<VH> {
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

    public M getItemMode(int position) {
        return mDatas.get(position);
    }

    @Override
    public void onBindViewHolder(VH viewHolder, int position) {
        viewHolder.fillData(mDatas.get(position), position);
    }

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