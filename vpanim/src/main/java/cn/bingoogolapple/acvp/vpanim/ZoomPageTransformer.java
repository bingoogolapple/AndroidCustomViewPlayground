package cn.bingoogolapple.acvp.vpanim;

import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/6/19 上午8:42
 * 描述:
 */
public class ZoomPageTransformer extends BGAPageTransformer {
    private float mMinScale = 0.85f;
    private float mMinAlpha = 0.65f;

    @Override
    public void handleInvisiblePage(View view, float position) {
        ViewHelper.setAlpha(view, 0);
    }

    @Override
    public void handleLeftPage(View view, float position) {
        float scaleFactor = Math.max(mMinScale, 1 - Math.abs(position));
        float vertMargin = view.getHeight() * (1 - scaleFactor) / 2;
        float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;
        if (position < 0) {
            ViewHelper.setTranslationX(view, horzMargin - vertMargin / 2);
        } else {
            ViewHelper.setTranslationX(view, -horzMargin + vertMargin / 2);
        }
        ViewHelper.setScaleX(view, scaleFactor);
        ViewHelper.setScaleY(view, scaleFactor);
        ViewHelper.setAlpha(view, mMinAlpha + (scaleFactor - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha));
    }

    @Override
    public void handleRightPage(View view, float position) {
        handleLeftPage(view, position);
    }

    public void setMinAlpha(float minAlpha) {
        if (minAlpha >= 0.6f && minAlpha <= 1.0f) {
            mMinAlpha = minAlpha;
        }
    }

    public void setMinScale(float minScale) {
        if (minScale >= 0.6f && minScale <= 1.0f) {
            mMinScale = minScale;
        }
    }
}