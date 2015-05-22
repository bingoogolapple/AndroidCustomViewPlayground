package cn.bingoogolapple.acvp.refreshlayout.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.bingoogolapple.acvp.refreshlayout.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 13:05
 * 描述:
 */
public class BGANormalRefreshViewHolder extends BGARefreshViewHolder {
    private TextView mStatusTv;
    private ImageView mArrowIv;
    private ProgressBar mProgressPb;
    /**
     * 向上旋转的动画
     */
    private RotateAnimation mUpAnim;
    /**
     * 向下旋转的动画
     */
    private RotateAnimation mDownAnim;

    public BGANormalRefreshViewHolder(Context context) {
        super(context);
        initAnimation();
    }

    private void initAnimation() {
        mUpAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mUpAnim.setDuration(500);
        mUpAnim.setFillAfter(true);

        mDownAnim = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDownAnim.setFillAfter(true);
    }

    @Override
    public View getLoadMoreFooterView() {
        return null;
    }

    @Override
    public View getRefreshHeaderView() {
        if(mRefreshHeaderView == null) {
            mRefreshHeaderView = View.inflate(mContext, R.layout.view_refresh_header_normal, null);
            mStatusTv = (TextView) mRefreshHeaderView.findViewById(R.id.tv_normal_refresh_header_status);
            mArrowIv = (ImageView) mRefreshHeaderView.findViewById(R.id.iv_normal_refresh_header_arrow);
            mProgressPb = (ProgressBar) mRefreshHeaderView.findViewById(R.id.pb_normal_refresh_header_progress);
        }
        return mRefreshHeaderView;
    }

    @Override
    public void handleScale(float scale) {
        ViewCompat.setAlpha(mStatusTv, scale);
    }

    @Override
    public void changeToIdle() {
    }

    @Override
    public void changeToPullDown() {
        mStatusTv.setText("下拉刷新");
        mProgressPb.setVisibility(View.INVISIBLE);
        mArrowIv.setVisibility(View.VISIBLE);
        mDownAnim.setDuration(500);
        mArrowIv.startAnimation(mDownAnim);
    }

    @Override
    public void changeToReleaseRefresh() {
        mStatusTv.setText("释放刷新");
        mProgressPb.setVisibility(View.INVISIBLE);
        mArrowIv.setVisibility(View.VISIBLE);
        mArrowIv.startAnimation(mUpAnim);
    }

    @Override
    public void changeToRefreshing() {
        mStatusTv.setText("正在刷新");
        // 必须把动画清空才能隐藏成功
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(View.INVISIBLE);
        mProgressPb.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEndLoadingMore() {
    }

    @Override
    public void onEndRefreshing() {
        mStatusTv.setText("下拉刷新");
        mProgressPb.setVisibility(View.INVISIBLE);
        mArrowIv.setVisibility(View.VISIBLE);
        mDownAnim.setDuration(0);
        mArrowIv.startAnimation(mDownAnim);
    }
}
