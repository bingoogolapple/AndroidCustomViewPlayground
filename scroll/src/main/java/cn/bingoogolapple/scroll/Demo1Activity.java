package cn.bingoogolapple.scroll;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 作者:王浩 邮件:bingoogolapple@gmail.com
 * 创建时间:15/8/3 16:42
 * 描述:
 */

/**
 * Demo描述:
 * scrollTo()和scrollBy()的理解以及使用
 * <p/>
 * 原理说明:
 * 1 其实View是没有边界,在屏幕上看到的只是View的一部分而已
 * 2 scrollTo()和scrollBy()的本质一样只是表现形式略有不同
 * 与这两个方法密切相关的两个变量mScrollX和mScrollY在
 * View的源码中可以看到:
 * <p/>
 * //The offset,in pixels,by which the content of this view is scrolled horizontally.
 * protected int mScrollX;
 * <p/>
 * //The offset,in pixels,by which the content of this view is scrolled vertically.
 * protected int mScrollY;
 * <p/>
 * 通过文档描述可知:
 * mScrollX和mScrollY表示:View的内容(content)相对于View本身在水平或垂直方向的偏移量.
 * <p/>
 * scrollTo(int x, int y)方法:
 * 将一个视图的内容移动到指定位置.此时偏移量 mScrollX,mScrollY就分别等于x,y.
 * 默认情况下 mScrollX和mScrollY均为0
 * <p/>
 * scrollBy(int x, int y)方法:
 * 在现有的基础上继续移动视图的内容.
 * 在该方法的源码很简单,也体现了这一点,如下:
 * public void scrollBy(int x, int y) {
 * scrollTo(mScrollX + x, mScrollY + y);
 * }
 * 默认情况下 mScrollX和mScrollY均为0
 * <p/>
 * 再次强调和注意:
 * scrollTo()和scrollBy()移动的只是View的内容,但是View的背景是不移动的.
 * 为了体现这点,在该示例中为View添加了背景色.
 * <p/>
 * 继续上面问题的延伸:
 * 假如一个ViewGroup(比如XXXLayout)调用了scrollTo(By)()方法会怎样?
 * 它的Content(即它所有的子View)都会移动,这点在下个例子中可以看到.
 * <p/>
 * <p/>
 * 3 scrollTo(int x,int y)和scrollBy(int x,int y)方法的坐标说明
 * 比如我们对于一个TextView调用scrollTo(0,25)
 * 那么该TextView中的content(比如显示的文字:Hello)会怎么移动呢?
 * 向下移动25个单位?不,恰好相反.
 * 这是为什么呢?
 * 因为调用这两个方法会导致视图重绘.
 * 即调用public void invalidate(int l, int t, int r, int b)方法.
 * 此处的l,t,r,b四个参数就表示View原来的坐标.
 * 在该方法中最终会调用:
 * tmpr.set(l - scrollX, t - scrollY, r - scrollX, b - scrollY);
 * p.invalidateChild(this, tmpr);
 * 其中tmpr是ViewParent,tmpr是Rect,this是原来的View.
 * 通过这两行代码就把View在一个Rect中重绘.
 * 请注意第一行代码:
 * 原来的l和r均减去了scrollX
 * 原来的t和b均减去了scrollY
 * 就是说scrollX如果是正值,那么重绘后的View的宽度反而减少了;反之同理
 * 就是说scrollY如果是正值,那么重绘后的View的高度反而减少了;反之同理
 * 所以,TextView调用scrollTo(0,25)和我们的理解相反
 * <p/>
 * scrollBy(int x,int y)方法与上类似,不再赘述.
 * <p/>
 * <p/>
 * 该示例的说明,请参加下面的代码注释
 * <p/>
 * <p/>
 * 参考资料:
 * 1 http://blog.csdn.net/wangjinyu501/article/details/32339379
 * 2 http://blog.csdn.net/qinjuning/article/details/7247126
 * Thank you very much
 * <p/>
 * <p/>
 * 备注说明:
 * 使用scrollTo(By)()方法移动过程较快而且比较生硬.
 * 为了优化scrollTo(By)()的滑动过程可采用Scroller类.
 * 该类源码第一句This class encapsulates scrolling.
 * 就指明了该类的目的:封装了滑动过程.
 * 在后面的示例中,将学习到Scroller的使用.
 */
public class Demo1Activity extends AppCompatActivity {
    private ViewGroup mViewGroup;
    private TextView mTextView;
    private Button mLeftButton;
    private Button mRightButon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo1);
        init();
    }

    private void init() {
        mViewGroup = (ViewGroup) findViewById(R.id.viewGroup);
        mTextView = (TextView) findViewById(R.id.textView);
        mLeftButton = (Button) findViewById(R.id.leftButton);
        mLeftButton.setOnClickListener(new ClickListenerImpl());
        mRightButon = (Button) findViewById(R.id.rightButton);
        mRightButon.setOnClickListener(new ClickListenerImpl());
    }

    /**
     * 示例说明:
     * 1 每次点击leftButton的时候
     * 1.1 调用scrollBy()让mTextView的内容(即文字)在原本偏移的基础上往左移30
     * 1.2 调用scrollBy()让mLeftButton的内容(即文字)在原本偏移的基础上也往左移30
     * 2 每次点击rightButton的时候
     * 2.1 调用scrollTo()让mTextView的内容(即文字)直接往右偏移30,而不管以前的基础(即 mScrollX和mScrollY)
     * 3 连续几次点击leftButton会看到mTextView的内容(即文字)每点一次都会往左移动30,
     * 然后再点击一次rightButton会看到mTextView的内容(即文字)直接一次性到了往右30的位置,而
     * 不是慢慢移动过去.
     * 这么操作
     * 1 很好的体现了这两个方法的区别.
     * 2 直观地看了scrollTo()方法的效用,它是不管以前的偏移量的.
     * 4 在该例中也可以看到调用这两个方法时,View的背景是没有移动.移动的是内容.
     */
    private class ClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leftButton:
                    // 让mTextView的内容往左移
                    mTextView.scrollBy(30, 0);
                    // 让mLeftButton的内容也往左移
                    mLeftButton.scrollBy(20, 0);

                    mViewGroup.scrollBy(0, -20);
                    break;
                case R.id.rightButton:
                    // 让mTextView的内容往右移直接到-30的位置
                    mTextView.scrollTo(-30, 0);
                    break;
                default:
                    break;
            }
        }

    }


    // 相反的，往左往上传正值，往下往右传负值
}