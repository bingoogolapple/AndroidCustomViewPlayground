package cn.bingoogolapple.acvp.vpanim;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/19 上午8:41
 * 描述:
 */
public class RotatePageTransformer implements ViewPager.PageTransformer {
    private static final float ROT_MAX = 15.0f;

    public void transformPage(View view, float position) {
        if (position < -1) {
            ViewHelper.setRotation(view, 0);
        } else if (position <= 1) {
            float rotation = (ROT_MAX * position);
            ViewHelper.setPivotX(view, view.getMeasuredWidth() * 0.5f);
            ViewHelper.setPivotY(view, view.getMeasuredHeight());
            ViewHelper.setRotation(view, rotation);
        } else {
            ViewHelper.setRotation(view, 0);
        }
    }
}