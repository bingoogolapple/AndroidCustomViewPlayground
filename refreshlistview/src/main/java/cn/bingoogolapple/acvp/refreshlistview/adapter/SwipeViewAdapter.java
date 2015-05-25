package cn.bingoogolapple.acvp.refreshlistview.adapter;

import android.content.Context;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlistview.R;
import cn.bingoogolapple.acvp.refreshlistview.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.adapterview.DMJAdapterViewSwipeAdapter;
import cn.bingoogolapple.androidcommon.adapterview.DMJAdapterViewSwipeHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:41
 * 描述:
 */
public class SwipeViewAdapter extends DMJAdapterViewSwipeAdapter<RefreshModel> {
    private View.OnClickListener mOnClickListener;

    public SwipeViewAdapter(Context context, View.OnClickListener onClickListener) {
        super(context, R.layout.item_swipelist, R.id.sl_item_swipelist_root);
        mOnClickListener = onClickListener;
    }

    @Override
    protected void setListener(DMJAdapterViewSwipeHolder viewHolder) {
        if (mOnClickListener != null) {
            viewHolder.getView(R.id.tv_item_swipelist_delete).setOnClickListener(mOnClickListener);
        }
    }

    @Override
    protected void fillData(DMJAdapterViewSwipeHolder viewHolder, RefreshModel model, int position) {
        viewHolder.getView(R.id.tv_item_swipelist_delete).setTag(model);

        viewHolder.setText(R.id.tv_item_swipelist_title, model.mTitle).setText(R.id.tv_item_swipelist_detail, model.mDetail);
    }

}