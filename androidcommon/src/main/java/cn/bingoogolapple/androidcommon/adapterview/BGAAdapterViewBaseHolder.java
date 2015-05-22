package cn.bingoogolapple.androidcommon.adapterview;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午12:54
 * 描述:
 */
public class BGAAdapterViewBaseHolder {
    protected final SparseArray<View> mViews = new SparseArray<View>();
    protected View mConvertView;
    protected int mPosition;

    public View getConvertView() {
        return mConvertView;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public BGAAdapterViewBaseHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

}