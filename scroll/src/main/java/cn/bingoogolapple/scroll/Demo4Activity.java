package cn.bingoogolapple.scroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 16:42
 * 描述:
 */
public class Demo4Activity extends AppCompatActivity {
    private ListView mListLv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo4);

        mListLv = (ListView) findViewById(R.id.lv_lottery_list);
        mListLv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"14082241期", "14082242期", "14082243期" }));
        findViewById(R.id.btn_lottery_test).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "内部按钮能获取到焦点", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
