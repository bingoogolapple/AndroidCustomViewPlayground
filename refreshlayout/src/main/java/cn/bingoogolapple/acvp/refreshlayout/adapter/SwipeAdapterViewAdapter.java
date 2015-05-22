package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.adapterview.BGAAdapterViewSwipeAdapter;
import cn.bingoogolapple.androidcommon.adapterview.BGAAdapterViewSwipeHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:41
 * 描述:
 */
public class SwipeAdapterViewAdapter extends BGAAdapterViewSwipeAdapter<RefreshModel> {
    private View.OnClickListener mOnClickListener;

    public SwipeAdapterViewAdapter(Context context, View.OnClickListener onClickListener) {
        super(context, R.layout.item_swipelist, R.id.sl_item_swipelist_root);
        mOnClickListener = onClickListener;
    }

    @Override
    protected void setListener(BGAAdapterViewSwipeHolder viewHolder) {
        if (mOnClickListener != null) {
            viewHolder.getView(R.id.tv_item_swipelist_delete).setOnClickListener(mOnClickListener);
        }
    }

    @Override
    protected void fillData(BGAAdapterViewSwipeHolder viewHolder, RefreshModel model, int position) {
        viewHolder.getView(R.id.tv_item_swipelist_delete).setTag(model);

        viewHolder.setText(R.id.tv_item_swipelist_title, model.mTitle).setText(R.id.tv_item_swipelist_detail, model.mDetail);
    }

}