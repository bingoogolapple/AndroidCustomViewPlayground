package cn.bingoogolapple.acvp.refreshlistview.adapter;

import android.content.Context;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.acvp.refreshlistview.util.BGAAdapterViewAdapter;
import cn.bingoogolapple.acvp.refreshlistview.util.BGAAdapterViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class AdapterViewAdapter extends BGAAdapterViewAdapter<String> {

    public AdapterViewAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.item_list);
    }

    @Override
    public void convert(BGAAdapterViewHolder viewHolder, String mode) {
        viewHolder.setText(R.id.tv_item_list_name, mode);
    }
}