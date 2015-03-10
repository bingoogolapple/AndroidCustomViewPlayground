package cn.bingoogolapple.acvp.recyclerview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class BGAEmptyView extends FrameLayout {
    private View mContentView;
    private View mEmptyView;

    public BGAEmptyView(Context context) {
        this(context, null);
    }

    public BGAEmptyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGAEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        if (getChildCount() != 2) {
            throw new RuntimeException(BGAEmptyView.class.getSimpleName() + "必须有且只有两个子控件");
        }
        mContentView = getChildAt(0);
        mEmptyView = getChildAt(1);
        mEmptyView.setVisibility(View.GONE);
    }

    public void showEmptyView() {
        mContentView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    public void showContentView() {
        mEmptyView.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);
    }

}