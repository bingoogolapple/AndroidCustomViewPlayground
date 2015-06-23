package cn.bingoogolapple.acvp.flowlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class FlowLayout extends ViewGroup {
    private List<Row> mRows = new ArrayList<>();
    private int mHorizontalChildGap;
    private int mVerticalChildGap;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mHorizontalChildGap = dp2px(context, 10);
        mVerticalChildGap = dp2px(context, 10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        /**
         * 1.EXACTLY:100dp,match_parent
         * 2.AT_MOST:wrap_content
         * 3.UNSPCIFIED:子控件想要多大就多大，很少见（ScrollView）
         */
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        mRows.clear();
        Row row = new Row(sizeWidth);

        View child;
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            if (!row.addChild(child)) {
                mRows.add(row);
                row = new Row(sizeWidth);
                row.addChild(child);
            }
        }

        // 添加最后一行
        if (!mRows.contains(row)) {
            mRows.add(row);
        }

        int height = 0;
        int rowCount = mRows.size();
        for (int i = 0; i < rowCount; i++) {
            height += mRows.get(i).mHeight;
            if (i != rowCount - 1) {
                height += mVerticalChildGap;
            }
        }
        setMeasuredDimension(sizeWidth, modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int rowCount = mRows.size();
        int top = getPaddingTop();
        Row row;
        for (int i = 0; i < rowCount; i++) {
            row = mRows.get(i);
            if (i != rowCount - 1) {
                row.layout(true, top);
            } else {
                row.layout(false, top);
            }
            top += row.mHeight + mVerticalChildGap;
        }
    }

    private class Row {
        private List<View> mViews = new ArrayList<>();
        private int mWidth;
        private int mNewWidth;
        private int mHeight;
        private int mMaxWidth;

        public Row(int sizeWidth) {
            mMaxWidth = sizeWidth - getPaddingLeft() - getPaddingRight();
        }

        public boolean addChild(View child) {
            if (isOutOfMaxWidth(child.getMeasuredWidth())) {
                return false;
            } else {
                mViews.add(child);
                mWidth = mNewWidth;

                int childHeight = child.getMeasuredHeight();
                mHeight = mHeight < childHeight ? childHeight : mHeight;
                return true;
            }
        }

        private boolean isOutOfMaxWidth(int childWidth) {
            if (mViews.size() == 0) {
                mNewWidth = mWidth + childWidth;
            } else {
                mNewWidth = mWidth + mHorizontalChildGap + childWidth;
            }
            return mNewWidth > mMaxWidth;
        }

        public void layout(boolean isNeedSplit, int top) {
            if (mViews.size() == 0) {
                return;
            }

            int left = getPaddingLeft();
            int count = mViews.size();
            int splitWidth = (mMaxWidth - mWidth) / count;
            View view;
            for (int i = 0; i < count; i++) {
                view = mViews.get(i);
                int childWidth = view.getMeasuredWidth();
                int childHeight = view.getMeasuredHeight();
                int topOffset = (int) ((mHeight - childHeight) / 2.0 + 0.5);
                if (isNeedSplit) {
                    childWidth = childWidth + splitWidth;
                    view.getLayoutParams().width = childWidth;
                    if (splitWidth > 0) {
                        /**
                         * 1.EXACTLY:100dp,match_parent
                         * 2.AT_MOST:wrap_content
                         * 3.UNSPCIFIED:子控件想要多大就多大，很少见（ScrollView）
                         */
                        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
                        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
                        view.measure(widthMeasureSpec, heightMeasureSpec);
                    }
                }
                view.layout(left, top + topOffset, left + childWidth, top + topOffset + childHeight);

                left += childWidth + mHorizontalChildGap;
            }
        }
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}