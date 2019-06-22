package edu.scut.luluteam.bathdisplay.model.info;

import edu.scut.luluteam.bathdisplay.utils.TransHelper;

/**
 * @author Guan
 * @date Created on 2019/3/17
 */
public class ToiletMeter extends BaseInfo {

    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ToiletMeter(String deviceGroup, String deviceNum) {
//        this.toiletId = toiletId;
//        this.time = time;
        this.deviceGroup = deviceGroup;
        this.deviceNum = deviceNum;
    }


    public static ToiletMeter parseByte(byte[] bytes) {
        int offset = 1;
//        String toiletId = TransHelper.getToiletId(bytes, offset, 8);
//        offset += 8;
//        String time = TransHelper.getRecordInfoTime(bytes, offset, 6);
//        offset += 6;
        String deviceGroup = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        String deviceNum = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        int value = TransHelper.getIntValue(bytes, offset, 4);
        offset += 4;
        ToiletMeter toiletMeter = new ToiletMeter(deviceGroup, deviceNum);
        toiletMeter.value = value;
        return toiletMeter;
    }
}
