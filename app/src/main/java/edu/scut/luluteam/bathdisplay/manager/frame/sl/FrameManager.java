package edu.scut.luluteam.bathdisplay.manager.frame.sl;

import edu.scut.luluteam.bathdisplay.manager.business.FrameProcessor;
import edu.scut.luluteam.bathdisplay.model.CustomFrame;

public class FrameManager {
    private static MyArrayBlockingQueue<Byte> queue = new MyArrayBlockingQueue<Byte>(2048);


    static {
        work();
    }

//    public FrameManager() {
//        mDataProcessor=new DataProcessor();
//    }

    public static void put(byte[] bytes) throws InterruptedException {
        Byte[] arr = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            arr[i] = bytes[i];
        }
        queue.putAll(arr);
    }

    public static void take() throws InterruptedException {
        CustomFrame customFrame = queue.takeWholeFrame();
        if (customFrame == null) return;
        FrameProcessor.process(customFrame);
    }

    public static void work() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
