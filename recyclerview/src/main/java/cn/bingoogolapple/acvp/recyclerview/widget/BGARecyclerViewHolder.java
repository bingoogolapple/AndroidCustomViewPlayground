package cn.bingoogolapple.acvp.recyclerview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class BGARecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected Context mContext;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;
    protected OnItemChildClickListener mOnItemChildClickListener;
    protected final SparseArray<View> mViews;

    public BGARecyclerViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
        mViews = new SparseArray<View>();
        mContext = itemView.getContext();
        mOnItemClickListener = onItemClickListener;
        mOnItemLongClickListener = onItemLongClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnItemClickListener) {
            mOnItemClickListener.onItemClick(v, getPosition());
        } else {
            if (mOnItemChildClickListener != null) {
                mOnItemChildClickListener.onItemChildClick(v, getPosition());
            }
        }
    }

    public void setOnItemChildClickListener(OnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnClickListener(int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnItemLongClickListener) {
            return mOnItemLongClickListener.onItemLongClick(v, getPosition());
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

    /**
     * 为ImageView设置图片
     *
     * @param viewId
     * @param drawableId
     * @return
     */
    public BGARecyclerViewHolder setImageResource(int viewId, int drawableId) {
        ImageView view = getView(viewId);
        view.setImageResource(drawableId);
        return this;
    }

}