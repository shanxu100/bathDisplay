package edu.scut.luluteam.bathdisplay.manager;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.util.List;

import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.model.setting.PitNumData;
import edu.scut.luluteam.bathdisplay.model.setting.ToiletNameData;
import edu.scut.luluteam.bathdisplay.mqtt.MqttService;
import edu.scut.luluteam.bathdisplay.utils.SharedPreferencesUtil;


/**
 * Created by TC on 2017/12/8.
 */

public class App extends Application {

    private static Context appContext;
    private static App mInstance;


    /**
     * 获取实例
     *
     * @return
     */
    public static App getInstance() {

        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        mInstance = this;
        init();
        AppConstant.HUMAN_FLOW_NUM = SharedPreferencesUtil.getInt(getAppContext(), AppConstant.HUMAN_FLOW_SHARE);
        AppConstant.CURRENT_DATE = SharedPreferencesUtil.getString(getAppContext(), AppConstant.CURRENT_DATE_SHARE, AppConstant.CURRENT_DATE);
        loadWaterAdjustedValue();
        loadCommonPitNumData();
        loadToiletNameData();
    }


    @Override
    public void onTerminate() {

        super.onTerminate();
    }


    private void init() {
        //启用mqtt
        Intent mqttIntent = new Intent(this, MqttService.class);
        startService(mqttIntent);
    }

    /**
     * 加载水表校正值
     */
    private void loadWaterAdjustedValue() {
//        String json = SharedPreferencesUtil.getString(getAppContext(), AppConstant.KEY_SAVE_WATER_ADJUSTED_VALUE);
//        if (!StringUtils.isEmpty(json)) {
//            AppConstant.waterAdjustedValue = new Gson().fromJson(json, WaterAdjustedValue.class);
//        }
        WaterMeterManager.getInstance().loadWaterAdjustedValue();
    }

    /**
     * 加载通用页面中的坑位数量值
     */
    private void loadCommonPitNumData() {
        String json = SharedPreferencesUtil.getString(getAppContext(), AppConstant.KEY_SAVE_PIT_NUM_DATA);
        if (AppConstant.enableCommonWebView && !StringUtils.isEmpty(json)) {
            AppConstant.pitNumData = new Gson().fromJson(json, PitNumData.class);
        }
    }

    /**
     * 加载通用页面中的厕所名和地址
     */
    private void loadToiletNameData() {
        String json = SharedPreferencesUtil.getString(getAppContext(), AppConstant.KEY_SAVE_TOILET_NAME_DATA);
        if (!StringUtils.isEmpty(json)) {
            AppConstant.toiletNameData = new Gson().fromJson(json, ToiletNameData.class);
        }
    }

    public static Context getAppContext() {
        return appContext;
    }

    /**
     * 版本号
     *
     * @return
     */
    public static int getVersionCode() {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = appContext.getPackageManager();
            packageInfo = pm.getPackageInfo(appContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public static String getVersionName()//获取版本号
    {
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown version name";
        }
    }

    /**
     * 判断app是否处于前台
     *
     * @return
     */
    public boolean isAppForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(getPackageName()) &&
                    processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager
                .getRunningServices(Integer.MAX_VALUE);

        if (!(serviceList.size() > 0)) {
            return false;
        }

        for (int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if (serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }


}
