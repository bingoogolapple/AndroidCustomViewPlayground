package cn.bingoogolapple.acvp.selectview.util;

import android.text.Spanned;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/8 18:00
 * 描述:
 */
public class BaseViewHolder {
    protected final SparseArray<View> mViews = new SparseArray<View>();
    protected View mConvertView;

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     *
     * @param viewId
     * @return
     */
    private Object obj;

    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BaseViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public BaseViewHolder setTextColor(int viewID,int colorID){
        TextView view = getView(viewID);
        view.setTextColor(colorID);
        return this;
    }

    public BaseViewHolder setBackRound(int viewId,int drawableID){
        TextView view = getView(viewId);
        view.setBackgroundResource(drawableID);
        return this;
    }

    public BaseViewHolder setText(int viewId, Spanned text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public void setVisible(int viewId, int visible) {
        getView(viewId).setVisibility(visible);
    }
}