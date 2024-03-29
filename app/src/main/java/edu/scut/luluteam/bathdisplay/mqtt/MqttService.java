package edu.scut.luluteam.bathdisplay.mqtt;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttService extends Service {
    private ExecutorService mqttExector = Executors.newSingleThreadExecutor();
    private static final String TAG = "MqttService";

    public MqttService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "start mqtt Service");
        mqttExector.execute(new Runnable() {
            @Override
            public void run() {
                MqttClientManager.getInstance().start();
            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "stop mqtt Service");
        MqttClientManager.getInstance().stop();
        super.onDestroy();
    }
}
