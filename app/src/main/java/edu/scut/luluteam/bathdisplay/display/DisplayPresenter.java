package edu.scut.luluteam.bathdisplay.display;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.constant.MsgConstant;
import edu.scut.luluteam.bathdisplay.manager.SingleSerialPortManager;
import edu.scut.luluteam.bathdisplay.manager.odor.OdorManager;
import edu.scut.luluteam.bathdisplay.model.BaseData;
import edu.scut.luluteam.bathdisplay.model.info.OdorDetector3;
import edu.scut.luluteam.bathdisplay.model.info.TempHumiDetector;
import edu.scut.luluteam.bathdisplay.model.info.ToiletMeter;
import edu.scut.luluteam.bathdisplay.model.info.ToiletPit;
import edu.scut.luluteam.bathdisplay.model.info.ToiletTime;
import edu.scut.luluteam.bathdisplay.model.setting.BaseSetting;
import edu.scut.luluteam.bathdisplay.utils.ByteUtil;
import edu.scut.luluteam.bathdisplay.utils.device.DeviceAdminHelper;
import edu.scut.luluteam.serialportlibrary.Device;

/**
 * @author Guan
 * @date Created on 2019/1/14
 */
public class DisplayPresenter implements DisplayContract.IPresenter {

    private DisplayContract.IView mView;
    private ArrayList<Device> devices = null;


    private static final String TAG = "DisplayPresenter";


    public DisplayPresenter(DisplayContract.IView mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }


    @Override
    public void subscribe() {
        EventBus.getDefault().register(this);

    }

    @Override
    public void unsubscribe() {
        EventBus.getDefault().unregister(this);
        closeSerialPort();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusMessage(BaseData baseData) {

        String json = baseData.getJson();

        if (baseData.getType() == BaseData.EVENT_BUS_MSG_TYPE_DATA) {
            // 处理data类型的数据
            onToiletData(json);
        } else if (baseData.getType() == BaseData.EVENT_BUS_MSG_TYPE_SETTING) {
            //处理设置的数据
            onToiletSetting(json);
        } else {
            Log.e(TAG, "收到EventBus未知类型的数据：json=" + json + "  type=" + baseData.getType());
        }


    }

    /**
     * 处理并显示厕所的各种数据
     *
     * @param json
     */
    private void onToiletData(String json) {
        byte deviceGroup = ByteUtil.toByteArray(Integer.parseInt(JSONObject.parseObject(json).getString("deviceGroup")))[1];
        if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_MAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_WOMAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_DISABLED) {

            ToiletPit toiletPit = new Gson().fromJson(json, ToiletPit.class);
            mView.updateToiletPitView(toiletPit);
            Log.i(TAG, "男女残卫厕蹲位数据");
            //以上逻辑保持不变，是为了应对定制化页面的情况
            //如果是通用页面，需要单独处理残卫的数据
            if (AppConstant.enableCommonWebView && deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_PIT_DISABLED) {
                //TODO 没有对通用页面残卫的deviceNum进行判断。即如果残卫有多个坑位，无论哪个坑位的数据来了，残卫的图表也会跟着变
                //这个问题以后要根据业务进行调整
                mView.updateToiletPitCommonViewForDisabled(toiletPit);
            }

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_METER_ELEC ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_METER_WATER) {
            ToiletMeter toiletMeter = new Gson().fromJson(json, ToiletMeter.class);
            mView.updateToiletMeterView(toiletMeter);
            Log.i(TAG, "水电表数据");

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_MAN ||
                deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_WOMAN) {
            Log.i(TAG, "男女厕气味探测器数据");
            OdorDetector3 detector3 = new Gson().fromJson(json, OdorDetector3.class);
            //将多个气体探测器计算均值后再显示
//            mView.updateToiletOdorDetectorView(detector3);
            OdorManager.getInstance().updateValue(detector3.getDeviceNum(), detector3.getValue());

        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_TEMP_HUMI) {
            TempHumiDetector detector = new Gson().fromJson(json, TempHumiDetector.class);
            mView.updateToieltTempHumiDetectorView(detector);
            Log.i(TAG, "温度探测器数据（不分男女）");
        } else if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_TIME) {
            ToiletTime toiletTime = new Gson().fromJson(json, ToiletTime.class);
            mView.updateToiletTimeView(toiletTime);
            Log.i(TAG, "时间数据数据");
        } else {
            Log.e(TAG, "未识别DeviceGroup=" + deviceGroup);
            return;
        }
    }


    /**
     * 处理设置页面传来的数据
     *
     * @param json
     */
    private void onToiletSetting(String json) {

        Log.i(TAG, "收到设置页面消息：" + json);
        int msgType = Integer.parseInt(JSONObject.parseObject(json).getString("msgType"));
        if (msgType == BaseSetting.SETTING_TYPE_PIT_NUM) {
            //设置坑位数量
//            PitNumData pitNumData = new Gson().fromJson(json, PitNumData.class);
            mView.updatPitNum(json);

        } else if (msgType == BaseSetting.SETTING_TYPE_WATER_ADJUSTED_VALUE) {
            //设置水表校正值
        } else if (msgType == BaseSetting.SETTING_TYPE_TOILET_NAME) {
            mView.upadateToiletName(json);
        }

    }

    /**
     * 这里进行一些初始化的操作
     */
    @Override
    public void loadData() {


    }

    @Override
    public void activeAdmin(Context context) {
        DeviceAdminHelper helper = new DeviceAdminHelper();
        helper.activeAdmin(context);
    }

    /**
     * 打开串口
     */
    @Override
    public void openSerialPort() {
        SingleSerialPortManager.getInstance().openSerialPort(null);
    }


    @Override
    public void closeSerialPort() {
        SingleSerialPortManager.getInstance().closeSerialPort();
    }


    @Override
    public void sendTestMsg() {

        byte[] testBytes = {0x5E, 0x00, 0x03, 0x00, 0x01, 0x02, 0x15, (byte) 0xA4, 0x24};
        SingleSerialPortManager.getInstance().sendBytes(testBytes);

    }

    @Override
    public void updateWaterAdjustedValue(float value) {

        //需要Setting页面传数据过来，再更新view。修改环境变量和保存数据的工作已经在Setting页面保存了
//        AppConstant.waterAdjustedValue=value;
//        SharedPreferencesUtil.putFloat(App.getAppContext(),AppConstant.KEY_SAVE_WATER_ADJUSTED_VALUE,value);
//        mView.updateWaterValueWhenAdjusted(AppConstant.waterOriginalValue + AppConstant.waterAdjustedValue);
    }

    @Override
    public void updateToiletTime() {
        //可能要改，需要在onDestroyView销毁线程
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                if (mView != null) {
                    mView.updateToiletTimeView();
                }
            }
        }, 0, 3, TimeUnit.HOURS);
    }


}
