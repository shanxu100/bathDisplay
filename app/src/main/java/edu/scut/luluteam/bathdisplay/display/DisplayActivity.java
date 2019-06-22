package edu.scut.luluteam.bathdisplay.display;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.base.BaseActivity;

/**
 * Activity 在项目中是一个全局控制着，负责创建View以及Presenter，并将两者联系起来
 * View用Fragment实现，实现和Activity的分离
 *
 * @author Guan
 */
public class DisplayActivity extends BaseActivity {

    private DisplayPresenter mPresent;
    private DisplayFragment mFragment;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
//        //应用可在锁屏页面运行
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        //应用运行时，保持屏幕高亮，不锁屏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        //全屏，隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        init();
    }

    private void init() {


        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.contentFrame) == null) {
            mFragment = DisplayFragment.newInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contentFrame, mFragment);
            transaction.commit();
        }
        if (mFragment != null) {
            mPresent = new DisplayPresenter(mFragment);
        }


    }


}
