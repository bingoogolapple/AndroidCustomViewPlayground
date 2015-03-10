package cn.bingoogolapple.acvp.recyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ItemDivider extends RecyclerView.ItemDecoration {
    private Drawable mDividerDrawable;
    private int mOrientation;

    public ItemDivider(Context context, int dividerResId) {
        this(context, dividerResId, LinearLayoutManager.VERTICAL);
    }

    public ItemDivider(Context context, int dividerResId, int orientation) {
        mDividerDrawable = context.getResources().getDrawable(dividerResId);
        setOrientation(orientation);
    }

    // 如果等于分割线的宽度或高度的话可以不用重写该方法
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDividerDrawable.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDividerDrawable.getIntrinsicWidth(), 0);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        View child;
        RecyclerView.LayoutParams layoutParams;
        int top;
        int bottom;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + mDividerDrawable.getIntrinsicHeight();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        View child;
        RecyclerView.LayoutParams layoutParams;
        int left;
        int right;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            child = parent.getChildAt(i);
            layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            left = child.getRight() + layoutParams.rightMargin;
            right = left + mDividerDrawable.getIntrinsicWidth();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);
        }
    }
}