package cn.bingoogolapple.scroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 16:42
 * 描述:
 */
public class Demo6Activity extends AppCompatActivity {
    private BounceTextView textView;
    public static int distance = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo6);
        textView = (BounceTextView) findViewById(R.id.textView);
    }

    public void click(View view) {

        switch (view.getId()) {
            case R.id.btn_scroll_to:
                textView.scrollTo(distance, 0);
                distance += 10;
                break;
            case R.id.btn_scroll_by:
                textView.scrollBy(30, 0);
                break;
            case R.id.btn_sping_back:
                //不知道为什么第一次调用会贴墙，即到达x=0的位置
                textView.spingBack();
                break;
        }

    }

}
