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
public class NormalAdapterViewAdapter extends BGAAdapterViewAdapter<RefreshModel> {

    public NormalAdapterViewAdapter(Context context) {
        super(context, R.layout.item_normal);
    }

    @Override
    protected void setChildListener(BGAAdapterViewHolder viewHolder) {
    }

    @Override
    public void fillData(BGAAdapterViewHolder viewHolder, RefreshModel model, int position) {
        viewHolder.setText(R.id.tv_item_normal_title, model.mTitle).setText(R.id.tv_item_normal_detail, model.mDetail);
    }
}