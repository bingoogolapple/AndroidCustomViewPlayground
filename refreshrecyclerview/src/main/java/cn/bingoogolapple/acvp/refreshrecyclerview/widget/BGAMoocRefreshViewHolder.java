package cn.bingoogolapple.acvp.refreshrecyclerview.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;

import cn.bingoogolapple.acvp.refreshrecyclerview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 13:16
 * 描述:
 */
public class BGAMoocRefreshViewHolder extends BGARefreshViewHolder {
    private MoocView mMoocView;

    public BGAMoocRefreshViewHolder(Context context) {
        super(context);
    }

    @Override
    public View getLoadMoreFooterView() {
//        if(mLoadMoreFooterView == null) {
//            mLoadMoreFooterView = View.inflate(mContext, R.layout.view_normal_refresh_footer, null);
//        }
//        return mLoadMoreFooterView;
        return null;
    }

    @Override
    public View getRefreshHeaderView() {
        if(mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header_mooc, null);
            mMoocView = (MoocView) mRefreshHeaderView.findViewById(R.id.moocView);
        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale) {
        scale = 0.6f + 0.4f * scale;
        ViewCompat.setScaleX(mMoocView, scale);
        ViewCompat.setScaleY(mMoocView, scale);
    }

    @Override
    public void changeToPullDown() {

    }

    @Override
    public void changeToReleaseRefresh() {

    }

    @Override
    public void changeToRefreshing() {
        mMoocView.startRefreshing();
    }

    @Override
    public void onEndLoadingMore() {

    }

    @Override
    public void onEndRefreshing() {
        mMoocView.stopRefreshing();
    }
}
