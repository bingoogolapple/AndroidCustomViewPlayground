package cn.bingoogolapple.acvp.refreshlayout.widget;

import android.content.Context;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlayout.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 13:16
 * 描述:
 */
public class BGAStickinessRefreshViewHolder extends BGARefreshViewHolder {
    private StickinessRefreshView mStickinessRefreshView;

    public BGAStickinessRefreshViewHolder(Context context) {
        super(context);
    }

    @Override
    public View getLoadMoreFooterView() {
        return null;
    }

    @Override
    public View getRefreshHeaderView() {
        if (mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header_stickiness, null);
            mStickinessRefreshView = (StickinessRefreshView) mRefreshHeaderView.findViewById(R.id.stickinessRefreshView);
        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale, int moveYDistance) {
        mStickinessRefreshView.setMoveYDistance((int)(moveYDistance * getPaddingTopScale()));
    }

    @Override
    public void changeToIdle() {
        mStickinessRefreshView.smoothToIdle();
    }

    @Override
    public void changeToPullDown() {
    }

    @Override
    public void changeToReleaseRefresh() {
    }

    @Override
    public void changeToRefreshing() {
        mStickinessRefreshView.startRefreshing();
    }

    @Override
    public void onEndLoadingMore() {
    }

    @Override
    public void onEndRefreshing() {
        mStickinessRefreshView.stopRefresh();
    }

    @Override
    public boolean canChangeToRefreshingStatus() {
        return mStickinessRefreshView.canChangeToRefreshing();
    }

    public int getRefreshHeaderViewHeight() {
        if (mRefreshHeaderView != null) {
            // 测量下拉刷新控件的高度
            mRefreshHeaderView.measure(0, 0);
            return mStickinessRefreshView.getTotalHeight();
        }
        return 0;
    }
}