package cn.bingoogolapple.androidcommon.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 15:01
 * 描述:
 */
public abstract class BGARecyclerViewSwipeAdapter<M> extends RecyclerSwipeAdapter<BGARecyclerViewHolder> {
    protected final int mItemLayoutId;
    protected final int mSwipeViewId;
    protected Context mContext;
    protected List<M> mDatas;
    private BGAOnRVItemClickListener mOnRVItemClickListener;
    private BGAOnRVItemLongClickListener mOnRVItemLongClickListener;
    protected BGAOnRVItemChildClickListener mOnRVItemChildClickListener;
    protected BGAOnRVItemChildLongClickListener mOnRVItemChildLongClickListener;

    public BGARecyclerViewSwipeAdapter(Context context, int itemLayoutId, int swipeViewId) {
        mContext = context;
        mItemLayoutId = itemLayoutId;
        mSwipeViewId = swipeViewId;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return mSwipeViewId;
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecyclerViewHolder viewHolder = new BGARecyclerViewHolder(LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false), mOnRVItemClickListener, mOnRVItemLongClickListener);
        viewHolder.setOnRVItemChildClickListener(mOnRVItemChildClickListener);
        viewHolder.setOnRVItemChildLongClickListener(mOnRVItemChildLongClickListener);
        setListener(viewHolder);
        return viewHolder;
    }

    protected abstract void setListener(BGARecyclerViewHolder viewHolder);

    @Override
    public void onBindViewHolder(BGARecyclerViewHolder viewHolder, int position) {
        fillData(viewHolder, position, getItemMode(position));
    }

    public abstract void fillData(BGARecyclerViewHolder viewHolder, int position, M model);

    public void setOnRVItemClickListener(BGAOnRVItemClickListener onRVItemClickListener) {
        mOnRVItemClickListener = onRVItemClickListener;
    }

    public void setOnRVItemLongClickListener(BGAOnRVItemLongClickListener onRVItemLongClickListener) {
        mOnRVItemLongClickListener = onRVItemLongClickListener;
    }

    public void setOnRVItemChildClickListener(BGAOnRVItemChildClickListener onRVItemChildClickListener) {
        mOnRVItemChildClickListener = onRVItemChildClickListener;
    }

    public void setOnRVItemChildLongClickListener(BGAOnRVItemChildLongClickListener onRVItemChildLongClickListener) {
        mOnRVItemChildLongClickListener = onRVItemChildLongClickListener;
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
        closeItem(position);
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(M model) {
        removeItem(mDatas.indexOf(model));
    }

    public void addItem(int position, M model) {
        mDatas.add(position, model);
        notifyItemInserted(position);
    }
}