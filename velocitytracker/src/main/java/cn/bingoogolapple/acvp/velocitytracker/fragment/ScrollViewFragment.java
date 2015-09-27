package cn.bingoogolapple.acvp.velocitytracker.fragment;

import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.acvp.velocitytracker.R;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/9/27 下午12:53
 * 描述:
 */
public class ScrollViewFragment extends BaseFragment {

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_scrollview);
    }

    @Override
    protected void setListener() {
        getViewById(R.id.tv_scrollview_clickablelabel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("点击了测试文本");
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
    }

    @Override
    protected void onUserVisible() {
    }
}
