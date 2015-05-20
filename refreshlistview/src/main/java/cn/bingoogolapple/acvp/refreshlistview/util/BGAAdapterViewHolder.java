package cn.bingoogolapple.acvp.refreshlistview.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:00
 * 描述:
 */
public class BGAAdapterViewHolder extends BGAViewHolder {
    private int mPosition;

    private BGAAdapterViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
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
    public static BGAAdapterViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new BGAAdapterViewHolder(context, parent, layoutId, position);
        }
        return (BGAAdapterViewHolder) convertView.getTag();
    }

    public int getPosition() {
        return mPosition;
    }

}