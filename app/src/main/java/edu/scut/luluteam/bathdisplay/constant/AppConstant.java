package edu.scut.luluteam.bathdisplay.constant;

import edu.scut.luluteam.bathdisplay.R;
import edu.scut.luluteam.bathdisplay.model.setting.PitNumData;
import edu.scut.luluteam.bathdisplay.model.setting.ToiletNameData;

public class AppConstant {
    public static final String HUMAN_FLOW_SHARE = "human_flow";
    public static int HUMAN_FLOW_NUM = 0;

    public static String CURRENT_DATE = "190327";
    public static final String CURRENT_DATE_SHARE = "current_date_share";

    /**
     * 界面的坑位数量默认值
     * 定制页面，需要输入指定的值；通用页面，输入0即可
     */
    public static PitNumData pitNumData = new PitNumData(3, 3, 5, 0, 0);
//    public static PitNumData pitNumData = new PitNumData(0, 0, 0, 0, 0);

    /**
     * 通用界面的厕所名和地址
     */
    public static ToiletNameData toiletNameData = new ToiletNameData("", "");
    /**
     * 保存到本地的key值
     */
    public static final String KEY_SAVE_WATER_ADJUSTED_VALUE = "key_save_water_adjusted_value";
    public static final String KEY_SAVE_PIT_NUM_DATA = "key_save_pit_num_value";
    public static final String KEY_SAVE_TOILET_NAME_DATA = "key_save_toilet_name_data";

    /**
     * 是否启用通用界面
     */
    public static final boolean enableCommonWebView = false;
    //        public static final String WebViewUrl="file:///android_asset/androidWebsite/html/WuHan.html";
    public static final String WebViewUrl = "file:///android_asset/androidWebsite/html/Claudy.html";
//    public static final String WebViewUrl = "file:///android_asset/androidWebsite/html/statePitPreview.html";

    /**
     * 图片轮播的相关配置
     */
    public static final boolean TURN_ON_VIEW_PAGER = false;
    public static final int[] images = {R.drawable.bottom_image, R.drawable.bottom_image2};
    public static final long VIEW_PAGER_DELAY = 10000;
}
