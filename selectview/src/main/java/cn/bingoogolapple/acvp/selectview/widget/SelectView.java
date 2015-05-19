package cn.bingoogolapple.acvp.selectview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;

import cn.bingoogolapple.acvp.selectview.R;
import cn.bingoogolapple.acvp.selectview.model.CascadeModel;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/5/17 10:39
 * 描述:
 */
public class SelectView extends EditText implements AdapterView.OnItemClickListener {
    private static final String TAG = SelectView.class.getSimpleName();
    private Drawable mArrowDrawable;
    private SelectViewDelegate mDelegate;
    private String mDefaultValue;

    private PopupWindow mValuePw;
    private ListView mListView;
    private CascadeModel mCascadeModel;

    public SelectView(Context context) {
        super(context);
    }

    public SelectView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initArrowDrawable();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectView);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initAttr(int attr, TypedArray typedArray) {
        switch (attr) {
            case R.styleable.SelectView_sv_defaultValue:
                mDefaultValue = typedArray.getString(attr);
                setText(mDefaultValue);
                break;
            case R.styleable.SelectView_sv_arrowImg:
                mArrowDrawable = typedArray.getDrawable(attr);
                break;
            default:
                break;
        }
    }

    private void initArrowDrawable() {
        setEnabled(false);
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.END);
        if (mArrowDrawable == null) {
            mArrowDrawable = getResources().getDrawable(R.mipmap.down_arrow);
        }
        mArrowDrawable.setBounds(0, 0, mArrowDrawable.getIntrinsicWidth(), mArrowDrawable.getIntrinsicHeight());
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], mArrowDrawable, getCompoundDrawables()[3]);
    }

    public void reset() {
        setText(mDefaultValue);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                boolean touchable = event.getX() > (getWidth() - getPaddingRight() - mArrowDrawable.getIntrinsicWidth()) && (event.getX() < getWidth() - getPaddingRight());
                if (touchable) {
                    onClickArrow();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private void onClickArrow() {
        showValuePw();
    }

    private void showValuePw() {
        if (mValuePw == null) {
            mValuePw = new PopupWindow(getListView(), getWidth(), getHeight() * 4, true);
            mValuePw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        if (getListView().getAdapter().getCount() > 0) {
            mValuePw.showAsDropDown(this, 0, 0);
        }
    }

    private void closeValuePw() {
        if (mValuePw != null) {
            mValuePw.dismiss();
        }
    }

    public ListView getListView() {
        if (mListView == null) {
            mListView = new ListView(getContext());
            mListView.setDivider(null);
            mListView.setDividerHeight(0);
            mListView.setOnItemClickListener(this);
        }
        return mListView;
    }

    public void setAdapter(BaseAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    public void setDelegate(SelectViewDelegate delegate) {
        mDelegate = delegate;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        closeValuePw();
        if (mDelegate != null) {
            mDelegate.onSelectViewValueChanged(this, position);
        }
    }

    public interface SelectViewDelegate {
        void onSelectViewValueChanged(SelectView selectView, int position);
    }

}