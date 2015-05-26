package cn.bingoogolapple.androidcommon.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/26 17:06
 * 描述:
 */
public class BGAViewHolderHelper {
    protected final SparseArray<View> mViews = new SparseArray<>();
    protected View mConvertView;

    public BGAViewHolderHelper(View convertView) {
        mConvertView = convertView;
    }

    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews
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

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public BGAViewHolderHelper setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

}