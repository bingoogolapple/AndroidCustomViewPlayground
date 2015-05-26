package cn.bingoogolapple.androidcommon.adapterview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:05
 * 描述:
 */
public abstract class BGAAdapterViewAdapter<M> extends BaseAdapter {
    protected final int mItemLayoutId;
    protected Context mContext;
    protected List<M> mDatas;
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;

    public BGAAdapterViewAdapter(Context context, int itemLayoutId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public M getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final BGAAdapterViewHolder viewHolder = BGAAdapterViewHolder.dequeueReusableAdapterViewHolder(mContext, convertView, parent, mItemLayoutId);
        viewHolder.setPosition(position);
        setChildListener(viewHolder);

        fillData(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();
    }

    protected abstract void setChildListener(BGAAdapterViewHolder viewHolder);

    protected abstract void fillData(BGAAdapterViewHolder viewHolder, M model, int position);

    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
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
        notifyDataSetChanged();
    }

    public void removeItem(M model) {
        mDatas.remove(model);
        notifyDataSetChanged();
    }

    public void addItem(int position, M model) {
        mDatas.add(position, model);
        notifyDataSetChanged();
    }

}