package edu.scut.luluteam.bathdisplay.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * @author Guan
 * @date Created on 2019/4/3
 */
public class DisplayImgAdapter extends PagerAdapter {

    private static final String TAG = "DisplayImgAdapter";

    private List<ImageView> imageViews;

    public DisplayImgAdapter(List<ImageView> imageViews) {
        this.imageViews = imageViews;
    }


    @Override
    public int getCount() {
        return imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int realPosition = position % imageViews.size();
//        Log.e(TAG, "position=" + position);
        ImageView imageView = imageViews.get(realPosition);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
