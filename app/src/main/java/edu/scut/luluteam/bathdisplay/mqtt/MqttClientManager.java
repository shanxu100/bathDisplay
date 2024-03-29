package edu.scut.luluteam.bathdisplay.mqtt;


import android.util.Log;

import org.apache.commons.lang.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.scut.luluteam.bathdisplay.manager.App;
import edu.scut.luluteam.bathdisplay.utils.SharedPreferencesUtil;


/**
 * @author Guan
 * @date Created on 2018/1/6
 */
public class MqttClientManager {

    private final static int qos = 2;

    private final static String clientId = "bathDisplay" + System.currentTimeMillis();
    private final static String TAG = "MqttClientManager";

    private static final String DEAFULT_MQTT_BROKER = "tcp://192.168.3.5:1883";
    //    private static final String DEAFULT_MQTT_BROKER = "tcp://47.97.211.221:61613";
    private static final String KEY_SAVE_MQTT_BROKER = "key_save_mqtt_broker";


    /**
     * 订阅的 topic
     */
    private final static String[] topics = new String[]{
//            "MAN_DOWN/+","WOMAN_DOWN/+","WATER_DOWN/+","WATT_DOWN/+","MAN_SESNOR_DOWN/+",
//            "WOMAN_SESNORDOWN/+","CLOCK_DOWN/+"
            "DISP_DOWN/+", "SYS_CLOCK/+"
//            "test_down/+"

    };

    private MqttClient client;
    private String broker;
    private ScheduledExecutorService reconnectExecutor = null;
    private static volatile MqttClientManager manager;
    private static boolean stopped = false;

    public String getBroker() {
        return broker;
    }

    public void setBroker(String broker) {
        this.broker = broker;
        saveMqttBroker(broker);
    }

    private MqttClientManager() {
        try {
            broker = getSavedMqttBroker();
            client = new MqttClient(broker, clientId, new MemoryPersistence());
            client.setCallback(new MyMqttCallback());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static MqttClientManager getInstance() {
        if (manager == null) {
            synchronized (MqttClientManager.class) {
                if (manager == null) {
                    manager = new MqttClientManager();
                }
            }
        }
        return manager;
    }

    //=========================================================

    private void saveMqttBroker(String broker) {
        //保存Mqtt服务器的数据
        SharedPreferencesUtil.putString(App.getAppContext(), KEY_SAVE_MQTT_BROKER, broker);

    }

    private String getSavedMqttBroker() {
        String broker = SharedPreferencesUtil.getString(App.getAppContext(), KEY_SAVE_MQTT_BROKER);
        if (StringUtils.isEmpty(broker)) {
            return DEAFULT_MQTT_BROKER;
        }
        return broker;
    }
    //=====================================================================================

    private MqttConnectOptions getOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        //需要根据实际情况配置
        options.setUserName("admin");
        options.setPassword("password".toCharArray());

        options.setCleanSession(true);
        // 设置超时时间 单位为秒
        options.setConnectionTimeout(2);
        // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        options.setKeepAliveInterval(20);
        //自动重连：1-2-4-8-16……最多两分钟-----MqttAndroidClient有用
        //自己实现重连
        options.setAutomaticReconnect(false);
        return options;
    }


    /**
     * 主要：发送消息
     *
     * @param topic
     * @param message msg
     * @return
     */
    public boolean sendMessage(String topic, MqttMessage message) {
        try {
            if (client.isConnected()) {
                client.publish(topic, message);
                Log.i(TAG, "mqtt publish: msg=" + message.toString());
                return true;
            } else {
                Log.e(TAG, "未连接mqtt服务器，发送失败...msg=" + message.toString());
                return false;
            }
        } catch (MqttException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 重连:
     */
    public void reconnect() {
        reconnectExecutor = new ScheduledThreadPoolExecutor(1);
        reconnectExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (stopped) {
                    Log.e(TAG, "mqtt stopped...shutdown reconnectExecutor");
                    reconnectExecutor.shutdownNow();
                    return;
                }
                Log.e(TAG, "mqtt正在重连...");
                if (!client.isConnected()) {
                    try {
                        client.connect(getOptions());
                    } catch (MqttException e) {
                        Log.e(TAG, "matt连接失败……");
                    }
                } else {
                    reconnectExecutor.shutdown();
                    Log.d(TAG, "mqtt关闭重连。。。");
                }
            }
        }, 2, 3, TimeUnit.SECONDS);

    }

    /**
     * 开启
     */
    public void start() {
        stopped = false;
        if (!client.isConnected()) {
            try {
                client.connect(getOptions());
                Log.i(TAG, "connect to MQTT service");
            } catch (MqttException e) {
                e.printStackTrace();
                MqttHandler.onNetwork(false);
                reconnect();
            }
        }

    }

    /**
     * 结束
     */
    public void stop() {
        stopped = true;
        try {
            if (reconnectExecutor != null && !reconnectExecutor.isShutdown()) {
                Log.e(TAG, "reconnectExecutor: Shutdown");
                reconnectExecutor.shutdownNow();
            }
            if (client != null) {
                Log.e(TAG, "client :disconnectForcibly");
                client.disconnectForcibly();
            }
            Log.e(TAG, "stop MQTT service===");
            manager = null;
        } catch (MqttException e) {
            e.printStackTrace();

        }
    }

    private class MyMqttCallback implements MqttCallbackExtended {

        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "mqtt失去连接------");
            MqttHandler.onNetwork(false);
            reconnect();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.i(TAG, "messageArrived--- topic:" + topic);
            message.getId();
            message.getQos();
            MqttHandler.onMessage(message.getPayload());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
//            logger.info("deliveryComplete---------" + token.isComplete());
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            Log.e(TAG, "Mqtt连接成功==" + serverURI);
            try {
                //因为是  options.setCleanSession(true); 所以每次断开重连后得重新订阅消息
                client.subscribe(topics);
                MqttHandler.onNetwork(true);
            } catch (MqttException e) {
                e.printStackTrace();
            }

        }
    }


}
