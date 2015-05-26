package cn.bingoogolapple.androidcommon.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

public class BGARecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected final SparseArray<View> mViews = new SparseArray<>();
    protected Context mContext;
    protected BGAOnRVItemClickListener mOnRVItemClickListener;
    protected BGAOnRVItemLongClickListener mOnRVItemLongClickListener;
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;

    public BGARecyclerViewHolder(View itemView, BGAOnRVItemClickListener onRVItemClickListener, BGAOnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        mContext = itemView.getContext();
        mOnRVItemClickListener = onRVItemClickListener;
        mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setItemChildClickListener(int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public void setItemChildLongClickListener(int viewId) {
        getView(viewId).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemClickListener) {
            mOnRVItemClickListener.onRVItemClick(v, getAdapterPosition());
        } else if (mOnItemChildClickListener != null) {
            mOnItemChildClickListener.onItemChildClick(v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemLongClickListener) {
            return mOnRVItemLongClickListener.onRVItemLongClick(v, getAdapterPosition());
        } else if (mOnItemChildLongClickListener != null) {
            return mOnItemChildLongClickListener.onItemChildLongClick(v, getAdapterPosition());
        }
        return false;
    }

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {

        View view = mViews.get(viewId);
        if (view == null) {
            view = this.itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BGARecyclerViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

}