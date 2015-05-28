package cn.bingoogolapple.acvp.refreshlistview.adapter;

import android.content.Context;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

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
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, RefreshModel model, int position) {
        viewHolderHelper.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
    }
}