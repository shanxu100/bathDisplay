package edu.scut.luluteam.bathdisplay.manager;

import android.content.Context;
import android.content.Intent;

import edu.scut.luluteam.bathdisplay.setting.SettingActivity;

/**
 * 控制页面跳转逻辑
 *
 * @author Guan
 * @date Created on 2019/3/21
 */
public class JumpManager {

    /**
     * 跳转到设置页面
     *
     * @param context
     */
    public static void gotoSettingView(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }
}
