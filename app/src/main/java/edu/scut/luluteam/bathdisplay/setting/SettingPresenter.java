package edu.scut.luluteam.bathdisplay.setting;

import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.manager.App;
import edu.scut.luluteam.bathdisplay.manager.SingleSerialPortManager;
import edu.scut.luluteam.bathdisplay.manager.ToastManager;
import edu.scut.luluteam.bathdisplay.manager.WaterMeterManager;
import edu.scut.luluteam.bathdisplay.model.BaseData;
import edu.scut.luluteam.bathdisplay.model.setting.ToiletNameData;
import edu.scut.luluteam.bathdisplay.mqtt.MqttClientManager;
import edu.scut.luluteam.bathdisplay.mqtt.MqttService;
import edu.scut.luluteam.bathdisplay.utils.SharedPreferencesUtil;
import edu.scut.luluteam.serialportlibrary.Device;

/**
 * @author Guan
 * @date Created on 2019/3/21
 */
public class SettingPresenter implements SettingContract.IPresenter {

    private SettingContract.IView mView;
    private static final String TAG = "SettingPresenter";


    public SettingPresenter(SettingContract.IView mView) {
        this.mView = mView;
        this.mView.setPresenter(this);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public String getCurrentSerialPort() {
        return SingleSerialPortManager.getInstance().getCurrentSerialPort();
    }

    @Override
    public List<Device> findSerialPort() {
        return SingleSerialPortManager.getInstance().getDevices();
    }


    @Override
    public void openSerialPort(Device device) {

        SingleSerialPortManager.getInstance().openSerialPort(device.getFile().getPath());
    }

    @Override
    public void setMqttServer(String broker) {
        if (App.isServiceExisted(App.getAppContext(), "edu.scut.luluteam.bathdisplay.mqtt.MqttService")) {
            Intent stopIntent = new Intent(App.getAppContext(), MqttService.class);
            App.getAppContext().stopService(stopIntent);
            Log.e(TAG, "已停止原有连接");
        }
        MqttClientManager.getInstance().setBroker(broker);
        Intent startIntent = new Intent(App.getAppContext(), MqttService.class);
        App.getAppContext().startService(startIntent);
        if (App.isServiceExisted(App.getAppContext(), "edu.scut.luluteam.bathdisplay.mqtt.MqttService")) {
            ToastManager.newInstance(String.format("地址已改为：[%s]！", MqttClientManager.getInstance().getBroker())).isLog(TAG).show();
        } else {
            ToastManager.newInstance("地址更改失败!").isLog(TAG).show();
        }
    }

    @Override
    public void updateWaterAdjustedValue(float value) {
        WaterMeterManager.getInstance().updateWaterAdjustedValue(value);
        //todo
        //限制两位小数，在页面加个提示
    }

    @Override
    public void updatePitNumData(int numManSit, int numManStand, int numWomanSit, int numDisabledSit, int numDisabledStand) {
//        PitNumData pitNumData = new PitNumData(numManSit, numManStand, numWomanSit, numDisabledSit, numDisabledStand);
        AppConstant.pitNumData.Update(numManSit, numManStand, numWomanSit, numDisabledSit, numDisabledStand);
        Log.i(TAG, "更新坑位便池数量" + AppConstant.pitNumData.toJson());
        SharedPreferencesUtil.putString(App.getAppContext(), AppConstant.KEY_SAVE_PIT_NUM_DATA, AppConstant.pitNumData.toJson());
        if (AppConstant.enableCommonWebView) {
            //开启了通用界面的按钮才会发送数据
            EventBus.getDefault().post(new BaseData(AppConstant.pitNumData.toJson(), BaseData.EVENT_BUS_MSG_TYPE_SETTING));
        } else {
            ToastManager.newInstance("该设置仅对通用页面有效。此页面为定制化页面").isLog(TAG).show();
        }
    }

    @Override
    public void updateToiletNameAndAddress(String name, String address) {

        AppConstant.toiletNameData.Update(name, address);
        Log.i(TAG, "更新厕所名和地址" + AppConstant.toiletNameData.toJson());
        SharedPreferencesUtil.putString(App.getAppContext(), AppConstant.KEY_SAVE_TOILET_NAME_DATA, AppConstant.toiletNameData.toJson());
        ToiletNameData toiletNameData = new ToiletNameData(name, address);
        EventBus.getDefault().post(new BaseData(toiletNameData.toJson(), BaseData.EVENT_BUS_MSG_TYPE_SETTING));
        ToastManager.newInstance("更改成功").show();
    }

}
