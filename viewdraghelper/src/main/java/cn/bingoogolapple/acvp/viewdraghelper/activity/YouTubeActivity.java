package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.acvp.viewdraghelper.widget.YouTubeLayout;
import cn.bingoogolapple.bacvp.BaseActivity;

public class YouTubeActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private ListView mMovieLv;

    private YouTubeLayout mMovieYtbl;
    private TextView mMovieTv;
    private ListView mCommentLv;

    private List<String> mMovieDatas;
    private List<String> mCommentDatas;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_youtube);
        mMovieLv = getViewById(R.id.lv_youtube_movie);
        mMovieYtbl = getViewById(R.id.ytbl_youtube_movie);
        mMovieTv = getViewById(R.id.tv_youtube_movie);
        mCommentLv = getViewById(R.id.lv_youtube_comment);
    }

    @Override
    protected void setListener() {
        mMovieLv.setOnItemClickListener(this);
        mCommentLv.setOnItemClickListener(this);

        mMovieYtbl.setScaleCallback(new YouTubeLayout.ScaleCallback() {
            @Override
            public void onScale(float scale) {
                ViewHelper.setAlpha(mMovieLv, scale);
            }
        });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {
        initMovieDatas();
        initCommentDatas();

        mMovieLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mMovieDatas));
        mCommentLv.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mCommentDatas));
    }

    private void initMovieDatas() {
        mMovieDatas = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            mMovieDatas.add("电影" + i);
        }
    }

    private void initCommentDatas() {
        mCommentDatas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            mCommentDatas.add("评论" + i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.equals(mMovieLv)) {
            mMovieTv.setText(mMovieDatas.get(position));
            mMovieYtbl.expand();
        } else if (parent.equals(mCommentLv)) {
            Toast.makeText(this, "点击了" + mCommentDatas.get(position), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_youtube_testclickheader:
                Toast.makeText(this, "点击了头部按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_youtube_testclickfooter:
                Toast.makeText(this, "点击了底部按钮", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_youtube_testclickbottom:
                Toast.makeText(this, "点击了底层按钮", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}