package cn.bingoogolapple.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 17:33
 * 描述:
 */
public class LotteryView extends LinearLayout {
    private Scroller mScroller;
    private GestureDetector mGestureDetector;
    // 第一个子控件的显示状态，默认不显示
    private boolean mFirstChildVisible = false;
    // 是否正在加载动画
    private boolean mIsScrolling = false;
    // 动画时间
    private int mDuration = 500;

    public LotteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new AccelerateDecelerateInterpolator());
        mGestureDetector = new GestureDetector(context, new GestureListenerImpl());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mFirstChildVisible) {
            scrollTo(0, 0);
        } else {
            // 注意：scrollTo的坐标是反过来的，不应该是 -getChildAt(0).getHeight()
            scrollTo(0, getChildAt(0).getHeight());
        }
    }

    @Override
    public void computeScroll() {
        // 如果正在滚动，并且scroller还未停止，则继续计算滚动
        if (mIsScrolling && mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else if (mIsScrolling) {
            mIsScrolling = false;
            mFirstChildVisible = !mFirstChildVisible;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            return true;
        }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    class GestureListenerImpl implements GestureDetector.OnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // 如果不是正在滑动才判断是否执行动画
            if (!mIsScrolling) {
                if (velocityY < 0 && mFirstChildVisible) {
                    // 设置隐藏
                    mIsScrolling = true;
                    // 第一,二个参数起始位置;第三,四个滚动的偏移量
                    mScroller.startScroll(0, 0, 0, getChildAt(0).getHeight(), mDuration);
                    postInvalidate();
                } else if (velocityY > 0 && !mFirstChildVisible) {
                    // 设置显示
                    mIsScrolling = true;
                    // 注意：坐标是反过来的。不应该是mScroller.startScroll(0, -getChildAt(0).getHeight(), 0, getChildAt(0).getHeight(), mDuration);
                    mScroller.startScroll(0, getChildAt(0).getHeight(), 0, -getChildAt(0).getHeight(), mDuration);
                    postInvalidate();
                }
            }
            return false;
        }

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

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }
    }
}