package cn.bingoogolapple.acvp.nestedscrolling.ui.widget;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/9/28 上午12:18
 * 描述:
 */
public class NestedParentOne extends LinearLayout implements NestedScrollingChild, NestedScrollingParent {

    public NestedParentOne(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}