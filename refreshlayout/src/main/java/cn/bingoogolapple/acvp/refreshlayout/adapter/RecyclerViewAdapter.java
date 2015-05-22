package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.recyclerview.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.recyclerview.BGARecyclerViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 16:31
 * 描述:
 */
public class RecyclerViewAdapter extends BGARecyclerViewAdapter<RefreshModel> {
    public RecyclerViewAdapter(Context context) {
        super(context, R.layout.item_list);
    }

    @Override
    public void setListener(BGARecyclerViewHolder viewHolder) {
    }

    @Override
    public void fillData(BGARecyclerViewHolder viewHolder, int position, RefreshModel model) {
        viewHolder.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
    }
}