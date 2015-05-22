package cn.bingoogolapple.acvp.refreshlistview.adapter;

import android.content.Context;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlistview.util.BGAAdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlistview.util.BGAAdapterViewHolder;
import cn.bingoogolapple.acvp.refreshlistview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class AdapterViewAdapter extends BGAAdapterViewAdapter<RefreshModel> {

    public AdapterViewAdapter(Context context, List<RefreshModel> datas) {
        super(context, datas, R.layout.item_list);
    }

    @Override
    public void convert(BGAAdapterViewHolder viewHolder, RefreshModel model) {
        viewHolder.setText(R.id.tv_item_list_title, model.mTitle).setText(R.id.tv_item_list_detail, model.mDetail);
    }
}