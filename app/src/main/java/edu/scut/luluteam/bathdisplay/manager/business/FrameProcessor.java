package edu.scut.luluteam.bathdisplay.manager.business;

import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.scut.luluteam.bathdisplay.constant.MsgConstant;
import edu.scut.luluteam.bathdisplay.manager.ToastManager;
import edu.scut.luluteam.bathdisplay.model.BaseData;
import edu.scut.luluteam.bathdisplay.model.CustomFrame;
import edu.scut.luluteam.bathdisplay.model.info.BaseInfo;
import edu.scut.luluteam.bathdisplay.model.info.OdorDetector3;
import edu.scut.luluteam.bathdisplay.model.info.TempHumiDetector;
import edu.scut.luluteam.bathdisplay.model.info.ToiletMeter;
import edu.scut.luluteam.bathdisplay.model.info.ToiletPit;
import edu.scut.luluteam.bathdisplay.model.info.ToiletTime;
import edu.scut.luluteam.bathdisplay.utils.ByteUtil;
import edu.scut.luluteam.bathdisplay.utils.CheckCRC;

/**
 * 多线程处理业务逻辑
 */
public class FrameProcessor {
    private static final String TAG = "FrameProcessor";

    private static ExecutorService executorService = Executors.newFixedThreadPool(2);


    public static void process(final CustomFrame customFrame) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                if (customFrame == null) {
                    return;
                }
                if (!checkCRC(customFrame)) {
                    reportError(customFrame.toBytes(), "CRC16 Check Error");
                    return;
                }
                //处理数据部分
                onMessage(customFrame);
            }
        });


    }

    /**
     * 做具体的业务处理
     *
     * @param customFrame
     */
    private static void onMessage(CustomFrame customFrame) {

        Log.i(TAG, "识别一个帧：" + ByteUtil.byte2hex(customFrame.toBytes()));

        byte[] msg = customFrame.getData();
//        if (msg.length < ) {
//            //最小是16，否则deviceGroup字段都没有了
//            return;
//        }
        byte deviceGroup = msg[1];

        BaseInfo baseInfo = null;
        if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_MAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_WOMAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_DISABLED) {
            baseInfo = ToiletPit.parseByte(msg);
            Log.i(TAG, "男女残卫厕蹲位数据");
        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_METER_ELEC ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_METER_WATER) {
            baseInfo = ToiletMeter.parseByte(msg);
            Log.i(TAG, "水电表数据");

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_MAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_WOMAN) {

            baseInfo = OdorDetector3.parseByte(msg);

            Log.i(TAG, "男女厕气味探测器数据");

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_TEMP_HUMI) {
            baseInfo = TempHumiDetector.parseByte(msg);
            Log.i(TAG, "温度探测器数据（不分男女）");

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_TIME) {
            baseInfo = ToiletTime.parseByte(msg);
            Log.i(TAG, "时间数据数据");
        } else {
            Log.e(TAG, "未识别DeviceGroup=" + deviceGroup);
            return;
        }
        if (baseInfo != null) {
            EventBus.getDefault().post(new BaseData(baseInfo.toJson(), BaseData.EVENT_BUS_MSG_TYPE_DATA));
        }

    }

    /**
     * 校验CRC
     *
     * @param customFrame
     * @return
     */
    private static boolean checkCRC(CustomFrame customFrame) {
        byte[] length_data = new byte[CustomFrame.LENGTH_SIZE + customFrame.getIntLength()];
        System.arraycopy(customFrame.getLength(), 0, length_data, 0, CustomFrame.LENGTH_SIZE);
        System.arraycopy(customFrame.getData(), 0, length_data, CustomFrame.LENGTH_SIZE, customFrame.getIntLength());
        return CheckCRC.check(length_data, customFrame.getCrc16());
    }

    /**
     * 报告错误，并将 帧 打印出来
     *
     * @param data
     * @param errInfo
     */
    protected static void reportError(byte[] data, String errInfo) {
        String frame_str = ByteUtil.byte2hex(data);
        ToastManager.newInstance("CustomFrame Error... " + errInfo + " Frame: " + frame_str)
                .isLog(TAG).show();
    }
}
