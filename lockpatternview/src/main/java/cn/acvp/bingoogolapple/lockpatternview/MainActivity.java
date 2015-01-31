package cn.acvp.bingoogolapple.lockpatternview;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private TextView mResultTv;
    private LockPatternView mContentLpv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResultTv = (TextView) findViewById(R.id.tv_main_result);
        mContentLpv = (LockPatternView) findViewById(R.id.lpv_main_content);
        mContentLpv.setOnPatternChangeListener(new LockPatternView.OnPatternChangeListener() {
            @Override
            public void onPatternChange(String password) {
                if(password == null) {
                    mResultTv.setText("至少5个图案");
                } else {
                    mResultTv.setText(password);
                }
            }

            @Override
            public void onPatternStart(boolean isRestart) {
                if(isRestart) {
                    mResultTv.setText("请绘制图案");
                }
            }
        });
    }

}