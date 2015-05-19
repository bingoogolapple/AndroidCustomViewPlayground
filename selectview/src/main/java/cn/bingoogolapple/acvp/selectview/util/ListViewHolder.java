package cn.bingoogolapple.acvp.selectview.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/7 13:17
 * 描述:
 */
public class ListViewHolder extends BaseViewHolder {
    protected int mPosition;

    private ListViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        mPosition = position;
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @param position
     * @return
     */
    public static ListViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ListViewHolder(context, parent, layoutId, position);
        }
        return (ListViewHolder) convertView.getTag();
    }

    public int getPosition() {
        return mPosition;
    }
}