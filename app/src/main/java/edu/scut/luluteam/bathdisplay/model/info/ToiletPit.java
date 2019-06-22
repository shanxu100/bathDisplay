package edu.scut.luluteam.bathdisplay.model.info;

import edu.scut.luluteam.bathdisplay.utils.TransHelper;

/**
 * @author Guan
 * @date Created on 2019/3/17
 */
public class ToiletPit extends BaseInfo {

    /**
     * true:可用，即無人
     * false：不可用，即有人
     */
    private boolean available;

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public ToiletPit(String deviceGroup, String deviceNum) {
//        this.toiletId = toiletId;
//        this.time = time;
        this.deviceGroup = deviceGroup;
        this.deviceNum = deviceNum;
    }

    public static ToiletPit parseByte(byte[] bytes) {
        int offset = 1;
//        String toiletId = TransHelper.getToiletId(bytes, offset, 8);
//        offset += 8;
//        String time = TransHelper.getRecordInfoTime(bytes, offset, 6);
//        offset += 6;
        String deviceGroup = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        String deviceNum = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        boolean available = (bytes[offset] == 0x00);
        offset += 1;
        ToiletPit toiletPit = new ToiletPit(deviceGroup, deviceNum);
        toiletPit.available = available;
        return toiletPit;

    }
}
