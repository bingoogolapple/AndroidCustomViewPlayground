package cn.bingoogolapple.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/4 10:18
 * 描述:
 */
public class BGAScrollView extends ScrollView {
    private BGAScrollViewDelegate mDelegate;

    public BGAScrollView(Context context) {
        this(context, null);
    }

    public BGAScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.scrollViewStyle);
    }

    public BGAScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mDelegate != null) {
            mDelegate.onScrollChanged(l, t, oldl, oldt);

            if (getScrollY() + getHeight() >= computeVerticalScrollRange()) {
                mDelegate.onBottom();
            }

            if (getScrollY() == 0) {
                mDelegate.onTop();
            }
        }
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        if (mDelegate != null) {
            if (mDelegate.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent)) {
                return true;
            }
        }
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    public void setDelegate(BGAScrollViewDelegate delegate) {
        mDelegate = delegate;
    }

    public interface BGAScrollViewDelegate {
        void onScrollChanged(int l, int t, int oldl, int oldt);

        void onBottom();

        void onTop();

        boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent);
    }

    public static class BGASimpleScrollViewDelegate implements BGAScrollViewDelegate {

        @Override
        public void onScrollChanged(int l, int t, int oldl, int oldt) {
        }

        @Override
        public void onBottom() {
        }

        @Override
        public void onTop() {
        }

        @Override
        public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
            return false;
        }
    }
}
