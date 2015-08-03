package cn.bingoogolapple.verticalpagescrollview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private VerticalPageScrollView mScrollView;
    private RadioGroup mRadioGroup;

    private int[] ivIds = {R.mipmap.iv1, R.mipmap.iv2, R.mipmap.iv3, R.mipmap.iv4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        mScrollView = (VerticalPageScrollView) findViewById(R.id.scrollView);

        initRadioGroup();
        initScrollView();
    }

    private void initRadioGroup() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main_1:
                        mScrollView.setCurrentItem(0);
                        break;
                    case R.id.rb_main_2:
                        mScrollView.setCurrentItem(1);
                        break;
                    case R.id.rb_main_3:
                        mScrollView.setCurrentItem(2);
                        break;
                    case R.id.rb_main_4:
                        mScrollView.setCurrentItem(3);
                        break;
                    case R.id.rb_main_5:
                        mScrollView.setCurrentItem(4);
                        break;
                }
            }
        });
    }

    private void initScrollView() {
        for (int ivId : ivIds) {
            ImageView iv = new ImageView(this);
            iv.setBackgroundResource(ivId);
            mScrollView.addView(iv);
        }
        View page5 = View.inflate(this, R.layout.view_page5, null);
        page5.findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "点击了测试按钮", Toast.LENGTH_SHORT).show();
            }
        });
        mScrollView.addView(page5);


        mScrollView.setDelegate(new VerticalPageScrollView.VerticalPageScrollViewDelegate() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mRadioGroup.check(R.id.rb_main_1);
                        break;
                    case 1:
                        mRadioGroup.check(R.id.rb_main_2);
                        break;
                    case 2:
                        mRadioGroup.check(R.id.rb_main_3);
                        break;
                    case 3:
                        mRadioGroup.check(R.id.rb_main_4);
                        break;
                    case 4:
                        mRadioGroup.check(R.id.rb_main_5);
                        break;
                }
            }
        });
    }

}