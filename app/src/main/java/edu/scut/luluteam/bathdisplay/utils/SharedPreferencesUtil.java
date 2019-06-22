package edu.scut.luluteam.bathdisplay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by JJY on 2016/9/14.
 */
public class SharedPreferencesUtil {
    public static SharedPreferences mySharedPreferences;

    public static void putString(Context context, String key, String value) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        String content = mySharedPreferences.getString(key, "");
        return content;
    }

    public static String getString(Context context, String key, String defaultStr) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        String content = mySharedPreferences.getString(key, defaultStr);
        return content;
    }

    public static void putInt(Context context, String key, int num) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putInt(key, num);
        editor.commit();
    }

    public static int getInt(Context context, String key) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        int content = mySharedPreferences.getInt(key, 0);
        return content;
    }

    public static void putFloat(Context context, String key, float num) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putFloat(key, num);
        editor.commit();
    }

    public static float getFloat(Context context, String key) {
        mySharedPreferences = context.getSharedPreferences("test", Activity.MODE_PRIVATE);
        mySharedPreferences = context.getSharedPreferences("test",
                Activity.MODE_PRIVATE);
        float content = mySharedPreferences.getFloat(key, 0.0f);
        return content;
    }

}
