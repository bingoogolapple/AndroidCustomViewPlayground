package cn.bingoogolapple.acvp.indexview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    private IndexView mIndexView;
    private ListView mListView;
    private TextView mTipTv;
    private List<Model> mDatas;
    private PinyinComparator mPinyinComparator;
    private CharacterParser mCharacterParser;
    private SortAdapter mSortAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndexView = (IndexView) findViewById(R.id.indexview);
        mListView = (ListView) findViewById(android.R.id.list);
        mTipTv = (TextView) findViewById(R.id.tv_main_tip);

        mIndexView.setTipTv(mTipTv);
        mPinyinComparator = new PinyinComparator();
        mCharacterParser = CharacterParser.getInstance();

        mDatas = new ArrayList<Model>();

        mDatas.add(new Model("安阳"));
        mDatas.add(new Model("鞍山"));
        mDatas.add(new Model("保定"));
        mDatas.add(new Model("包头"));
        mDatas.add(new Model("北京"));
        mDatas.add(new Model("北海"));
        mDatas.add(new Model("宝鸡"));
        mDatas.add(new Model("本兮"));
        mDatas.add(new Model("滨州"));
        mDatas.add(new Model("常州"));
        mDatas.add(new Model("常德"));
        mDatas.add(new Model("常熟"));
        mDatas.add(new Model("成都"));
        mDatas.add(new Model("承德"));
        mDatas.add(new Model("沧州"));
        mDatas.add(new Model("重庆"));
        mDatas.add(new Model("东莞"));
        mDatas.add(new Model("大庆"));
        mDatas.add(new Model("佛山"));
        mDatas.add(new Model("广州"));
        mDatas.add(new Model("合肥"));
        mDatas.add(new Model("海口"));
        mDatas.add(new Model("济南"));
        mDatas.add(new Model("兰州"));
        mDatas.add(new Model("南京"));
        mDatas.add(new Model("泉州"));
        mDatas.add(new Model("荣成"));
        mDatas.add(new Model("三亚"));
        mDatas.add(new Model("上海"));
        mDatas.add(new Model("汕头"));
        mDatas.add(new Model("天津"));
        mDatas.add(new Model("武汉"));
        mDatas.add(new Model("厦门"));
        mDatas.add(new Model("宜宾"));
        mDatas.add(new Model("张家界"));
        mDatas.add(new Model("自贡"));

        handleTopc();
        Collections.sort(mDatas, mPinyinComparator);
        mSortAdapter = new SortAdapter(mDatas, this);
        mListView.setAdapter(mSortAdapter);
        setListener();
    }

    private void setListener() {
        mIndexView.setOnChangedListener(new IndexView.OnChangedListener() {
            @Override
            public void onChanged(String text) {
                int position = mSortAdapter.getPositionForSection(text.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }
        });
    }

    public void handleTopc() {
        for (Model model : mDatas) {
            model.topc = mCharacterParser.getSelling(model.name).substring(0, 1).toUpperCase();
            if (model.name.equals("重庆")) {
                model.topc = "C";
            }
        }
    }

    private static class SortAdapter extends BaseAdapter implements SectionIndexer {
        private List<Model> mDatas;
        private Context mContext;

        public SortAdapter(List<Model> datas, Context context) {
            mDatas = datas;
            mContext = context;
        }

        public int getSectionForPosition(int position) {
            return mDatas.get(position).topc.charAt(0);
        }

        public int getPositionForSection(int section) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = mDatas.get(i).topc;
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = null;
            if (convertView == null) {
                textView = new TextView(mContext);
                textView.setTextSize(30);
                textView.setPadding(20, 20, 20, 20);
            } else {
                textView = (TextView) convertView;
            }
            textView.setText(((Model) getItem(position)).name);
            return textView;
        }
    }

}