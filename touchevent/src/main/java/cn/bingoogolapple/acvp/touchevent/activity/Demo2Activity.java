package cn.bingoogolapple.acvp.touchevent.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.acvp.touchevent.R;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/8 下午10:03
 * 描述:
 */
public class Demo2Activity extends AppCompatActivity {
    private RecyclerView mRecyclerView1;
    private RecyclerView mRecyclerView2;
    private RecyclerView mRecyclerView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        mRecyclerView1 = (RecyclerView) findViewById(R.id.recyclerview1);
        mRecyclerView2 = (RecyclerView) findViewById(R.id.recyclerview2);
        mRecyclerView3 = (RecyclerView) findViewById(R.id.recyclerview3);

        mRecyclerView1.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView3.setLayoutManager(new LinearLayoutManager(this));

        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            data.add(i);
        }
        mRecyclerView1.setAdapter(new TestAdapter(mRecyclerView1, data));
        mRecyclerView2.setAdapter(new TestAdapter(mRecyclerView2, data));
        mRecyclerView3.setAdapter(new TestAdapter(mRecyclerView3, data));
    }

    private static final class TestAdapter extends BGARecyclerViewAdapter<Integer> {

        private static final int[] ids = new int[]{R.mipmap.img1, R.mipmap.img2, R.mipmap.img3, R.mipmap.img4, R.mipmap.img5, R.mipmap.img6, R.mipmap.img7};

        public TestAdapter(RecyclerView recyclerView, List<Integer> data) {
            super(recyclerView, R.layout.item);
            setData(data);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, Integer integer) {
            helper.setImageResource(R.id.imageView, ids[((int) (Math.random() * 4))]);
        }
    }

}