package cn.bingoogolapple.acvp.refreshlistview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import cn.bingoogolapple.acvp.refreshlistview.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/20 11:00
 * 描述:
 */
public class NormalRefreshListView extends AbRefreshListView {

    public NormalRefreshListView(Context context) {
        super(context);
    }

    public NormalRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NormalRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getRefreshHeaderView() {
        return View.inflate(getContext(), R.layout.view_refresh_header, null);
    }

    @Override
    protected View getWholeFooterView() {
        return View.inflate(getContext(), R.layout.view_refresh_footer, null);
    }

}