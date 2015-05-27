package cn.bingoogolapple.acvp.refreshlayout.adapter;

import android.content.Context;

import cn.bingoogolapple.acvp.refreshlayout.R;
import cn.bingoogolapple.acvp.refreshlayout.mode.RefreshModel;
import cn.bingoogolapple.acvp.refreshlayout.widget.BGASwipeItemLayout;
import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewHolder;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:39
 * 描述:
 */
public class BGASwipeAdapterViewAdapter extends BGAAdapterViewAdapter<RefreshModel> {
    private BGASwipeItemLayout mOpenedSil;

    public BGASwipeAdapterViewAdapter(Context context) {
        super(context, R.layout.item_bgaswipe);
    }

    @Override
    protected void setChildListener(BGAAdapterViewHolder viewHolder) {
        BGASwipeItemLayout swipeItemLayout = viewHolder.getViewHolderHelper().getView(R.id.sil_item_bgaswipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil = swipeItemLayout;
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                mOpenedSil = null;
            }
        });
        viewHolder.setItemChildClickListener(R.id.tv_item_bgaswipe_delete);
        viewHolder.setItemChildLongClickListener(R.id.tv_item_bgaswipe_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, RefreshModel model, int position) {
        closeOpenedSwipeItemLayoutWithAnim();
        viewHolderHelper.setText(R.id.tv_item_bgaswipe_title, model.mTitle).setText(R.id.tv_item_bgaswipe_detail, model.mDetail).setText(R.id.et_item_bgaswipe_title, model.mTitle);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        if (mOpenedSil != null) {
            mOpenedSil.closeWithAnim();
            mOpenedSil = null;
        }
    }

    public void closeOpenedSwipeItemLayout() {
        if (mOpenedSil != null) {
            mOpenedSil.close();
            mOpenedSil = null;
        }
    }
}