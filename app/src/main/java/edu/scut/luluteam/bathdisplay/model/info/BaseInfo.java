package edu.scut.luluteam.bathdisplay.model.info;

import com.google.gson.Gson;

import edu.scut.luluteam.bathdisplay.constant.MsgConstant;

/**
 * @author Guan
 * @date Created on 2019/3/17
 */
public class BaseInfo {

    protected byte msgType = MsgConstant.MSG_TYPE;

    /**
     * 原数据帧中 0x01 --> "1"; 0x02 --> "2";
     * 以此类推
     * 0x01 --> 男厕
     * 0x02 --> 女厕
     * 0x03 --> 残卫
     * 0x04 --> 电表
     * 0x05 --> 水表
     * 0x06--> 气体传感器（氨气、硫化氢）
     * 0x07 --> 时间设备
     */
    protected String deviceGroup;

    /**
     * 即为index
     * 当值为1~60时，表示男厕蹲位控制器编码
     * 当值为61~90时，表示男厕便池控制器编码
     * 当值为171 ~ 180时，表示男厕过道冲水控制器
     * <p>
     * 当值为91~150时，表示女厕蹲位控制器编码
     * 当值为181~190时，表示女厕过道冲水控制器
     * <p>
     * 当值为151~160时，表示第三卫生间蹲位控制器编码
     * 当值为161~170时，表示第三卫生间便池控制器编码
     * 当值为191~200时，表示第三卫生间过道冲水控制器
     * <p>
     * 当值为223时，表示 电表 读数数据
     * <p>
     * 当值为224时，表示 水表 读数数据
     * <p>
     * 当值为225时，表示温度湿度传感器读数数据
     * <p>
     * 201表示男厕第一个NH3传感器，
     * 202表示男厕第一个H2S传感器
     * 203表示男厕第二个NH3传感器，
     * 204表示男厕第二个H2S传感器
     * 205表示男厕第三个NH3传感器，
     * 206表示男厕第三个H2S传感器
     * <p>
     * 208表示女厕第一个NH3传感器，
     * 209表示女厕第一个H2S传感器
     * 210表示女厕第二个NH3传感器，
     * 211表示女厕第二个H2S传感器
     * 212表示女厕第三个NH3传感器，
     * 213表示女厕第三个H2S传感器
     * <p>
     * 214表示第三卫生间第一个NH3传感器，
     * 215表示第三卫生间第一个H2S传感器
     */
    protected String deviceNum;

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public String getDeviceGroup() {
        return deviceGroup;
    }

    public void setDeviceGroup(String deviceGroup) {
        this.deviceGroup = deviceGroup;
    }

    public String getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(String deviceNum) {
        this.deviceNum = deviceNum;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }


}
