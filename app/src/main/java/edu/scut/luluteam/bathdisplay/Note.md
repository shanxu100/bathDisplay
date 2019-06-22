MVP架构：
https://www.jianshu.com/p/389c9ae1a82c
https://github.com/googlesamples/android-architecture/tree/todo-mvp/

Android模拟器与PC通讯
1、环境配置
- 安装VSPD，在PC机上模拟若干串口
- 安装Genymotion模拟器，配置设备串口相关参数，并启动
- 安装“友善串口调试助手”软件，模拟PC端发送和接收串口的通信

2、Android端发开串口通信功能
参考Google开源项目：serialPort
https://blog.csdn.net/u010312949/article/details/80199018

3、adb相关命令
-将android-sdk/platform-tools路径添加到环境变量，这样方便在AS中直接使用 adb命令

#查看已连接设备列表
adb devices

#打开adb
adb shell
或者在连接多个设备的时候，需要指定某个具体的设备
adb -s 设备名 shell

4、问题：如何判断外接的设备连接到了Android板的哪个串口？

5、插入拔出设备（如键盘）后导致App重启？（重要）
在清单文件中进行设置android:configChanges,除了 keyboard,还要加上 navigation(不起作用)


6、adb远程调试与adb相关命令
https://blog.csdn.net/xusiwei1236/article/details/41480319
adb root
adb shell
adb connect xxxxx:xx
adb disconnect
adb devices
adb remount
adb start-service
adb kill-service

7、android TV开发相关
https://www.jianshu.com/p/d393866e4fa5

8、导航栏、状态栏、ActionBar相关
https://blog.csdn.net/chen_xiaobao/article/details/80985923

9、MQTT服务器地址是否不变，是否设置了密码？
192.168.3.5， 端口是：1883，mqtt的用户名为： admin, 密码为 password.


11、统计人流量：统计一天中“有人”的数据帧的个数


13、建议设计更加灵活的定制化页面和通用页面的切换方式，这样可以实现只build一次就能在多个场景下应用

14、总结WebView中WebSetting的相关设置和参数，避免因WebView设置的错误导致debug半天的问题