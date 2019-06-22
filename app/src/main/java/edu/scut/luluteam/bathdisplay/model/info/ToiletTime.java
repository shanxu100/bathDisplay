package edu.scut.luluteam.bathdisplay.model.info;

import edu.scut.luluteam.bathdisplay.utils.TimeUtil;
import edu.scut.luluteam.bathdisplay.utils.TransHelper;

/**
 * @author Guan
 * @date Created on 2019/3/17
 */
public class ToiletTime extends BaseInfo {

    private String year;

    private String month;

    private String day;

    private String hour;
    private String minute;
    private String second;

    public ToiletTime(String deviceGroup, String deviceNum) {
//        this.toiletId = toiletId;
//        this.time = time;
        this.deviceGroup = deviceGroup;
        this.deviceNum = deviceNum;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }


    public static ToiletTime parseByte(byte[] bytes) {
        int offset = 1;
//        String toiletId = TransHelper.getToiletId(bytes, offset, 8);
//        offset += 8;
//        String time = TransHelper.getRecordInfoTime(bytes, offset, 6);
//        offset += 6;
        String deviceGroup = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;
        String deviceNum = TransHelper.getIntValue(bytes, offset, 1) + "";
        offset += 1;

//        String year = String.valueOf(TransHelper.getIntValue(bytes, offset, 1)) +
//                String.valueOf(TransHelper.getIntValue(bytes, offset + 1, 1));
//        offset += 2;
//        String month = String.valueOf(TransHelper.getIntValue(bytes, offset, 1));
//        offset += 1;
//        String day = String.valueOf(TransHelper.getIntValue(bytes, offset, 1));
//        offset += 1;
//        String hour = String.valueOf(TransHelper.getIntValue(bytes, offset, 1));
//        offset += 1;
//        String minute = String.valueOf(TransHelper.getIntValue(bytes, offset, 1));
//        offset += 1;
//        String second = String.valueOf(TransHelper.getIntValue(bytes, offset, 1));
        /**
         * 更改为用bcd编码
         */
        String year = TransHelper.getRecordInfoTime(bytes, offset, 1);
        offset++;
        String month = TransHelper.getRecordInfoTime(bytes, offset, 1);
        offset++;
        String day = TransHelper.getRecordInfoTime(bytes, offset, 1);
        offset++;
        String hour = TransHelper.getRecordInfoTime(bytes, offset, 1);
        offset++;
        String minute = TransHelper.getRecordInfoTime(bytes, offset, 1);
        offset++;
        String second = TransHelper.getRecordInfoTime(bytes, offset, 1);
        ToiletTime toiletTime = new ToiletTime(deviceGroup, deviceNum);
        toiletTime.year = year;
        toiletTime.month = month;
        toiletTime.day = day;
        toiletTime.hour = hour;
        toiletTime.minute = minute;
        toiletTime.second = second;
        return toiletTime;


    }

    @Override
    public String toString() {
        return "ToiletTime{" +
                "year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", hour='" + hour + '\'' +
                ", minute='" + minute + '\'' +
                ", second='" + second + '\'' +
                '}';
    }

    public String toTimeString() {
        return year +
                TimeUtil.num2Str(month) +
                TimeUtil.num2Str(day) +
                "." +
                TimeUtil.num2Str(hour) +

                TimeUtil.num2Str(minute) +
                TimeUtil.num2Str(second);
    }

    public long toTimestamp() {
        return TimeUtil.getTimestamp(year + month + day + hour + minute + second);
    }

}
