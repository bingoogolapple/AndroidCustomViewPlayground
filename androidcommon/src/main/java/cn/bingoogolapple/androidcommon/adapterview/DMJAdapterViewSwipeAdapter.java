package cn.bingoogolapple.androidcommon.adapterview;

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
public abstract class DMJAdapterViewSwipeAdapter<M> extends BaseSwipeAdapter {
    protected final int mItemLayoutId;
    protected final int mSwipeViewId;
    protected Context mContext;
    protected List<M> mDatas;

    public DMJAdapterViewSwipeAdapter(Context context, int itemLayoutId, int swipeViewId) {
        mContext = context;
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
    public M getItem(int position) {
        return mDatas == null ? null : mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View generateView(int position, ViewGroup viewGroup) {
        View convertView = LayoutInflater.from(mContext).inflate(mItemLayoutId, viewGroup, false);
        setListener(new DMJAdapterViewSwipeHolder(convertView));
        return convertView;
    }

    protected abstract void setListener(DMJAdapterViewSwipeHolder viewHolder);

    @Override
    public void fillValues(int position, View convertView) {
        DMJAdapterViewSwipeHolder viewHolder = (DMJAdapterViewSwipeHolder) convertView.getTag();
        viewHolder.setPosition(position);
        fillData(viewHolder, mDatas.get(position), position);
    }

    protected abstract void fillData(DMJAdapterViewSwipeHolder viewHolder, M model, int position);

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
        closeItem(position);
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void removeItem(M model) {
        removeItem(mDatas.indexOf(model));
    }

    public void addItem(int position, M model) {
        mDatas.add(position, model);
        notifyDataSetChanged();
    }
}