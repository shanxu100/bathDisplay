package edu.scut.luluteam.bathdisplay.display;

import android.content.Context;

import edu.scut.luluteam.bathdisplay.base.IBasePresenter;
import edu.scut.luluteam.bathdisplay.base.IBaseView;
import edu.scut.luluteam.bathdisplay.manager.odor.OdorManager;
import edu.scut.luluteam.bathdisplay.model.info.OdorDetector3;
import edu.scut.luluteam.bathdisplay.model.info.TempHumiDetector;
import edu.scut.luluteam.bathdisplay.model.info.ToiletMeter;
import edu.scut.luluteam.bathdisplay.model.info.ToiletPit;
import edu.scut.luluteam.bathdisplay.model.info.ToiletTime;

/**
 * @author Guan
 * @date Created on 2019/1/14
 */
public class DisplayContract {

    public interface IView extends IBaseView<IPresenter> {


        void updateToiletPitView(ToiletPit toiletPit);

        /**
         * 只用于更新通用页面的残卫部分数据
         *
         * @param toiletPit
         */
        void updateToiletPitCommonViewForDisabled(ToiletPit toiletPit);

        void updateToiletMeterView(ToiletMeter waterMeter);


        /**
         * 更新气体探测器显示
         */
        @Deprecated
        void updateToiletOdorDetectorView(OdorDetector3 detector3);

        void updateOdorDetectorView(OdorManager.OdorArg arg);


        /**
         * 更新厕所温度湿度显示
         *
         * @param detector
         */
        void updateToieltTempHumiDetectorView(TempHumiDetector detector);

        /**
         * 收到时间数据帧后校正
         * 废弃了此条数据协议，因此该方法也就废弃
         *
         * @param toiletTime
         */
        @Deprecated
        void updateToiletTimeView(ToiletTime toiletTime);

        /**
         * 获取系统时间进行显示
         * 用于替代 updateToiletTimeView(ToiletTime toiletTime) 方法
         */
        void updateToiletTimeView();

        void updateWaterValueWhenAdjusted(float value);

        void updatPitNum(String json);

        void upadateToiletName(String json);
    }


    public interface IPresenter extends IBasePresenter {

        /**
         * 一些初始化操作
         */
        void loadData();


        /**
         * 激活管理员
         *
         * @param context
         */
        void activeAdmin(Context context);


        /**
         * 打开串口
         */
        void openSerialPort();


        /**
         * 关闭串口
         */
        void closeSerialPort();


        @Deprecated
        void sendTestMsg();

        /**
         * 更新水表校正值
         */
        @Deprecated
        void updateWaterAdjustedValue(float value);

        void updateToiletTime();

    }

}
