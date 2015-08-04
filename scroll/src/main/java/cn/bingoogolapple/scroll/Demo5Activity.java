package cn.bingoogolapple.scroll;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 16:42
 * 描述:
 */
public class Demo5Activity extends AppCompatActivity {
    private static final String TAG = Demo5Activity.class.getSimpleName();
    private BGAScrollView mScrollView;
    private ImageView mImageView;
    private int mOriginalHeight;
    private int mDrawableHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo5);
        mScrollView = (BGAScrollView) findViewById(R.id.scrollView);
        mImageView = (ImageView) findViewById(R.id.imageView);
        setListener();
    }

    private void setListener() {
        mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mOriginalHeight == 0) {
                    mOriginalHeight = mImageView.getHeight();
                    mDrawableHeight = mImageView.getDrawable().getIntrinsicHeight();
                }
            }
        });
        mScrollView.setDelegate(new BGAScrollView.BGAScrollViewDelegate() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
//                Log.i(TAG, "onScrollChanged");
            }

            @Override
            public void onBottom() {
//                Log.i(TAG, "onBottom");
            }

            @Override
            public void onTop() {
//                Log.i(TAG, "onTop");
            }

            @Override
            public boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
                Log.i(TAG, "deltaY = " + deltaY + " scrollY = " + scrollY + " scrollRangeY = " + scrollRangeY + "  maxOverScrollY = " + maxOverScrollY);
                if (isTouchEvent && scrollY == 0) {
                    if (mImageView.getHeight() >= mOriginalHeight && mImageView.getHeight() <= mDrawableHeight) {
                        int newHeight = (int) (mImageView.getHeight() - deltaY / 3.0f);
                        mImageView.getLayoutParams().height = newHeight;
                        mImageView.requestLayout();
                        return true;
                    }
                }
                return false;
            }
        });
//        mScrollView.setDelegate(new BGAScrollView.BGASimpleScrollViewDelegate() {
//            @Override
//            public void onBottom() {
//                Log.i(TAG, "onBottom");
//            }
//
//            @Override
//            public void onTop() {
//                Log.i(TAG, "onTop");
//            }
//        });
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 执行回弹动画, 方式一: 属性动画\值动画
                        // 从当前高度mImage.getHeight(), 执行动画到原始高度mOriginalHeight
                        final int startHeight = mImageView.getHeight();
                        final int endHeight = mOriginalHeight;
                        valueAnimator(startHeight, endHeight);

                        // 执行回弹动画, 方式二: 自定义Animation
//                        ResetAnimation animation = new ResetAnimation(mImageView, startHeight, endHeight);
//                        mImageView.startAnimation(animation);

                        break;
                }
                return false;
            }
        });
    }

    private void valueAnimator(final int startHeight, final int endHeight) {
        ValueAnimator mValueAnim = ValueAnimator.ofInt(1);
        mValueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator mAnim) {
                float fraction = mAnim.getAnimatedFraction();
                // fraction 0.0 -> 1.0
                Integer newHeight = evaluate(fraction, startHeight, endHeight);
                mImageView.getLayoutParams().height = newHeight;
                mImageView.requestLayout();
            }
        });

        mValueAnim.setInterpolator(new OvershootInterpolator());
        mValueAnim.setDuration(1000);
        mValueAnim.start();
    }

    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int startInt = startValue;
        return (int) (startInt + fraction * (endValue - startInt));
    }

    private static class ResetAnimation extends Animation {
        private final ImageView mImageView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ResetAnimation(ImageView imageView, int startHeight, int endHeight) {
            mImageView = imageView;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setInterpolator(new OvershootInterpolator());
            setDuration(1000);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            // interpolatedTime 0.0f -> 1.0f
            Integer newHeight = evaluate(interpolatedTime, mStartHeight, mEndHeight);
            mImageView.getLayoutParams().height = newHeight;
            mImageView.requestLayout();
        }


        /**
         * 类型估值器
         *
         * @param fraction
         * @param startValue
         * @param endValue
         * @return
         */
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            int startInt = startValue;
            return (int) (startInt + fraction * (endValue - startInt));
        }

    }

}
