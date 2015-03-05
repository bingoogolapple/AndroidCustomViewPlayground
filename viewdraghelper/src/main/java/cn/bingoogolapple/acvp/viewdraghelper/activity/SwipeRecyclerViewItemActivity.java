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

    @BGAAView(R.id.swiperecyclerviewitem_left)
    private SwipeRecyclerViewItem mLeftSrvi;
    @BGAAView(R.id.swiperecyclerviewitem_right)
    private SwipeRecyclerViewItem mRightSrvi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        findViewById(R.id.btn_swipeitem_topview).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SwipeRecyclerViewItemActivity.this,"长按了TopView中的按钮",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        findViewById(R.id.btn_swipeitem_bottomview).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SwipeRecyclerViewItemActivity.this,"长按了BottomView中的按钮",Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_swipeitem_topview:
                Toast.makeText(this, "点击了TopView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_swipeitem_bottomview:
                Toast.makeText(this, "点击了BottomView", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_swipeitem_openleft:
                mLeftSrvi.open();
                break;
            case R.id.btn_swipeitem_closeleft:
                mLeftSrvi.close();
                break;

            case R.id.btn_swipeitem_openright:
                mRightSrvi.open();
                break;
            case R.id.btn_swipeitem_closeright:
                mRightSrvi.close();
                break;
        }
    }
}