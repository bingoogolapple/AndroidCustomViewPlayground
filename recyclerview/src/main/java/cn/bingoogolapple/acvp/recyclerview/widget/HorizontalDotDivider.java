package cn.bingoogolapple.acvp.recyclerview.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import cn.bingoogolapple.acvp.recyclerview.R;

public class HorizontalDotDivider extends RecyclerView.ItemDecoration {
    private Drawable mDividerDrawable;

    public HorizontalDotDivider(Context context) {
        mDividerDrawable = context.getResources().getDrawable(R.mipmap.list_divider);
    }

    // 如果等于分割线的宽度或高度的话可以不用重写该方法
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildPosition(view) == parent.getChildCount() - 1) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 200, mDividerDrawable.getIntrinsicHeight());
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Log.i("bga", "onDraw1");
        super.onDraw(c, parent, state);
        Log.i("bga", "onDraw2");
    }

    // 先是onDraw，然后再是onDrawOver，选一个重写即可

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Log.i("bga", "onDrawOver1");
        drawVertical(c, parent);
        Log.i("bga", "onDrawOver2");
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight() - 200;
        View child;
        RecyclerView.LayoutParams layoutParams;
        int top;
        int bottom;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            child = parent.getChildAt(i);
            layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            top = child.getBottom() + layoutParams.bottomMargin;
            bottom = top + mDividerDrawable.getIntrinsicHeight();
            mDividerDrawable.setBounds(left, top, right, bottom);
            mDividerDrawable.draw(c);

            c.drawText("" + i, right + 100, top - child.getHeight() / 2, new Paint());
        }
    }

}