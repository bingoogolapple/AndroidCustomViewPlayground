package cn.bingoogolapple.acvp.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 10:34
 * 描述:
 */
public abstract class AbRefreshListView extends ListView implements AbsListView.OnScrollListener {
    /**
     * 整个头部控件，下拉刷新控件mRefreshHeaderView和下拉刷新控件下方的自定义组件mCustomHeaderView的父控件
     */
    private LinearLayout mWholeHeaderView;
    /**
     * 下拉刷新控件
     */
    private View mRefreshHeaderView;
    /**
     * 下拉刷新控件下方的自定义控件（例如ListView顶部的轮播广告条）
     */
    private View mCustomHeaderView;
    /**
     * 上拉加载更多控件
     */
    private View mWholeFooterView;
    /**
     * 下拉刷新控件的高度
     */
    private int mRefreshHeaderViewHeight;
    /**
     * 上拉加载更多控件的高度
     */
    private int mWholeFooterViewHeight;

    public AbRefreshListView(Context context) {
        this(context, null);
    }

    public AbRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initWholeHeaderView();
        initWholeFooterView();

        setOnScrollListener(this);
    }

    /**
     * 初始化整个头部控件
     */
    private void initWholeHeaderView() {
        mWholeHeaderView = new LinearLayout(getContext());
        mWholeHeaderView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
        mWholeHeaderView.setOrientation(LinearLayout.VERTICAL);

        initRefreshHeaderView();

        addHeaderView(mWholeHeaderView);
    }

    /**
     * 初始化下拉刷新控件
     *
     * @return
     */
    private void initRefreshHeaderView() {
        mRefreshHeaderView = getRefreshHeaderView();
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mRefreshHeaderView);

            // 测量下拉刷新控件的高度
            mRefreshHeaderView.measure(0, 0);
            mRefreshHeaderViewHeight = mRefreshHeaderView.getMeasuredHeight();
            mWholeHeaderView.setPadding(0, -mRefreshHeaderViewHeight, 0, 0);
        }
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    protected abstract View getRefreshHeaderView();

    private void initWholeFooterView() {
        mWholeFooterView = getWholeFooterView();
        if (mWholeFooterView != null) {
            mWholeFooterView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT));
            addFooterView(mWholeFooterView);

            // 测量下拉刷新控件的高度
            mWholeFooterView.measure(0, 0);
            mWholeFooterViewHeight = mWholeFooterView.getMeasuredHeight();
            mWholeFooterView.setPadding(0, -mWholeFooterViewHeight, 0, 0);
        }
    }

    /**
     * 获取下拉刷新控件
     *
     * @return
     */
    protected abstract View getWholeFooterView();

    /**
     * 添加下拉刷新控件下方的自定义控件
     *
     * @param customHeaderView
     */
    public void addCustomHeaderView(View customHeaderView) {
        mCustomHeaderView = customHeaderView;
        if (mCustomHeaderView != null) {
            mCustomHeaderView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mCustomHeaderView);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

}