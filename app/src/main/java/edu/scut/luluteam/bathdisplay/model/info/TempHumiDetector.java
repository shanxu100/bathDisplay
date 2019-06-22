package edu.scut.luluteam.bathdisplay.model.info;

import edu.scut.luluteam.bathdisplay.utils.TransHelper;

/**
 * 温度湿度探测器
 *
 * @author Guan
 * @date Created on 2019/3/29
 */
public class TempHumiDetector extends BaseInfo {

    private int temperature;

    private int humidity;

    private boolean stateTemperature;

    private boolean stateHumidity;

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public boolean isStateTemperature() {
        return stateTemperature;
    }

    public void setStateTemperature(boolean stateTemperature) {
        this.stateTemperature = stateTemperature;
    }

    public boolean isStateHumidity() {
        return stateHumidity;
    }

    public void setStateHumidity(boolean stateHumidity) {
        this.stateHumidity = stateHumidity;
    }

    public TempHumiDetector(String deviceGroup, String deviceNum) {
//        this.toiletId = toiletId;
//        this.time = time;
        this.deviceGroup = deviceGroup;
        this.deviceNum = deviceNum;
    }

    public static TempHumiDetector parseByte(byte[] bytes) {
        //解析基本字段
        int offset = 1;
//        String toiletId = TransHelper.getToiletId(bytes, offset, 8);
//        offset += 8;
//        String time = TransHelper.getRecordInfoTime(bytes, offset, 6);
//        offset += 6;
        String deviceGroup = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        String deviceNum = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        TempHumiDetector detector = new TempHumiDetector(deviceGroup, deviceNum);

        //解析温度湿度
        int temperature = TransHelper.getIntValue(bytes, offset, 2);
        offset += 2;
        int humidity = TransHelper.getIntValue(bytes, offset, 2);
        offset += 2;
        detector.temperature = temperature;
        detector.humidity = humidity;


        //解析探测器状态
        detector.stateTemperature = TransHelper.isWork(bytes[offset]);
        offset++;
        detector.stateHumidity = TransHelper.isWork(bytes[offset]);
        offset++;

        return detector;
    }

    /**
     * 是否存在故障设备
     *
     * @return
     */
    public boolean hasNotWorkDevice() {
        boolean result = stateTemperature && stateHumidity;
        return !result;
    }
}
