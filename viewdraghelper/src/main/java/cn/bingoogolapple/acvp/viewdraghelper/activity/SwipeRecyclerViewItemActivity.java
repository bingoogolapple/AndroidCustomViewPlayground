package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.acvp.viewdraghelper.widget.SwipeRecyclerViewItem;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_swiperecyclerviewitem)
public class SwipeRecyclerViewItemActivity extends ActionBarActivity {

    @BGAAView(R.id.swiperecyclerviewitem)
    private SwipeRecyclerViewItem mSwipeItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        findViewById(R.id.iv_swipeitem_star).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SwipeRecyclerViewItemActivity.this, "长按了星星", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findViewById(R.id.iv_swipeitem_delete).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SwipeRecyclerViewItemActivity.this, "长按了删除", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_swipeitem_star:
                Toast.makeText(this, "点击了星星", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_swipeitem_delete:
                Toast.makeText(this, "点击了删除", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_swipeitem_open:
                mSwipeItem.open();
                break;
            case R.id.btn_swipeitem_close:
                mSwipeItem.close();
                break;
            case R.id.btn_swipeitem_status:
                showStatus(mSwipeItem.getStatus());
                break;
        }
    }

    private void showStatus(SwipeRecyclerViewItem.Status status) {
        if (status == SwipeRecyclerViewItem.Status.Opened) {
            Toast.makeText(this, "打开状态", Toast.LENGTH_SHORT).show();
        } else if(status == SwipeRecyclerViewItem.Status.Closed) {
            Toast.makeText(this, "关闭状态", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "正在移动", Toast.LENGTH_SHORT).show();
        }
    }
}