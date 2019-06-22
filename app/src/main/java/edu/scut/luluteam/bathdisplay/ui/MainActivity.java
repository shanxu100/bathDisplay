package edu.scut.luluteam.bathdisplay.ui;

import android.os.Bundle;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.base.BaseActivity;

/**
 * 主页面改为了DisplayActivity
 *
 * @author Guan
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
