package edu.scut.luluteam.bathdisplay.manager;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import edu.scut.luluteam.bathdisplay.display.DisplayActivity;

/**
 * 开机自启
 * 监听开机广播，实现开机自启动
 * 有时会监听不到，那么可以选择多监听几个广播
 *
 * @author Guan
 */
public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    private static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    private static final String TAG = "AutoStartBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //开机后一般会停留在锁屏页面且短时间内没有进行解锁操作屏幕会进入休眠状态，
        // 此时就需要先唤醒屏幕和解锁屏幕

        //屏幕唤醒
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //最后的参数是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.SCREEN_DIM_WAKE_LOCK, "bathDisplay:" + TAG);
        //设置释放时间。不设置就一直亮着
        wl.acquire();

        //屏幕解锁
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("bathDisplay:" + TAG);
        kl.disableKeyguard();


        if (intent.getAction().equals(action_boot)) {
            Intent intent1 = new Intent(context, DisplayActivity.class);
//            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }

    /*

     <!-- 设置权限 -->
    <!--允许程序禁用键盘锁-->
    <uses-permission android:name="android.permission.DISABLE_KEYGUARD" />
    <!--允许一个程序接收到 ACTION_BOOT_COMPLETED广播在系统完成启动-->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <!--允许使用PowerManager的 WakeLocks保持进程在休眠时从屏幕消失-->
    <uses-permission android:name="android.permission.WAKE_LOCK" />

     //监听
        <receiver
            android:name=".manager.AutoStartBroadcastReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <category android:name="android.intent.category.HOME"/>
            </intent-filter>
        </receiver>

        4、重启手机，测试app有没有自动启动。如果有，那么恭喜你。如果没有，请往下看。

如果按照上面的全部步骤后操作后，重启没有自动启动程序，怎么办呢？是怎么回事呢？
那么首先请检查一下你的手机是不是安装了360之类的安全软件，
如果有，请在软件的自启动软件管理中将app设置为【允许】
（我的手机没有安装这些软件，但是系统设置里面自带了自启动软件管理的功能 ，
所以我在这里将我的app设置为【允许开机启动】），重启手机，测试是否成功。
如果还是失败，那么请检查你的手机是不是设置了app安装首选位置是sd卡，据说安装到sd卡的话，
因为手机启动成功后（发送了启动完成的广播后）才加载sd卡，所以app接收不到广播。
如果是的话，把app安装到内部存储试试。
如果不懂得设置的话，那么直接在AndroidManifest.xml文件中设置安装路径，
android:installLocation="internalOnly"。比如：

    <manifest
    package="cn.weixq.autorun"
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:installLocation="internalOnly">

     */
}
