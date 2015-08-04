package cn.bingoogolapple.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.BounceInterpolator;
import android.widget.OverScroller;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/4 14:21
 * 描述:
 */
public class BounceTextView extends TextView {
    private static final String TAG = BounceTextView.class.getSimpleName();
    private OverScroller mScroller;

    private float lastX;
    private float lastY;

    private float startX;
    private float startY;


    public BounceTextView(Context context) {
        this(context, null);
    }

    public BounceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BounceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new OverScroller(context, new BounceInterpolator());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getX() - lastX;
                float disY = event.getY() - lastY;
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX), -(int) (getY() - startY));
                invalidate();
                break;
        }
        return true;
    }


    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getX();
        startY = getY();
    }

    public void spingBack() {
        if (mScroller.springBack((int) getX(), (int) getY(), 0, (int) getX(), 0, (int) getY() - 100)) {
            Log.d("TAG", "getX()=" + getX() + "__getY()=" + getY());
            invalidate();
        }

    }

}
