package cn.bingoogolapple.acvp.uitableview.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BGAViewHolder<M> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected Context mContext;
    protected OnItemClickListener mOnItemClickListener;
    protected OnItemLongClickListener mOnItemLongClickListener;

    public BGAViewHolder(View itemView, OnItemClickListener onItemClickListener, OnItemLongClickListener onItemLongClickListener) {
        super(itemView);
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
            onClickChild(v);
        }
    }

    protected void onClickChild(View v) {
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnItemLongClickListener) {
            return mOnItemLongClickListener.onItemLongClick(v, getPosition());
        } else {
            return onLongClickChild(v);
        }
    }

    protected boolean onLongClickChild(View v) {
        return false;
    }

    public abstract void fillData(M item, int position);
}