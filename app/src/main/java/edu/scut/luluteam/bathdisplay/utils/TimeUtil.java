package edu.scut.luluteam.bathdisplay.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Guan
 */
public class TimeUtil {
    private TimeUtil() {
        throw new AssertionError("工具类不可实例化");
    }

    /**
     * 根据指定格式，获取当前日期
     *
     * @param pattern
     * @return
     */
    public static String getTime(String pattern) {
        SimpleDateFormat format;
        if (pattern != null) {
            format = new SimpleDateFormat(pattern);
            return format.format(new Date());
        }
        return null;
    }

    /**
     * 根据指定格式，获取当前日期
     *
     * @param pattern
     * @return
     */
    public static String getTime(String pattern, long timestamp) {
        SimpleDateFormat format;
        if (pattern != null) {
            format = new SimpleDateFormat(pattern);
            return format.format(new Date(timestamp));
        }
        return null;
    }

    /**
     * 根据指定格式转换 日期显示格式
     *
     * @param inputTime
     * @param pattern_old
     * @param pattern_new
     * @return
     * @throws ParseException
     */
    public static String getTime(String inputTime, String pattern_old, String pattern_new) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(pattern_old);
        Date date = format.parse(inputTime);
        format.applyPattern(pattern_new);
        return format.format(date);
    }

    /**
     * 根据yyMMdd，获取 yyMMdd000000 的时间戳
     *
     * @return
     */
    public static String getTimestampOnDay(String yy, String MM, String day) {
        String pattern = "yyMMdd";
        String yyMMdd = getTime(pattern);
        return yyMMdd + "000000";
    }

    /**
     * 获取 距离今天， before天前的日期
     *
     * @param before x天前
     * @return
     */
    public static String getYyMMdd0000(int before) {
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(new Date());
        //距离指定日期当前的日期
        theCa.add(Calendar.DATE, 0 - before);
        Date beforDate = theCa.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyMMdd");
        return format.format(beforDate.getTime()) + "000000";
    }

    /**
     * HHmmss --> ms
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long hHmmssToMs(String time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            return format.parse(time).getTime();

        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取时间戳
     *
     * @param yyMMddHHmmss
     * @return
     */
    public static long getTimestamp(String yyMMddHHmmss) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
            return format.parse(yyMMddHHmmss).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static long getTimestamp(String pattern, String timeStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            return format.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * x 天之前的时间戳
     *
     * @param x
     * @return
     */
    public static long xDaysAgo(int x) {
        return System.currentTimeMillis() - x * 24 * 60 * 60 * 1000;
    }

    public static String secondToHHmmss(int second) {
        return secondToHHmmss(second, "HHmmss");

    }

    /**
     * 将距离零点秒数时间转为相应时间格式
     *
     * @param second
     * @param pattern
     * @return
     */
    public static String secondToHHmmss(int second, String pattern) {
        Date date = new Date(second * 1000);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        format.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        return format.format(date);
    }

    /**
     * 计算当前时间与一个时间点的时间差（秒），如果当前时间点已经过了，那么就算第二天这个时间点与当前时间的时间差
     *
     * @return 必须为正数
     */
    public static int calculateDiffOfNowAndOneTime(String HHmmss) {
        SimpleDateFormat format = new SimpleDateFormat("HHmmss");
        try {
            Date inputDate = format.parse(HHmmss);
            Date now = format.parse(format.format(new Date()));
            if (now.before(inputDate)) {
                return (int) ((inputDate.getTime() - now.getTime()) / 1000);
            } else {
                return 24 * 3600 - (int) ((now.getTime() - inputDate.getTime()) / 1000);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 将1-9前面补0
     */
    public static String num2Str(int num) {
        if (num > 0 && num < 10) {
            return "0" + num;
        } else {
            return num + "";
        }
    }

    public static String num2Str(String string) {
        int num = Integer.parseInt(string);
        return num2Str(num);
    }


    /**
     * 测试用
     *
     * @param args
     */
    public static void main(String[] args) {
//        System.out.println(hHmmssToMs("1:00", "HH:mm"));
////        System.out.println(getTimestamp("180101000000"));
//
////        System.out.print(secondToHHmmss(3600));
//        System.out.print(getYyMMdd0000(7));
        System.out.println(calculateDiffOfNowAndOneTime("211600"));

    }

}
