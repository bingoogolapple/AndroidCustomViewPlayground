package cn.bingoogolapple.androidcommon.adapterview;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.recyclerview.BGAOnItemChildLongClickListener;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/21 上午1:00
 * 描述:
 */
public class BGAAdapterViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected final SparseArray<View> mViews = new SparseArray<View>();
    protected BGAOnItemChildClickListener mOnItemChildClickListener;
    protected BGAOnItemChildLongClickListener mOnItemChildLongClickListener;
    protected View mConvertView;
    protected int mPosition;

    private BGAAdapterViewHolder(Context context, ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    /**
     * 拿到一个可重用的ViewHolder对象
     *
     * @param context
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static BGAAdapterViewHolder dequeueReusableAdapterViewHolder(Context context, View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new BGAAdapterViewHolder(context, parent, layoutId);
        }
        return (BGAAdapterViewHolder) convertView.getTag();
    }

    public void setOnItemChildClickListener(BGAOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    public void setItemChildClickListener(int viewId) {
        getView(viewId).setOnClickListener(this);
    }

    public void setOnItemChildLongClickListener(BGAOnItemChildLongClickListener onItemChildLongClickListener) {
        mOnItemChildLongClickListener = onItemChildLongClickListener;
    }

    public void setItemChildLongClickListener(int viewId) {
        getView(viewId).setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemChildClickListener != null) {
            mOnItemChildClickListener.onItemChildClick(v, getPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemChildLongClickListener != null) {
            return mOnItemChildLongClickListener.onItemChildLongClick(v, getPosition());
        }
        return false;
    }

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

    public BGAAdapterViewHolder setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

}