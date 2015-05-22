package cn.bingoogolapple.acvp.refreshlistview.adapter;

import android.content.Context;
import android.view.View;

import java.util.List;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlistview.util.BGASwipeViewAdapter;
import cn.bingoogolapple.acvp.refreshlistview.util.BGASwipeViewHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:41
 * 描述:
 */
public class SwipeViewAdapter extends BGASwipeViewAdapter<RefreshModel> {
    private View.OnClickListener mOnClickListener;

    public SwipeViewAdapter(Context context, View.OnClickListener onClickListener, List<RefreshModel> datas) {
        super(context, datas, R.layout.item_swipelist, R.id.sl_item_swipelist_root);
        mOnClickListener = onClickListener;
    }

    @Override
    protected void setListener(BGASwipeViewHolder viewHolder) {
        if (mOnClickListener != null) {
            viewHolder.getView(R.id.tv_item_swipelist_delete).setOnClickListener(mOnClickListener);
        }
    }

    @Override
    protected void fillData(BGASwipeViewHolder viewHolder, RefreshModel model) {
        viewHolder.getView(R.id.tv_item_swipelist_delete).setTag(model);

        viewHolder.setText(R.id.tv_item_swipelist_title, model.mTitle).setText(R.id.tv_item_swipelist_detail, model.mDetail);
    }

}