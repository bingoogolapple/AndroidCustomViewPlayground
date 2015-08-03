package cn.bingoogolapple.scroll;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void demo1(View v) {
        startActivity(new Intent(this, Demo1Activity.class));
    }

    public void demo2(View v) {
        startActivity(new Intent(this, Demo2Activity.class));
    }

    public void demo3(View v) {
        startActivity(new Intent(this, Demo3Activity.class));
    }

    public void demo4(View v) {
        startActivity(new Intent(this, Demo4Activity.class));
    }

}