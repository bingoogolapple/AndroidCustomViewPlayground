package cn.bingoogolapple.scroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 16:42
 * 描述:
 */
public class Demo2Activity extends AppCompatActivity {
    private Button mButton;
    private LinearLayoutSubClass mLinearLayoutSubClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        mLinearLayoutSubClass = (LinearLayoutSubClass) findViewById(R.id.linearLayoutSubClass);
        mButton = (Button) findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLinearLayoutSubClass.beginScroll();
            }
        });
    }

}
