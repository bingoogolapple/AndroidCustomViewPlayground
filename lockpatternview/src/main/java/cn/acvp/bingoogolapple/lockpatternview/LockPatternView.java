package cn.acvp.bingoogolapple.lockpatternview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

public class LockPatternView extends View {
    private Point[][] points = new Point[3][3];
    private boolean mInit = false;
    private int mWidth;
    private int mHeight;
    private float mOffsetX;
    private float mOffsetY;

    public LockPatternView(Context context) {
        super(context);
    }

    public LockPatternView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatternView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(!mInit) {
            initPoints();
        }
        points2Canvas(canvas);
    }

    private void points2Canvas(Canvas canvas) {
        mWidth = getWidth();
        mHeight = getHeight();

        if(mWidth > mHeight) {
            // 横屏
            mOffsetX = (mWidth - mHeight) / 2;
            mWidth = mHeight;
        } else {
            // 竖屏
            mOffsetY = (mHeight - mWidth) / 2;
            mHeight = mWidth;
        }


    }

    private void initPoints() {
        
    }

    public static class Point {
        public static int STATE_NORMAL = 0;
        public static int STATE_PRESSED = 1;
        public static int STATE_ERROR = 2;
        public float x,y;
        public int index = 0;
        public int state = STATE_NORMAL;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}