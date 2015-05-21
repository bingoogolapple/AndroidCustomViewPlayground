package cn.bingoogolapple.acvp.refreshlistview.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import cn.bingoogolapple.acvp.refreshlistview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 11:00
 * 描述:
 */
public class BGAMoocRefreshListView extends BGARefreshListView {
    private MoocView mMoocView;


    public BGAMoocRefreshListView(Context context) {
        super(context);
    }

    public BGAMoocRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BGAMoocRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getRefreshHeaderView() {
        View refreshView = View.inflate(getContext(), R.layout.view_mooc_refresh_header, null);
        mMoocView = (MoocView) refreshView.findViewById(R.id.moocView);
        return refreshView;
    }

    @Override
    protected View getWholeFooterView() {
        return View.inflate(getContext(), R.layout.view_normal_refresh_footer, null);
    }

    @Override
    protected void handleScale(float scale) {
//        printLog("scale = " + scale);
        scale = 0.6f + 0.4f * scale;
        ViewCompat.setScaleX(mMoocView, scale);
        ViewCompat.setScaleY(mMoocView, scale);
    }

    @Override
    protected void changeToPullDown() {

    }

    @Override
    protected void changeToReleaseRefresh() {

    }

    @Override
    protected void changeToRefreshing() {
        mMoocView.startRefreshing();
    }

    @Override
    protected void onEndLoadingMore() {
        printLog("结束上拉加载更多");
    }

    @Override
    protected void onEndRefreshing() {
        mMoocView.stopRefreshing();
    }

}