package edu.scut.luluteam.bathdisplay.constant;


/**
 * @author Guan
 * @date 2017/9/18
 */
public class MsgConstant {

    /**
     * 帧头和帧尾
     */
    public static final byte FRAME_SOF = 0x5E;
    public static final byte FRAME_EOF = 0x24;


    public static final byte MSG_TYPE = 0x50;

    //==================================================================
    //DeviceGroup相关字段
    //==================================================================

    public static final byte DEVICE_GROUP_TOILET_PIT_MAN = 0x01;
    public static final byte DEVICE_GROUP_TOILET_PIT_WOMAN = 0x02;
    public static final byte DEVICE_GROUP_TOILET_PIT_DISABLED = 0x03;
    public static final byte DEVICE_GROUP_TOILET_METER_ELEC = 0x04;
    public static final byte DEVICE_GROUP_TOILET_METER_WATER = 0x05;
    /**
     * 气体探测器--男厕
     */
    public static final byte DEVICE_GROUP_TOILET_DETECTOR_ODOR_MAN = 0x06;
    /**
     * 气体探测器--女厕
     */
    public static final byte DEVICE_GROUP_TOILET_DETECTOR_ODOR_WOMAN = 0x09;
    /**
     * 温度湿度探测器
     */
    public static final byte DEVICE_GROUP_TOILET_DETECTOR_TEMP_HUMI = 0x07;
    public static final byte DEVICE_GROUP_TOILET_TIME = 0x08;

    //======================================================================================
    //deviceNum相关自字段
    //======================================================================================

    public static final byte DEVICE_NUM_TOILET_DETECTOR_ODOR_NH3 = 0x01;
    public static final byte DEVICE_NUM_TOILET_DETECTOR_ODOR_H2S = 0x02;

    //======================================================================================
    //
    //======================================================================================
    // 0x00表示无人，所以是可用的，表示true
    public static final byte STATE_AVAILABLE = 0x00;
    public static final byte STATE_UNAVAILABLE = 0x01;

    /**
     * 判断设备是否正常工作
     * 正常0x00 异常0xff
     */
    public static final byte STATE_WORK = 0x00;
    public static final byte STATE_NOT_WORK = (byte) 0xff;

    public static final int MAN_TOTAL_NUM = 6;
    public static final int WOMAN_TOTAL_NUM = 5;


}
