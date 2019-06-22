package edu.scut.luluteam.bathdisplay.manager;

import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.util.Observable;

import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.model.setting.WaterAdjustedValue;
import edu.scut.luluteam.bathdisplay.utils.SharedPreferencesUtil;

/**
 * @author Guan
 * @date Created on 2019/6/19
 */
public class WaterMeterManager extends Observable {
    /**
     * 水表原始值：每次收到水表值都要更新此值
     * 最终显示的值=原始值+校正值
     */
    private float waterOriginalValue;

    /**
     * 水表校正值（可正可负）
     */
    private WaterAdjustedValue waterAdjustedValue = new WaterAdjustedValue(0.0f);

    private static WaterMeterManager instance;
    private static final String TAG = "WaterMeterManager";

    public static WaterMeterManager getInstance() {
        if (instance == null) {
            synchronized (WaterMeterManager.class) {
                if (instance == null) {
                    instance = new WaterMeterManager();
                }
            }
        }
        return instance;
    }

    public float getWaterOriginalValue() {
        return waterOriginalValue;
    }

    public void setWaterOriginalValue(float waterOriginalValue) {
        this.waterOriginalValue = waterOriginalValue;
    }

    public WaterAdjustedValue getWaterAdjustedValue() {
        return waterAdjustedValue;
    }

    public void setWaterAdjustedValue(WaterAdjustedValue waterAdjustedValue) {
        this.waterAdjustedValue = waterAdjustedValue;
    }

    private WaterMeterManager() {

    }

    /**
     * 加载水表校正值
     */
    public void loadWaterAdjustedValue() {
        String json = SharedPreferencesUtil.getString(App.getAppContext(), AppConstant.KEY_SAVE_WATER_ADJUSTED_VALUE);
        if (!StringUtils.isEmpty(json)) {
            waterAdjustedValue = new Gson().fromJson(json, WaterAdjustedValue.class);
        }
    }

    /**
     * 更新并保存水表校正值
     *
     * @param value
     */
    public void updateWaterAdjustedValue(float value) {
//        AppConstant.waterAdjustedValue = new WaterAdjustedValue(value);
        waterAdjustedValue.setValue(value);
        Log.e(TAG, "更新水表校正值：" + value);
        SharedPreferencesUtil.putString(App.getAppContext(), AppConstant.KEY_SAVE_WATER_ADJUSTED_VALUE, waterAdjustedValue.toJson());
        setChanged();
        notifyObservers();
    }

    /**
     * 获取当前需要显示的水表值=原始值+校正值
     *
     * @return
     */
    public float getCurrentWaterValue() {
        float newValue = waterOriginalValue + waterAdjustedValue.getValue();
        String msg = " 水表原始值：" + waterOriginalValue + "  校正值：" + waterAdjustedValue.getValue() + "  矫正后：" + newValue;
        Log.i(TAG, msg);
        if (newValue <= 0) {
            ToastManager.newInstance("水表数值错误：" + msg).setDuration(Toast.LENGTH_LONG).show();
            return 0;
        }
        return newValue;
    }
}
