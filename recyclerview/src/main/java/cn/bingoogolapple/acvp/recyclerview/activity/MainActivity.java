package cn.bingoogolapple.acvp.recyclerview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.bingoogolapple.acvp.recyclerview.R;
import cn.bingoogolapple.bgaannotation.BGAA;
import cn.bingoogolapple.bgaannotation.BGAALayout;

@BGAALayout(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BGAA.injectView2Activity(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_main_helloworld:
                startActivity(new Intent(this, HelloworldActivity.class));
                break;
            case R.id.btn_main_helloworld2:
                startActivity(new Intent(this, Helloworld2Activity.class));
                break;
            case R.id.btn_main_headindex:
                startActivity(new Intent(this, HeadindexActivity.class));
                break;
            case R.id.btn_main_download:
                startActivity(new Intent(this, DownloadActivity.class));
                break;
            case R.id.btn_main_decoration:
                startActivity(new Intent(this, DecorationActivity.class));
                break;
            case R.id.btn_main_testdialog:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Dialog");
                builder.setMessage("少数派客户端");
                builder.setPositiveButton("OK", null);
                builder.setNegativeButton("Cancel", null);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.show();
                break;

        }
    }

}