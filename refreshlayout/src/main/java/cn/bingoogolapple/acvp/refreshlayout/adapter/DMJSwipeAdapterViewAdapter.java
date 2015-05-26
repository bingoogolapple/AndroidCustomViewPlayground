package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.androidcommon.adapterview.DMJAdapterViewSwipeAdapter;
import cn.bingoogolapple.androidcommon.adapterview.DMJAdapterViewSwipeHolder;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:41
 * 描述:
 */
public class DMJSwipeAdapterViewAdapter extends DMJAdapterViewSwipeAdapter<RefreshModel> {
    private View.OnClickListener mOnClickListener;

    public DMJSwipeAdapterViewAdapter(Context context, View.OnClickListener onClickListener) {
        super(context, R.layout.item_dmjswipe, R.id.sl_item_dmjswipe_root);
        mOnClickListener = onClickListener;
    }

    @Override
    protected void setListener(DMJAdapterViewSwipeHolder viewHolder) {
        if (mOnClickListener != null) {
            viewHolder.getView(R.id.tv_item_dmjswipe_delete).setOnClickListener(mOnClickListener);
        }
    }

    @Override
    protected void fillData(DMJAdapterViewSwipeHolder viewHolder, RefreshModel model, int position) {
        viewHolder.getView(R.id.tv_item_dmjswipe_delete).setTag(model);

        viewHolder.setText(R.id.tv_item_dmjswipe_title, model.mTitle).setText(R.id.tv_item_dmjswipe_detail, model.mDetail);
    }

}