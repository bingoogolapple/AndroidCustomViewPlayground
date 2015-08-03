package cn.bingoogolapple.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Demo描述:
 * 实现可以拉动后回弹的布局.
 * 类似于下拉刷新的.
 *
 * 参考资料:
 * 1 http://gundumw100.iteye.com/blog/1884373
 * 2 http://blog.csdn.net/gemmem/article/details/7321910
 * 3 http://ipjmc.iteye.com/blog/1615828
 * 4 http://blog.csdn.net/c_weibin/article/details/7438323
 * 5 http://www.cnblogs.com/wanqieddy/archive/2012/05/05/2484534.html
 * 6 http://blog.csdn.net/hudashi/article/details/7353075
 *   Thank you very much
 */
public class BounceableLinearLayout extends LinearLayout {
    private Scroller mScroller;
    private GestureDetector mGestureDetector;

    public BounceableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context, new OvershootInterpolator());
        mGestureDetector = new GestureDetector(context, new GestureListenerImpl());
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            System.out.println("computeScroll()---> " + "mScroller.getCurrX()=" + mScroller.getCurrX() + "," + "mScroller.getCurrY()=" + mScroller.getCurrY());
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 必须执行postInvalidate()从而调用computeScroll()
            // 其实,在此调用invalidate();亦可
            postInvalidate();
        }
        super.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                // 手指抬起时回到最初位置
                prepareScroll(0, 0);
                break;
            default:
                // 其余情况交给GestureDetector手势处理
                return mGestureDetector.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    class GestureListenerImpl implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        // 控制拉动幅度:
        // int disY=(int)((distanceY - 0.5)/2);
        // 亦可直接调用:
        // smoothScrollBy(0, (int)distanceY);
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            int disY = (int) ((distanceY - 0.5) / 2);
            beginScroll(0, disY);
            return false;
        }

        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

    }

    // 滚动到目标位置
    protected void prepareScroll(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        beginScroll(dx, dy);
    }

    // 设置滚动的相对偏移
    protected void beginScroll(int dx, int dy) {
        System.out.println("smoothScrollBy()---> dx=" + dx + ",dy=" + dy);
        // 第一,二个参数起始位置;第三,四个滚动的偏移量
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 2000);
        System.out.println("smoothScrollBy()---> " + "mScroller.getFinalX()=" + mScroller.getFinalX() + "," + "mScroller.getFinalY()=" + mScroller.getFinalY());
        // 必须执行invalidate()从而调用computeScroll()
        invalidate();
    }
}