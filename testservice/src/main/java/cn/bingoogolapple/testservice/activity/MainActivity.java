package cn.bingoogolapple.testservice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.bingoogolapple.bacvp.BaseActivity;
import cn.bingoogolapple.testservice.R;
import cn.bingoogolapple.testservice.service.TestService;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
    }

    public void testService(View v) {
        Intent intent = new Intent(this, TestService.class);
        intent.putExtra("ext", "我是 MainActivity startService 扩展信息 " + System.currentTimeMillis());
        startService(intent);

        startActivity(new Intent(this, ServiceActivity.class));
    }
}
