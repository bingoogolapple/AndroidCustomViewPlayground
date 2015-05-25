package cn.bingoogolapple.androidcommon.adapterview;

import android.view.View;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:03
 * 描述:
 */
public class DMJAdapterViewSwipeHolder extends BGAAdapterViewBaseHolder {

    public DMJAdapterViewSwipeHolder(View convertView) {
        mConvertView = convertView;
        mConvertView.setTag(this);
    }

}