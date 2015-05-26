package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.recyclerview.BGARecyclerViewHolder;
import cn.bingoogolapple.androidcommon.recyclerview.DMJRecyclerViewSwipeAdapter;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/22 16:24
 * 描述:
 */
public class DMJSwipeRecyclerViewAdapter extends DMJRecyclerViewSwipeAdapter<RefreshModel> {

    public DMJSwipeRecyclerViewAdapter(Context context) {
        super(context, R.layout.item_dmjswipe, R.id.sl_item_dmjswipe_root);
    }

    @Override
    protected void setListener(BGARecyclerViewHolder viewHolder) {
        viewHolder.setItemChildClickListener(R.id.tv_item_dmjswipe_delete);
    }

    @Override
    public void fillData(BGARecyclerViewHolder viewHolder, int position, RefreshModel model) {
        viewHolder.setText(R.id.tv_item_dmjswipe_title, model.mTitle).setText(R.id.tv_item_dmjswipe_detail, model.mDetail);
    }
}