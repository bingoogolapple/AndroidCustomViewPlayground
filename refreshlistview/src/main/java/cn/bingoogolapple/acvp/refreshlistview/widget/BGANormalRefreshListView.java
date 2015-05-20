package cn.bingoogolapple.acvp.refreshlistview.widget;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bingoogolapple.acvp.refreshlistview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 11:00
 * 描述:
 */
public class BGANormalRefreshListView extends BGARefreshListView {
    private TextView mStatusTv;
    private TextView mTimeTv;
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

    public BGANormalRefreshListView(Context context) {
        super(context);
    }

    public BGANormalRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BGANormalRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getRefreshHeaderView() {
        View refreshView = View.inflate(getContext(), R.layout.view_normal_refresh_header, null);
        mStatusTv = (TextView) refreshView.findViewById(R.id.tv_normal_refresh_header_status);
        mTimeTv = (TextView) refreshView.findViewById(R.id.tv_normal_refresh_header_time);
        mArrowIv = (ImageView) refreshView.findViewById(R.id.iv_normal_refresh_header_arrow);
        mProgressPb = (ProgressBar) refreshView.findViewById(R.id.pb_normal_refresh_header_progress);

        mTimeTv.setText("最后刷新时间：" + getCurrentTime());
        initAnimation();
        return refreshView;
    }

    private void initAnimation() {
        mUpAnim = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mUpAnim.setDuration(500);
        mUpAnim.setFillAfter(true);

        mDownAnim = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mDownAnim.setDuration(500);
        mDownAnim.setFillAfter(true);
    }

    @Override
    protected View getWholeFooterView() {
        return View.inflate(getContext(), R.layout.view_normal_refresh_footer, null);
    }

    @Override
    protected void handleScale(float scale) {
        printLog("scale = " + scale);
        ViewCompat.setAlpha(mStatusTv, scale);
    }

    @Override
    protected void changeToPullDown() {
        mStatusTv.setText("下拉刷新");
        mProgressPb.setVisibility(INVISIBLE);
        mArrowIv.setVisibility(VISIBLE);
        mArrowIv.startAnimation(mDownAnim);
    }

    @Override
    protected void changeToReleaseRefresh() {
        mStatusTv.setText("释放刷新");
        mProgressPb.setVisibility(INVISIBLE);
        mArrowIv.setVisibility(VISIBLE);
        mArrowIv.startAnimation(mUpAnim);
    }

    @Override
    protected void changeToRefreshing() {
        mStatusTv.setText("正在刷新");
        mArrowIv.clearAnimation();
        mArrowIv.setVisibility(View.INVISIBLE);
        mProgressPb.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onEndLoadingMore() {
        printLog("结束上拉加载更多");
    }

    @Override
    protected void onEndRefreshing() {
        printLog("结束正在刷新");
    }

    private String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }

}