package edu.scut.luluteam.bathdisplay.display;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.adapter.DisplayImgAdapter;
import edu.scut.luluteam.bathdisplay.constant.AppConstant;
import edu.scut.luluteam.bathdisplay.constant.MsgConstant;
import edu.scut.luluteam.bathdisplay.manager.App;
import edu.scut.luluteam.bathdisplay.manager.JumpManager;
import edu.scut.luluteam.bathdisplay.manager.ToastManager;
import edu.scut.luluteam.bathdisplay.manager.WaterMeterManager;
import edu.scut.luluteam.bathdisplay.manager.odor.OdorManager;
import edu.scut.luluteam.bathdisplay.model.info.OdorDetector3;
import edu.scut.luluteam.bathdisplay.model.info.TempHumiDetector;
import edu.scut.luluteam.bathdisplay.model.info.ToiletMeter;
import edu.scut.luluteam.bathdisplay.model.info.ToiletPit;
import edu.scut.luluteam.bathdisplay.model.info.ToiletTime;
import edu.scut.luluteam.bathdisplay.model.setting.ToiletNameData;
import edu.scut.luluteam.bathdisplay.ui.TextClock;
import edu.scut.luluteam.bathdisplay.ui.webview.WebViewManager;
import edu.scut.luluteam.bathdisplay.utils.ByteUtil;
import edu.scut.luluteam.bathdisplay.utils.SharedPreferencesUtil;

import static edu.scut.luluteam.bathdisplay.manager.App.getAppContext;

/**
 * @author Guan
 * @date Created on 2019/1/14
 */
public class DisplayFragment extends Fragment implements DisplayContract.IView, Observer {

    private DisplayContract.IPresenter mPresenter;
    private static final String TAG = "DisplayFragment";


    private FrameLayout fl_webview;
    private WebViewManager webviewManager;
    private TextView tv_men_toilet_remain;
    private TextView tv_women_toilet_remain;
    private TextView tv_water_meter;
    private TextView tv_electric_meter;
    private TextView tv_temperature;
    private TextView tv_humidity;
    private TextView tv_man_nh3;
    private TextView tv_man_h2s;
    private TextView tv_woman_nh3;
    private TextView tv_woman_h2s;
    private TextView tv_headcount;
    private TextView tv_men_toilet_count;
    private TextView tv_women_toilet_count;

    private TextView tv_toilet_name;
    private TextView tv_toilet_address;


    private Button btn_setting;

    private TextClock textClockDate;
    private TextClock textClockTime;

    private ImageView iv_disable_state;

    /**
     * 显示故障配备的提示
     */
    private ImageView iv_man_detector_state;
    private ImageView iv_woman_detector_state;

    /**
     * viewpager图片轮播的相关控件
     */
    private ViewPager viewPager;
    private LinearLayout ll_bottom_img;
    private DisplayImgViewPagerHandler mHander;


    public DisplayFragment() {
        // Required empty public constructor
    }

    public static DisplayFragment newInstance() {
        DisplayFragment fragment = new DisplayFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_display_v2, container, false);

        initUI(view);

        //进行一系列的初始化
        mPresenter.subscribe();

