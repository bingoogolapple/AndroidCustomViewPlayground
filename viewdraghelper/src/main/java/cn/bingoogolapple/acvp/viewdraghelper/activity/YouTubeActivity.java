package cn.bingoogolapple.acvp.viewdraghelper.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.viewdraghelper.R;
import cn.bingoogolapple.acvp.viewdraghelper.widget.YouTubeLayout;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;
import cn.bingoogolapple.bgaannotation.BGAAView;

@BGAALayout(R.layout.activity_youtube)
public class YouTubeActivity extends ActionBarActivity implements AdapterView.OnItemClickListener {
    @BGAAView(R.id.lv_youtube_movice)
    private ListView mMovieLv;

    @BGAAView(R.id.ytbl_youtube_movie)
    private YouTubeLayout mMovieYtbl;
    @BGAAView(R.id.tv_youtube_movie)
    private TextView mMovieTv;
    @BGAAView(R.id.lv_youtube_comment)
    private ListView mCommentLv;

    private List<String> mMovieDatas;
    private List<String> mCommentDatas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);

        initMovieDatas();
        initCommentDatas();

        mMovieLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mMovieDatas));
        mCommentLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mCommentDatas));

        mMovieLv.setOnItemClickListener(this);
    }

    private void initMovieDatas() {
        mMovieDatas = new ArrayList<String>();
        for (int i = 0; i < 40; i++) {
            mMovieDatas.add("电影" + i);
        }
    }

    private void initCommentDatas() {
        mCommentDatas = new ArrayList<String>();
        for (int i = 0; i < 20; i++) {
            mCommentDatas.add("评论" + i);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mMovieTv.setText(mMovieDatas.get(position));
        mMovieYtbl.expand();
    }
}