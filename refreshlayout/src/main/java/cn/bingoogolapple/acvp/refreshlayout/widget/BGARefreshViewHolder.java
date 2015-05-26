package cn.bingoogolapple.acvp.refreshlayout.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bingoogolapple.acvp.refreshlayout.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 12:56
 * 描述:
 */
public abstract class BGARefreshViewHolder {
    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
     */
    private static final float PADDING_TOP_SCALE = 1.8f;
    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值
     */
    private static final float SPRING_DISTANCE_SCALE = 0.4f;

    protected Context mContext;
    protected BGARefreshLayout mRefreshLayout;
    /**
     * 下拉刷新控件
     */
    protected View mRefreshHeaderView;
    /**
     * 上拉加载更多控件
     */
    protected View mLoadMoreFooterView;

    protected TextView mFooterStatusTv;
    protected ImageView mFooterChrysanthemumIv;
    protected AnimationDrawable mFooterChrysanthemumAd;
    protected String mLodingMoreText = "加载中...";

    private boolean mIsLoadingMoreEnabled = true;
    private int mLoadMoreBackgroundColorRes = -1;
    private int mLoadMoreBackgroundDrawableRes = -1;

    /**
     * 头部控件移动动画时常
     */
    private int mTopAnimDuration = 500;
    /**
     * 底部控件移动动画时常
     */
    private int mBottomAnimDuration = 300;

    public BGARefreshViewHolder(Context context, boolean isLoadingMoreEnabled) {
        mContext = context;
        mIsLoadingMoreEnabled = isLoadingMoreEnabled;
    }

    public void setLoadingMoreText(String loadingMoreText) {
        mLodingMoreText = loadingMoreText;
    }

    public void setLoadMoreBackgroundColorRes(@ColorRes int loadMoreBackgroundColorRes) {
        mLoadMoreBackgroundColorRes = loadMoreBackgroundColorRes;
    }

    public void setLoadMoreBackgroundDrawableRes(@DrawableRes int loadMoreBackgroundDrawableRes) {
        mLoadMoreBackgroundDrawableRes = loadMoreBackgroundDrawableRes;
    }

    public int getTopAnimDuration() {
        return mTopAnimDuration;
    }

    public void setTopAnimDuration(int topAnimDuration) {
        mTopAnimDuration = topAnimDuration;
    }

    public int getBottomAnimDuration() {
        return mBottomAnimDuration;
    }

    public void setBottomAnimDuration(int bottomAnimDuration) {
        mBottomAnimDuration = bottomAnimDuration;
    }

    /**
     * 获取上拉加载更多控件，如果不喜欢这种上拉刷新风格可重写该方法实现自定义LoadMoreFooterView
     *
     * @return
     */
    public View getLoadMoreFooterView() {
        if (!mIsLoadingMoreEnabled) {
            return null;
        }
        if (mLoadMoreFooterView == null) {
            mLoadMoreFooterView = View.inflate(mContext, R.layout.view_normal_refresh_footer, null);
            if (mLoadMoreBackgroundColorRes != -1) {
                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundColorRes);
            }
            if (mLoadMoreBackgroundDrawableRes != -1) {
                mLoadMoreFooterView.setBackgroundResource(mLoadMoreBackgroundDrawableRes);
            }
            mFooterStatusTv = (TextView) mLoadMoreFooterView.findViewById(R.id.tv_normal_refresh_footer_status);
            mFooterChrysanthemumIv = (ImageView) mLoadMoreFooterView.findViewById(R.id.iv_normal_refresh_footer_chrysanthemum);
            mFooterChrysanthemumAd = (AnimationDrawable) mFooterChrysanthemumIv.getDrawable();
            mFooterStatusTv.setText(mLodingMoreText);
        }
        return mLoadMoreFooterView;
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    public abstract View getRefreshHeaderView();

    /**
     * 下拉刷新控件可见时，处理上下拉进度
     *
     * @param scale         下拉过程0 ==> 1
     * @param moveYDistance
     */
    public abstract void handleScale(float scale, int moveYDistance);

    /**
     * 进入到未处理下拉刷新状态
     */
    public abstract void changeToIdle();

    /**
     * 进入下拉刷新状态
     */
    public abstract void changeToPullDown();

    /**
     * 进入释放刷新状态
     */
    public abstract void changeToReleaseRefresh();

    /**
     * 进入正在刷新状态
     */
    public abstract void changeToRefreshing();

    /**
     * 结束下拉刷新
     */
    public abstract void onEndRefreshing();

    /**
     * 手指移动距离与下拉刷新控件paddingTop移动距离的比值
     *
     * @return
     */
    public float getPaddingTopScale() {
        return PADDING_TOP_SCALE;
    }

    /**
     * 下拉刷新控件paddingTop的弹簧距离与下拉刷新控件高度的比值
     *
     * @return
     */
    public float getSpringDistanceScale() {
        return SPRING_DISTANCE_SCALE;
    }

    /**
     * 是处于能够进入刷新状态
     *
     * @return
     */
    public boolean canChangeToRefreshingStatus() {
        return false;
    }

    /**
     * 进入加载更多状态
     */
    public void changeToLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd.start();
        }
    }

    /**
     * 结束上拉加载更多
     */
    public void onEndLoadingMore() {
        if (mIsLoadingMoreEnabled && mFooterChrysanthemumAd != null) {
            mFooterChrysanthemumAd.stop();
        }
    }

    /**
     * 获取下拉刷新控件的高度，如果初始化时的高度和最后展开的最大高度不一致，需重写该方法返回最大高度
     *
     * @return
     */
    public int getRefreshHeaderViewHeight() {
        if (mRefreshHeaderView != null) {
            // 测量下拉刷新控件的高度
            mRefreshHeaderView.measure(0, 0);
            return mRefreshHeaderView.getMeasuredHeight();
        }
        return 0;
    }

    public void startChangeWholeHeaderViewPaddingTop(int distance) {
        mRefreshLayout.startChangeWholeHeaderViewPaddingTop(distance);
    }

    public void setRefreshLayout(BGARefreshLayout refreshLayout) {
        mRefreshLayout = refreshLayout;
    }

}