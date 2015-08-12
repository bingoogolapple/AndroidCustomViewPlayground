package cn.bingoogolapple.acvp.velocitytracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import cn.bingoogolapple.acvp.velocitytracker.adapter.SwipeRecyclerViewAdapter;
import cn.bingoogolapple.acvp.velocitytracker.engine.DataEngine;
import cn.bingoogolapple.acvp.velocitytracker.model.RefreshModel;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemLongClickListener;

public class MainActivity extends AppCompatActivity implements BGAOnRVItemClickListener, BGAOnRVItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BGAStickyNavRefreshLayout mStickyNavRefreshLayout;
    private RecyclerView mDataRv;
    private SwipeRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        setListener();
        processLogic();
    }

    private void initView() {
        mStickyNavRefreshLayout = (BGAStickyNavRefreshLayout) findViewById(R.id.stickyNavRefreshLayout);
        mDataRv = (RecyclerView) findViewById(R.id.rv_main_data);
    }

    private void setListener() {
        mAdapter = new SwipeRecyclerViewAdapter(this);
        mAdapter.setOnRVItemClickListener(this);
        mAdapter.setOnRVItemLongClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);

        mDataRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }
        });
    }

    private void processLogic() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDataRv.setLayoutManager(linearLayoutManager);

        mDataRv.setAdapter(mAdapter);

        DataEngine.loadInitDatas(new DataEngine.RefreshModelResponseHandler() {
            @Override
            public void onFailure() {
            }

            @Override
            public void onSuccess(List<RefreshModel> refreshModels) {
                mAdapter.setDatas(refreshModels);
            }
        });
    }

    @Override
    public void onRVItemClick(View v, int position) {
        showToast("点击了条目 " + mAdapter.getItem(position).title);
    }

    @Override
    public boolean onRVItemLongClick(View v, int position) {
        showToast("长按了条目 " + mAdapter.getItem(position).title);
        return true;
    }

    @Override
    public void onItemChildClick(View v, int position) {
        if (v.getId() == R.id.tv_item_swipe_delete) {
            mAdapter.removeItem(position);
        }
    }

    @Override
    public boolean onItemChildLongClick(View v, int position) {
        if (v.getId() == R.id.tv_item_swipe_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).title);
            return true;
        }
        return false;
    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}