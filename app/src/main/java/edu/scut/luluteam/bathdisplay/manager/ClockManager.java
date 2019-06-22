package edu.scut.luluteam.bathdisplay.manager;

import android.app.AlarmManager;
import android.content.Context;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Calendar;

/**
 * @author Guan
 * @date Created on 2019/3/21
 */
public class ClockManager {

    public static long getTime(int mode, int year, int month, int day,
                               int hour, int minute, int second) {
        Calendar c = Calendar.getInstance();
        if (mode == Calendar.AM) {
            c.set(Calendar.AM_PM, Calendar.AM);
        } else {
            c.set(Calendar.AM_PM, Calendar.PM);
        }
        c.set(Calendar.YEAR, year + 2000);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        c.set(Calendar.HOUR, hour);//HOUR_OF_DAY
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTimeInMillis();
    }


    /**
     * 设置android系统时间
     * 需要如下权限
     * <uses-permission android:name="android.permission.SET_TIME" />
     * <uses-permission android:name="android.permission.SET_TIME_ZONE" />
     *
     * @param mode
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param minute
     * @param second
     * @param mContext
     */
    public static void setAndroidSystemTime(int mode, int year, int month, int day,
                                            int hour, int minute, int second, Context mContext) {
        Calendar c = Calendar.getInstance();
        if (mode == Calendar.AM) {
            c.set(Calendar.AM_PM, Calendar.AM);
        } else {
            c.set(Calendar.AM_PM, Calendar.PM);
        }
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);

        c.set(Calendar.HOUR, hour);//HOUR_OF_DAY
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, second);
        c.set(Calendar.MILLISECOND, 0);

        long when = c.getTimeInMillis();

        if (when / 1000 < Integer.MAX_VALUE) {
            AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setTime(when);
        }
    }


    /**
     * root权限下设置系统时间
     *
     * @param time 格式为“年月日.时分秒”，例如：20111209.121212
     */
    public static boolean setTimeWithRoot(String time) {
        Process process = null;
        DataOutputStream os = null;
        try {
//              process = new ProcessBuilder()
//                    .command("/system/xbin/su")
//                    .redirectErrorStream(true).start();
////            process = Runtime.getRuntime().exec("/system/xbin/su");
//            os = new DataOutputStream(process.getOutputStream());
////            os.writeBytes("date -s " + time + "\n");
////            os.writeBytes("exit\n");
////            os.flush();
//            os.writeBytes("setprop persist.sys.timezone GMT\n");
//            os.writeBytes("/system/bin/date -s "+time+"\n");
//            os.writeBytes("clock -w\n");
//            os.writeBytes("exit\n");
//            os.flush();
//            process.waitFor();
            process = Runtime.getRuntime().exec("su");
//          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT+8:00\n");
            os.writeBytes("date -s " + time + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            ToastManager.newInstance("请添加root权限").show();
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }


}
