package cn.bingoogolapple.acvp.refreshrecyclerview.adapter;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;
import cn.bingoogolapple.acvp.refreshrecyclerview.activity.MainActivity;
import cn.bingoogolapple.acvp.refreshrecyclerview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.BGARecyclerViewAdapter;
import cn.bingoogolapple.acvp.refreshrecyclerview.util.BGARecyclerViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 17:48
 * 描述:
 */
public class RefreshModeAdapter extends BGARecyclerViewAdapter<RefreshModel> {
    public RefreshModeAdapter(MainActivity mainActivity) {
        super(mainActivity, mainActivity);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_list;
    }

    @Override
    public void fillData(BGARecyclerViewHolder viewHolder, int position, RefreshModel model) {
        viewHolder.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
    }
}