package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.adapterview.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapterview.BGAAdapterViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class AdapterViewAdapter extends BGAAdapterViewAdapter<RefreshModel> {

    public AdapterViewAdapter(Context context) {
        super(context, R.layout.item_list);
    }

    @Override
    public void fillData(BGAAdapterViewHolder viewHolder, RefreshModel model, int position) {
        viewHolder.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
    }
}