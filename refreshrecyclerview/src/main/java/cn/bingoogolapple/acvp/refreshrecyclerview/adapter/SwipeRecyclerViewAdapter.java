package cn.bingoogolapple.acvp.refreshrecyclerview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import java.util.List;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;
import cn.bingoogolapple.acvp.refreshrecyclerview.activity.SwipeRecyclerViewDemoActivity;
import cn.bingoogolapple.acvp.refreshrecyclerview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.BGARecyclerViewHolder;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.OnItemChildClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 10:43
 * 描述:
 */
public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<BGARecyclerViewHolder> implements OnItemChildClickListener {
    private SwipeRecyclerViewDemoActivity mSwipeRecyclerViewDemoActivity;
    private List<RefreshModel> mDatas;

    public SwipeRecyclerViewAdapter(SwipeRecyclerViewDemoActivity swipeRecyclerViewDemoActivity) {
        mSwipeRecyclerViewDemoActivity = swipeRecyclerViewDemoActivity;
    }

    @Override
    public BGARecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BGARecyclerViewHolder viewHolder = new BGARecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipelist, parent, false), mSwipeRecyclerViewDemoActivity, mSwipeRecyclerViewDemoActivity);
        setListener(viewHolder);
        return viewHolder;
    }

    private void setListener(BGARecyclerViewHolder viewHolder) {
        viewHolder.setOnItemChildClickListener(this);
        viewHolder.setOnClickListener(R.id.tv_item_swipelist_delete);
    }

    @Override
    public void onBindViewHolder(final BGARecyclerViewHolder viewHolder, final int position) {
        RefreshModel refreshModel = mDatas.get(position);
        viewHolder.setText(R.id.tv_item_swipelist_title, refreshModel.mTitle).setText(R.id.tv_item_swipelist_detail, refreshModel.mDetail);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.sl_item_swipelist_root;
    }

    @Override
    public int getItemCount() {
        return null == mDatas ? 0 : mDatas.size();
    }

    public void setDatas(List<RefreshModel> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, RefreshModel m) {
        mDatas.add(position, m);
        notifyItemInserted(position);
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_swipelist_delete) {
            removeItem(position);
        }
    }
}