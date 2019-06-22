package edu.scut.luluteam.bathdisplay.setting;

import java.util.List;

import edu.scut.luluteam.bathdisplay.base.IBasePresenter;
import edu.scut.luluteam.bathdisplay.base.IBaseView;
import edu.scut.luluteam.serialportlibrary.Device;

/**
 * @author Guan
 * @date Created on 2019/3/21
 */
public class SettingContract {

    public interface IView extends IBaseView<IPresenter> {


    }


    public interface IPresenter extends IBasePresenter {


        String getCurrentSerialPort();

        List<Device> findSerialPort();

        void openSerialPort(Device device);

        void setMqttServer(String broker);

        /**
         * 更新水表校正值
         */
        void updateWaterAdjustedValue(float value);

        /**
         * 更新厕所坑位便池数量
         *
         * @param numManSit
         * @param numManStand
         * @param numWomanSit
         * @param numDisabledSit
         * @param numDisabledStand
         */
        void updatePitNumData(int numManSit, int numManStand, int numWomanSit, int numDisabledSit, int numDisabledStand);

        void updateToiletNameAndAddress(String name, String address);

    }


}
