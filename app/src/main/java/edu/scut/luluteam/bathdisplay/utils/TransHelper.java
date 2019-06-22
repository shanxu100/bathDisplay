package edu.scut.luluteam.bathdisplay.utils;


import android.util.Log;

import edu.scut.luluteam.bathdisplay.constant.MsgConstant;


/**
 * 完成  字节<--->字段  之间转换的帮助类
 *
 * @author Guan
 * @date 2017/9/21
 */
public class TransHelper {

    private static final String TAG = "TransHelper";

    //==========toiletId 的转换======================

    /**
     * ToiletId : byte[] --> String
     *
     * @param bytes
     * @param offset
     * @param count
     * @return
     */
    public static String getToiletId(byte[] bytes, int offset, int count) {

        byte[] cardIdBytes = new byte[count];
        System.arraycopy(bytes, offset, cardIdBytes, 0, count);
        String cardId = ByteUtil.byte2string(cardIdBytes);
        // 此处应该把cardId转换为toiletId
//        String toiletId = cardId;
        return cardId;
    }

    /**
     * StringId : byte[] --> String
     *
     * @param bytes
     * @param offset
     * @param count
     * @return
     */
    public static String getStringId(byte[] bytes, int offset, int count) {

        byte[] tempBytes = new byte[count];
        System.arraycopy(bytes, offset, tempBytes, 0, count);
        return ByteUtil.byte2string(tempBytes);
    }

    /**
     * cardId : String --> byte[]
     *
     * @param catdId
     * @return
     */
    public static byte[] getToiletIdBytes(String catdId) {
        byte[] byteArr = ByteUtil.string2bytes(catdId);
        if (byteArr.length == 8) {
            return byteArr;
        } else {
            return ByteUtil.string2bytes("00000000");
        }
    }

    /**
     * CityId: String --> byte[]
     *
     * @param cid
     * @return
     */
    public static byte[] getCityIdBytes(String cid) {
        byte[] byteArr = ByteUtil.string2bytes(cid);
        if (byteArr.length == 9) {
            return byteArr;
        } else {
            return ByteUtil.string2bytes("000000000");
        }
    }


    //==========================


    //===========================================

    /**
     * 转换时间 yyMMddhhmmss
     * byte[] --> String
     *
     * @param bytes
     * @param offset
     * @param count
     * @return
     */
    public static String getRecordInfoTime(byte[] bytes, int offset, int count) {
        byte[] timeBytes = new byte[count];
        System.arraycopy(bytes, offset, timeBytes, 0, count);
        return ByteUtil.bcdToStr(timeBytes);
    }

    /**
     * 转换时间 yyMMddhhmmss
     * String --> byte[]
     *
     * @param time
     * @return
     */
    public static byte[] getSystemTime(String time) {
        return ByteUtil.strToBcd(time);
    }

    //=======================================

    /**
     * 判断坑位是否可用
     *
     * @param b
     * @return
     */
    public static boolean isAvailable(byte b) {
        if (b == MsgConstant.STATE_AVAILABLE) {
            return true;
        } else if (b == MsgConstant.STATE_UNAVAILABLE) {
            return false;
        }
        return false;
    }

    public static boolean isWork(byte b) {
        if (b == MsgConstant.STATE_WORK) {
            return true;
        } else if (b == MsgConstant.STATE_NOT_WORK) {
            return false;
        }
        return false;
    }

    /**
     * Byte:  bit   bit    bit  bit bit bit bit bit
     * index :7     6       5   4   3   2   1   0
     *
     * @param b
     * @param index
     * @return true：1   false：0
     */
    public static boolean getBitAction(Byte b, int index) {
        return ((b >> index) & 0x01) != 0;
    }

    /**
     * Byte:  bit   bit    bit  bit bit bit bit bit
     * -       7     6       5   4   3   2   1   0
     * -       --------    --------  ------  ------
     * index:     3             2       1       0
     *
     * @param b
     * @param index
     * @return
     */
    public static int get2BitValue(Byte b, int index) {
        return ((b >> index * 2) & 0x03);
    }


    //===================================================================

    /**
     * 长度、探头值（数值）
     * byte[] --> int
     *
     * @param bytes
     * @param offset
     * @param count
     * @return
     */
    public static int getIntValue(byte[] bytes, int offset, int count) {
        int res = 0;
        if (count == 1) {
            res = ByteUtil.toInteger((byte) 0x00, bytes[offset]);
            offset += count;
        } else if (count == 2) {
            res = ByteUtil.toInteger(bytes[offset], bytes[offset + 1]);
            offset += count;
        } else if (count == 4) {
            res = ByteUtil.toInteger(bytes[offset], bytes[offset + 1], bytes[offset + 2], bytes[offset + 3]);
            offset += count;
        } else {
            Log.e(TAG, "表示探头值（数值）需要2个字节，表示水电表读数需要4个字节。但是count=" + count);

        }

        return res;
    }


    /**
     * 温度：int --> byte
     *
     * @param tmperature
     * @return
     */
    public static byte getTempByte(int tmperature) {
        if (tmperature > 128 || tmperature < -127) {
            tmperature = 0;
        }
        byte b = (byte) tmperature;
        return b;
    }

    /**
     * 天气代码: condCode --> byte
     *
     * @param condCode
     * @return
     */
    public static byte[] getCondByte(int condCode) {
        return ByteUtil.toByteArray(condCode);
    }

}
