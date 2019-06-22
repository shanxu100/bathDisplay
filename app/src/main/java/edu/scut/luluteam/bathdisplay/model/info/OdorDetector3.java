package edu.scut.luluteam.bathdisplay.model.info;

import edu.scut.luluteam.bathdisplay.utils.TransHelper;

/**
 * @author Guan
 * @date Created on 2019/3/30
 */
public class OdorDetector3 extends BaseInfo {

    private int value;
    private boolean state;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean hasNotWorkDevice() {
        return !state;
    }

    public OdorDetector3(String deviceGroup, String deviceNum) {
//        this.toiletId = toiletId;
//        this.time = time;
        this.deviceGroup = deviceGroup;
        this.deviceNum = deviceNum;
    }

    public static OdorDetector3 parseByte(byte[] bytes) {
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
        OdorDetector3 detector = new OdorDetector3(deviceGroup, deviceNum);

        //解析探测器数值字段
        detector.value = TransHelper.getIntValue(bytes, offset, 2);
        offset += 2;
        detector.state = TransHelper.isWork(bytes[offset]);
        offset++;

        return detector;
    }
}