        mPresenter.openSerialPort();
        mPresenter.updateToiletTime();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    /**
     * 进行UI方面的初始化操作
     *
     * @param view
     */
    private void initUI(View view) {
        tv_men_toilet_remain = (TextView) view.findViewById(R.id.tv_men_toilet_remain);
        tv_women_toilet_remain = (TextView) view.findViewById(R.id.tv_women_toilet_remain);
        tv_water_meter = (TextView) view.findViewById(R.id.tv_water_meter);
        tv_electric_meter = (TextView) view.findViewById(R.id.tv_electric_meter);
        tv_temperature = (TextView) view.findViewById(R.id.tv_temperature);
        tv_humidity = (TextView) view.findViewById(R.id.tv_humidity);
        tv_man_nh3 = (TextView) view.findViewById(R.id.tv_man_nh3);
        tv_man_h2s = (TextView) view.findViewById(R.id.tv_man_h2s);
        tv_woman_nh3 = (TextView) view.findViewById(R.id.tv_woman_nh3);
        tv_woman_h2s = (TextView) view.findViewById(R.id.tv_woman_h2s);
        tv_headcount = (TextView) view.findViewById(R.id.tv_headcount);

        tv_toilet_name = (TextView) view.findViewById(R.id.tv_toilet_name);
        tv_toilet_address = (TextView) view.findViewById(R.id.tv_manage);

        tv_men_toilet_count = (TextView) view.findViewById(R.id.tv_men_toilet_count);
        tv_women_toilet_count = (TextView) view.findViewById(R.id.tv_women_toilet_count);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        ll_bottom_img = (LinearLayout) view.findViewById(R.id.ll_bottom_img);

        fl_webview = (FrameLayout) view.findViewById(R.id.fl_webview);
        //加载之前设置的厕所名和地址
        if (!StringUtils.isEmpty(AppConstant.toiletNameData.toJson())) {
            if (!StringUtils.isEmpty(AppConstant.toiletNameData.getToiletName())
                    || !StringUtils.isEmpty(AppConstant.toiletNameData.getToiletAddress())) {
                upadateToiletName(AppConstant.toiletNameData.toJson());
            }
        }


        //加载之前设置的厕所名和地址（等webview加载完成后）
        webviewManager = new WebViewManager(getContext(), fl_webview, AppConstant.WebViewUrl, new WebViewManager.WebViewLoadInterface() {
            @Override
            public void onLoadFinished() {
                String pitNumJson = AppConstant.pitNumData.toJson();
                Log.e(TAG, "坑位数据为:" + pitNumJson);
                if (!StringUtils.isEmpty(pitNumJson)) {
                    //webview加载完成后，刷新坑位数量
                    updatPitNum(pitNumJson);
                }
            }
        });

        webviewManager.initWebView();

        btn_setting = (Button) view.findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpManager.gotoSettingView(getContext());
            }
        });

        textClockDate = (TextClock) view.findViewById(R.id.tc_time_date);
        textClockTime = (TextClock) view.findViewById(R.id.tc_time_clock);
        iv_man_detector_state = (ImageView) view.findViewById(R.id.iv_man_detector_state);
        iv_woman_detector_state = (ImageView) view.findViewById(R.id.iv_woman_detector_state);
        if (AppConstant.enableCommonWebView) {
            iv_disable_state = (ImageView) view.findViewById(R.id.iv_disable_state);
        }


        tv_headcount.setText(AppConstant.HUMAN_FLOW_NUM + "人");
