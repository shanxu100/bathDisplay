package edu.scut.luluteam.bathdisplay.model.setting;

import com.google.gson.Gson;

/**
 * @author Guan
 * @date Created on 2019/4/15
 */
public class BaseSetting {

    /**
     * 设置水表校正值的数据
     */
    public static final int SETTING_TYPE_WATER_ADJUSTED_VALUE = 1;

    /**
     * 设置厕所坑位数量的数据
     */
    public static final int SETTING_TYPE_PIT_NUM = 2;

    /**
     * 设置厕所名称及地址的数据
     */
    public static final int SETTING_TYPE_TOILET_NAME = 3;

    public int msgType;

    public BaseSetting(int msgType) {
        this.msgType = msgType;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }
}
