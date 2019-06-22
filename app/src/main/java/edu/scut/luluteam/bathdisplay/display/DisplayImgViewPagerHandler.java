package edu.scut.luluteam.bathdisplay.display;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;

/**
 * @author Guan
 * @date Created on 2019/4/10
 */
public class DisplayImgViewPagerHandler extends Handler {
    private static final String TAG = "ImgViewPagerHandler";


    private ViewPager viewPager;
    private int size;
    private long delay;

    public DisplayImgViewPagerHandler(ViewPager viewPager, int size, long delay) {
        this.viewPager = viewPager;
        this.size = size;
        this.delay = delay;
    }

    public void start() {
        sendEmptyMessage(0);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int item = (viewPager.getCurrentItem() + 1) % size;
        viewPager.setCurrentItem(item);
        //使用这个方法实现循环调用
        sendEmptyMessageDelayed(0, delay);
    }
}