//        tv_men_toilet_remain.setText(MsgConstant.MAN_TOTAL_NUM + "");
//        tv_women_toilet_remain.setText(MsgConstant.WOMAN_TOTAL_NUM + "");
//        tv_men_toilet_count.setText(MsgConstant.MAN_TOTAL_NUM + "");
//        tv_women_toilet_count.setText(MsgConstant.WOMAN_TOTAL_NUM + "");

        tv_men_toilet_count.setText(AppConstant.pitNumData.getNumManSit() + AppConstant.pitNumData.getNumManStand() + "");
        tv_women_toilet_count.setText(AppConstant.pitNumData.getNumWomanSit() + "");

        textClockDate.setListener(new TextClock.OneSecondListener() {
            @Override
            public void onChanged(long currentMillTime) {
                AppConstant.HUMAN_FLOW_NUM = 0;
                tv_headcount.setText(AppConstant.HUMAN_FLOW_NUM + "人");
                SharedPreferencesUtil.putInt(getAppContext(), AppConstant.HUMAN_FLOW_SHARE, AppConstant.HUMAN_FLOW_NUM);
            }
        });
        textClockTime.start();
        textClockDate.start();

        turnOnViewPager();

        OdorManager.getInstance().addObserver(this);
        WaterMeterManager.getInstance().addObserver(this);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁
        mPresenter.unsubscribe();
        if (mHander != null) {
            mHander.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void setPresenter(DisplayContract.IPresenter presenter) {
        this.mPresenter = presenter;
    }


    @Override
    public void updateToiletPitView(ToiletPit toiletPit) {

        String pitJson = toiletPit.toJson();
        webviewManager.onNewMsg("androidCallFunction", pitJson, new WebViewManager.WebViewCallback() {
            @Override
            public void onValue(String json) {
                try {
                    if (StringUtils.isEmpty(json) || "null".equals(json)) {
                        return;
                    }
                    String s = json.replaceAll("\\\\", "");
                    s = s.substring(1, s.length() - 1);
                    Log.e(TAG, json + "  " + s);
                    JSONObject jsonObject = JSONObject.parseObject(s);
                    tv_men_toilet_remain.setText(jsonObject.getString("manAvailableCount"));
                    tv_women_toilet_remain.setText(jsonObject.getString("womanAvailableCount"));
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        if (!toiletPit.isAvailable()) {
            AppConstant.HUMAN_FLOW_NUM++;
            SharedPreferencesUtil.putInt(getAppContext(), AppConstant.HUMAN_FLOW_SHARE, AppConstant.HUMAN_FLOW_NUM);
            tv_headcount.setText(AppConstant.HUMAN_FLOW_NUM + "人");
        }

        Log.i(TAG, "webview内容改变了" + pitJson);


    }

    @Override
    public void updateToiletPitCommonViewForDisabled(ToiletPit toiletPit) {

        //对于通用的页面，更新残卫的数据
        if (toiletPit.isAvailable()) {
            iv_disable_state.setImageResource(R.drawable.disable_state_free);
        } else {
            iv_disable_state.setImageResource(R.drawable.disable_state_using);
        }
    }


    @Override
    public void updateToiletMeterView(ToiletMeter toiletMeter) {
        String deviceGroup = toiletMeter.getDeviceGroup();
        byte deviceGroupByte = ByteUtil.toByteArray(Integer.parseInt(deviceGroup))[1];
        String value = String.format("%.2f", toiletMeter.getValue() * 0.01);
        Log.i(TAG, "组号为:" + deviceGroup + "，原始值为:" + value);
        if (deviceGroupByte == MsgConstant.DEVICE_GROUP_TOILET_METER_ELEC) {
            tv_electric_meter.setText(value + "Kw·h");
        } else if (deviceGroupByte == MsgConstant.DEVICE_GROUP_TOILET_METER_WATER) {
            WaterMeterManager.getInstance().setWaterOriginalValue(toiletMeter.getValue() * 0.01f);
            float newWaterValue = WaterMeterManager.getInstance().getCurrentWaterValue();
            tv_water_meter.setText(String.format("%.2f", newWaterValue) + "L/s");
        }

    }


    @Override
    public void updateToiletOdorDetectorView(OdorDetector3 detector3) {
        String value = String.format("%.2f", detector3.getValue() * 0.01);
        Log.i(TAG, "deviceGroup:" + detector3.getDeviceGroup() + "value：" + value + " state: " + detector3.isState());

        byte deviceGroup = ByteUtil.toByteArray(Integer.parseInt(detector3.getDeviceGroup()))[1];
        byte deviceNum = ByteUtil.toByteArray(Integer.parseInt(detector3.getDeviceNum()))[1];


        if (deviceGroup == MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_MAN) {
            //男厕
            if (deviceNum == MsgConstant.DEVICE_NUM_TOILET_DETECTOR_ODOR_NH3) {
                tv_man_nh3.setText(value + "ppm");
            } else if (deviceNum == MsgConstant.DEVICE_NUM_TOILET_DETECTOR_ODOR_H2S) {
                tv_man_h2s.setText(value + "ppm");
            }
            // 异常状态先不用显示
            //            iv_man_detector_state.setVisibility(detector3.hasNotWorkDevice() ? View.VISIBLE : View.INVISIBLE);
        } else if (String.valueOf(MsgConstant.DEVICE_GROUP_TOILET_DETECTOR_ODOR_WOMAN).equals(detector3.getDeviceGroup())) {
            //女厕
            if (deviceNum == MsgConstant.DEVICE_NUM_TOILET_DETECTOR_ODOR_NH3) {
                tv_woman_nh3.setText(value + "ppm");
            } else if (deviceNum == MsgConstant.DEVICE_NUM_TOILET_DETECTOR_ODOR_H2S) {
                tv_woman_h2s.setText(value + "ppm");
            }
            //异常状态先不用显示
            //            iv_woman_detector_state.setVisibility(detector3.hasNotWorkDevice() ? View.VISIBLE : View.INVISIBLE);
        }

    }

    @Override
    public void updateOdorDetectorView(OdorManager.OdorArg arg) {
        String value = String.format("%.2f", arg.value * 0.01);
        Log.i(TAG, "value：" + value);

        if ((arg).arg0 == OdorManager.ODOR_MAN) {

            if ((arg).arg1 == OdorManager.ODOR_NH3) {
                tv_man_nh3.setText(value + "ppm");

            } else if ((arg).arg1 == OdorManager.ODOR_H2S) {
                tv_man_h2s.setText(value + "ppm");

            }

        } else if ((arg).arg0 == OdorManager.ODOR_WOMAN) {

            if ((arg).arg1 == OdorManager.ODOR_NH3) {
                tv_woman_nh3.setText(value + "ppm");

            } else if ((arg).arg1 == OdorManager.ODOR_H2S) {
                tv_woman_h2s.setText(value + "ppm");

            }

        } else if ((arg).arg0 == OdorManager.ODOR_DISABLED) {

            if ((arg).arg1 == OdorManager.ODOR_NH3) {

                Log.e(TAG, "残卫NH3值为：" + value);
            } else if ((arg).arg1 == OdorManager.ODOR_H2S) {
                Log.e(TAG, "残卫H2S值为：" + value);

            }
        }
    }


    @Override
    public void updateToieltTempHumiDetectorView(TempHumiDetector detector) {

        String temperature = String.format("%.1f", detector.getTemperature() * 0.1);
        String humidity = String.format("%.1f", detector.getHumidity() * 0.1);

        Log.i(TAG, "deviceGroup:" + detector.getDeviceGroup() + "温度：" + temperature + ",湿度：" + humidity);

        tv_temperature.setText(temperature + "°C");
        tv_humidity.setText(humidity + "%");

    }

    @Override
    public void updateToiletTimeView(ToiletTime toiletTime) {

        long time = toiletTime.toTimestamp();
        Log.i(TAG, toiletTime.toTimeString() + "   timestamp=" + time);

        textClockTime.setCurrentMillTime(time);
        textClockDate.setCurrentMillTime(time);
    }

    @Override
    public void updateToiletTimeView() {
//        ToastManager.newInstance("更新系统时间:" + System.currentTimeMillis()).show();
        textClockTime.setCurrentMillTime(System.currentTimeMillis());
    }

    @Override
    public void updateWaterValueWhenAdjusted(float value) {
        tv_water_meter.setText(String.format("%.2f", value) + "L/s");

    }

    @Override
    public void updatPitNum(String json) {
        tv_men_toilet_count.setText(AppConstant.pitNumData.getNumManSit() + AppConstant.pitNumData.getNumManStand() + "");
        tv_women_toilet_count.setText(AppConstant.pitNumData.getNumWomanSit() + "");
        webviewManager.onNewMsg("freshPitNum", json, new WebViewManager.WebViewCallback() {
            @Override
            public void onValue(String json) {
                Log.i(TAG, json);
                ToastManager.newInstance("更改成功").show();
            }
        });
    }

    @Override
    public void upadateToiletName(String json) {

        ToiletNameData toiletNameData = new Gson().fromJson(json, ToiletNameData.class);
        String toiletName = toiletNameData.getToiletName();
        String toiletAddress = toiletNameData.getToiletAddress();
        tv_toilet_name.setText(toiletName);
        tv_toilet_address.setText(toiletAddress);

    }

    /**
     * 设置viewpager的图片并开启轮播
     * 相关参数在AppConstant中配置
     */
    private void turnOnViewPager() {
        if (!AppConstant.TURN_ON_VIEW_PAGER) {
            //不开启轮播
            ll_bottom_img.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);
            return;
        }

        ll_bottom_img.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);

        ArrayList<ImageView> imageViews = new ArrayList<>();
        for (int i = 0; i < AppConstant.images.length; i++) {
            ImageView imageView = new ImageView(App.getAppContext());
            imageView.setBackgroundResource(AppConstant.images[i]);
            imageViews.add(imageView);
        }
        viewPager.setAdapter(new DisplayImgAdapter(imageViews));
        viewPager.setCurrentItem(0);
        if (mHander == null) {
            mHander = new DisplayImgViewPagerHandler(viewPager, imageViews.size(), AppConstant.VIEW_PAGER_DELAY);
        }
        mHander.start();

    }

    @Override
    public void update(Observable o, Object arg) {

        if (o instanceof OdorManager && arg instanceof OdorManager.OdorArg) {
            updateOdorDetectorView((OdorManager.OdorArg) arg);
        } else if (o instanceof WaterMeterManager) {
            updateWaterValueWhenAdjusted(WaterMeterManager.getInstance().getCurrentWaterValue());
        }
    }
}
