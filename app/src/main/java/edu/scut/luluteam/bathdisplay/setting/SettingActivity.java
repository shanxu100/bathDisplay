package edu.scut.luluteam.bathdisplay.setting;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.base.BaseActivity;

/**
 * @author Guan
 */
public class SettingActivity extends BaseActivity {


    private SettingPresenter mPresent;
    private SettingFragment mFragment;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    private void init() {


        fragmentManager = getSupportFragmentManager();
        if (fragmentManager.findFragmentById(R.id.contentFrame) == null) {
            mFragment = SettingFragment.newInstance();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.contentFrame, mFragment);
            transaction.commit();
        }
        if (mFragment != null) {
            mPresent = new SettingPresenter(mFragment);
        }


    }

}
