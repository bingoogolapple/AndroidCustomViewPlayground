package cn.bingoogolapple.scroll;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 17:10
 * 描述:
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * API注释:
 *
 * 1 //第一,二个参数起始位置;第三,四个滚动的偏移量;第五个参数持续时间
 *   startScroll(int startX, int startY, int dx, int dy, int duration)
 *
 * 2 //在startScroll()方法执行过程中即在duration时间内computeScrollOffset()
 *   方法会一直返回true，但当动画执行完成后会返回返加false.
 *   computeScrollOffset()
 *
 * 3 当执行ontouch()或invalidate()或postInvalidate()均会调用该方法
 *   computeScroll()
 *
 */
public class LinearLayoutSubClass extends LinearLayout {
    private Scroller mScroller;
    private boolean flag = true;

    public LinearLayoutSubClass(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 也可采用该构造方法传入一个interpolator
        // mScroller=new Scroller(context, interpolator);
        mScroller = new Scroller(context);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            // 使其再次调用computeScroll()直至滑动结束,即不满足if条件
            postInvalidate();
        }
    }

    public void beginScroll() {
        if (flag) {
            mScroller.startScroll(0, 0, -500, 0, 3000);
            flag = false;
        } else {
            mScroller.startScroll(-500, 0, 500, 0, 3000);
            flag = true;
        }
        // 调用invalidate();使其调用computeScroll()
        invalidate();
    }

}