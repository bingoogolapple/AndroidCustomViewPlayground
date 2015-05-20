package cn.bingoogolapple.acvp.refreshlistview.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:07
 * 描述:
 */
public abstract class BGASwipeViewAdapter<T> extends BaseSwipeAdapter {
    protected LayoutInflater mInflater;
    protected List<T> mDatas;
    protected Context mContext;
    protected final int mItemLayoutId;
    protected final int mSwipeViewId;

    public BGASwipeViewAdapter(Context context, List<T> datas, int itemLayoutId, int swipeViewId) {
        mDatas = datas;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItemLayoutId = itemLayoutId;
        mSwipeViewId = swipeViewId;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return mSwipeViewId;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setDatas(List<T> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (mDatas == null) {
            mDatas = datas;
        } else {
            mDatas.addAll(datas);
        }
        notifyDataSetChanged();
    }

    public void remove(T mode) {
        mDatas.remove(mode);
        notifyDataSetChanged();
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        View convertView = mInflater.inflate(mItemLayoutId, null);
        setListener(new BGASwipeViewHolder(convertView));
        return convertView;
    }

    @Override
    public void fillValues(int position, View convertView) {
        fillData((BGASwipeViewHolder) convertView.getTag(), mDatas.get(position));
    }

    protected abstract void setListener(BGASwipeViewHolder viewHolder);

    protected abstract void fillData(BGASwipeViewHolder viewHolder, T mode);

}
