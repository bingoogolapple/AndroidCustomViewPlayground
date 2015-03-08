package cn.bingoogolapple.acvp.lockpatternview.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.lockpatternview.R;

public class LockPatternView extends View {
    private static final int POINT_SIZE = 5;
    private Point[][] mPoints = new Point[3][3];
    private boolean mIsInit = false;
    private boolean mIsSelecting = false;
    private boolean mIsFinish = false;
    private boolean mMovingNoPoint = false;
    private int mSize;
    private float mOffsetX;
    private float mOffsetY;
    private float mPointRadius;

    private float mMovingX;
    private float mMovingY;

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap mPointNormalBitmap;
    private Bitmap mPointPressedBitmap;
    private Bitmap mPointErrorBitmap;
    private Bitmap mLinePressedBitmap;
    private Bitmap mLineErrorBitmap;

    private List<Point> mPointList = new ArrayList<Point>();

    private Matrix mMatrix = new Matrix();
    private OnPatternChangeListener mOnPatternChangeListener;


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
        if (!mIsInit) {
            initPoints();
        }
        points2Canvas(canvas);

        if (mPointList.size() > 0) {
            Point pointA = mPointList.get(0);
            for (int i = 0; i < mPointList.size(); i++) {
                Point pointB = mPointList.get(i);
                line2Canvas(canvas, pointA, pointB);
                pointA = pointB;
            }
            if (mMovingNoPoint) {
                line2Canvas(canvas, pointA, new Point(mMovingX, mMovingY));
            }
        }
    }

    private void points2Canvas(Canvas canvas) {
        Point point;
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                point = mPoints[i][j];
                if (point.state == Point.STATE_PRESSED) {
                    canvas.drawBitmap(mPointPressedBitmap, point.x - mPointRadius, point.y - mPointRadius, mPaint);
                } else if (point.state == Point.STATE_ERROR) {
                    canvas.drawBitmap(mPointErrorBitmap, point.x - mPointRadius, point.y - mPointRadius, mPaint);
                } else {
                    canvas.drawBitmap(mPointNormalBitmap, point.x - mPointRadius, point.y - mPointRadius, mPaint);
                }
            }
        }
    }

    private void line2Canvas(Canvas canvas, Point pointA, Point pointB) {
        float lineLength = (float) Point.distance(pointA, pointB);
        float degrees = getDegrees(pointA, pointB);
        canvas.rotate(degrees, pointA.x, pointA.y);
        if (pointA.state == Point.STATE_PRESSED) {
            mMatrix.setScale(lineLength / mLinePressedBitmap.getWidth(), 1);
            mMatrix.postTranslate(pointA.x - mLinePressedBitmap.getWidth() / 2, pointA.y - mLinePressedBitmap.getHeight() / 2);
            canvas.drawBitmap(mLinePressedBitmap, mMatrix, mPaint);
        } else {
            mMatrix.setScale(lineLength / mLineErrorBitmap.getWidth(), 1);
            mMatrix.postTranslate(pointA.x - mLineErrorBitmap.getWidth() / 2, pointA.y - mLineErrorBitmap.getHeight() / 2);
            canvas.drawBitmap(mLineErrorBitmap, mMatrix, mPaint);
        }
        canvas.rotate(-degrees, pointA.x, pointA.y);
    }

    private void initPoints() {
        int width = getWidth();
        int height = getHeight();

        if (width > height) {
            // 横屏
            mOffsetX = (width - height) / 2;
            mSize = height;
        } else {
            // 竖屏
            mOffsetY = (height - width) / 2;
            mSize = width;
        }

        mPointNormalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point_normal);
        mPointPressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point_pressed);
        mPointErrorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.point_error);
        mLinePressedBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.line_pressed);
        mLineErrorBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.line_error);

        mPoints[0][0] = new Point(mOffsetX + mSize / 4, mOffsetY + mSize / 4);
        mPoints[0][1] = new Point(mOffsetX + mSize / 2, mOffsetY + mSize / 4);
        mPoints[0][2] = new Point(mOffsetX + mSize * 3 / 4, mOffsetY + mSize / 4);

        mPoints[1][0] = new Point(mOffsetX + mSize / 4, mOffsetY + mSize / 2);
        mPoints[1][1] = new Point(mOffsetX + mSize / 2, mOffsetY + mSize / 2);
        mPoints[1][2] = new Point(mOffsetX + mSize * 3 / 4, mOffsetY + mSize / 2);

        mPoints[2][0] = new Point(mOffsetX + mSize / 4, mOffsetY + mSize * 3 / 4);
        mPoints[2][1] = new Point(mOffsetX + mSize / 2, mOffsetY + mSize * 3 / 4);
        mPoints[2][2] = new Point(mOffsetX + mSize * 3 / 4, mOffsetY + mSize * 3 / 4);

        mPointRadius = mPointNormalBitmap.getWidth() / 2;

        int index = 1;
        for (Point[] points : mPoints) {
            for (Point point : points) {
                point.index = index;
                index++;
            }
        }

        mIsInit = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mMovingNoPoint = false;
        mIsFinish = false;
        mMovingX = event.getX();
        mMovingY = event.getY();
        Point point = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(mOnPatternChangeListener != null) {
                    mOnPatternChangeListener.onPatternStart(true);
                }
                resetPoint();
                point = checkSelectPoint();
                if (point != null) {
                    mIsSelecting = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsSelecting) {
                    point = checkSelectPoint();
                    if (point == null) {
                        mMovingNoPoint = true;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mIsFinish = true;
                mIsSelecting = false;
                break;
        }

        // 选中重复检查
        if (!mIsFinish && mIsSelecting && point != null) {
            if (crossPointCheck(point)) {
                mMovingNoPoint = true;
            } else {
                point.state = Point.STATE_PRESSED;
                mPointList.add(point);
            }
        }

        if (mIsFinish) {
            if (mPointList.size() == 1) {
                // 绘制不成立
                resetPoint();
            } else if (mPointList.size() > 0 && mPointList.size() < POINT_SIZE) {
                // 绘制错误
                errorPoint();
                if(mOnPatternChangeListener != null) {
                    mOnPatternChangeListener.onPatternChange(null);
                }
            } else {
                // 绘制成功
                if(mOnPatternChangeListener != null) {
                    String password = "";
                    for(Point selectedPoint : mPointList) {
                        password += selectedPoint.index;
                    }
                    mOnPatternChangeListener.onPatternChange(password);
                }
            }
        }
        postInvalidate();
        return true;
    }

    private void resetPoint() {
        for (Point point : mPointList) {
            point.state = Point.STATE_NORMAL;
        }
        mPointList.clear();
    }

    private void errorPoint() {
        for (Point point : mPointList) {
            point.state = Point.STATE_ERROR;
        }
    }

    private boolean crossPointCheck(Point point) {
        if (mPointList.contains(point)) {
            return true;
        } else {
            return false;
        }
    }

    private Point checkSelectPoint() {
        Point point;
        for (int i = 0; i < mPoints.length; i++) {
            for (int j = 0; j < mPoints[i].length; j++) {
                point = mPoints[i][j];
                if (Point.with(point.x, point.y, mPointRadius, mMovingX, mMovingY)) {
                    return point;
                }
            }
        }
        return null;
    }

    public float getDegrees(Point pointA, Point pointB) {
        return (float) Math.toDegrees(Math.atan2(pointB.y - pointA.y, pointB.x - pointA.x));
    }
    
    public void setOnPatternChangeListener(OnPatternChangeListener onPatternChangeListener) {
        mOnPatternChangeListener = onPatternChangeListener;
    }

    public static class Point {
        public static int STATE_NORMAL = 0;
        public static int STATE_PRESSED = 1;
        public static int STATE_ERROR = 2;
        public float x, y;
        public int index = 0;
        public int state = STATE_NORMAL;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public static double distance(Point pointA, Point pointB) {
            return Math.sqrt(Math.pow(Math.abs(pointA.x - pointB.x), 2) + Math.pow(Math.abs(pointA.y - pointB.y), 2));
        }

        public static boolean with(float pointX, float pointY, float radius, float movingX, float movingY) {
            return Math.sqrt(Math.pow(Math.abs(pointX - movingX), 2) + Math.pow(Math.abs(pointY - movingY), 2)) < radius;
        }
    }

    public static interface  OnPatternChangeListener {
        public void onPatternChange(String password);

        public void onPatternStart(boolean isRestart);
    }

}