package edu.scut.luluteam.bathdisplay.mqtt;


import edu.scut.luluteam.bathdisplay.utils.ByteUtil;

/**
 * @author Guan
 * @date Created on 2017/11/30
 */
public class MqttHandler {
    private static final String TAG = "MqttHandler";


    private MqttHandler() {

    }

    /**
     * @param connected
     */
    public static void onNetwork(boolean connected) {
//        EventBus eventBus = EventBusManager.getInstance(EventBusManager.INSTANCE_NAME.Broadcast);
//        EventBusManager.EventBusMsg msg = new EventBusManager.EventBusMsg(EventBusManager.MsgType.fromMqtt);
//        msg.setAction(connected);
//        eventBus.post(msg);
    }


    /**
     * 处理mqtt消息
     *
     * @param bytes
     */
    public static void onMessage(byte[] bytes) {
//        if (StringUtils.isEmpty(json)) {
//            Log.i(TAG, "mqtt on Message:  空数据");
//            return;
//        }
        edu.scut.luluteam.bathdisplay.manager.frame.guan.FrameManager.put(bytes);
        System.out.println("收到mqtt:" + ByteUtil.byte2hex(bytes));


    }


}
