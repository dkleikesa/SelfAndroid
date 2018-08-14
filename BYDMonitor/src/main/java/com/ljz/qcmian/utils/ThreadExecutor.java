package com.ljz.qcmian.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.RejectedExecutionException;

public class ThreadExecutor {
    private static final Handler mHandler;

    static {
        HandlerThread handlerThread = new HandlerThread("net");
        handlerThread.start();
        mHandler = new Handler(handlerThread.getLooper());
    }

    /**
     * post到子线程
     * <p>
     * 解耦<code>TaskManager#run(Runnable)</code>
     *
     * @param action 回调
     */
    public static void post(Runnable action) {
        mHandler.post(action);
    }

    /**
     * post到子线程延时处理
     *
     * @param action     回调
     * @param delayMills 延时
     */
    public static void postDelayed(Runnable action, long delayMills) {
        mHandler.postDelayed(action, delayMills);
    }

    /**
     * 移除回调
     *
     * @param action 回调
     */
    public static void removeCallbacks(Runnable action) {
        if (action == null) {
            return;
        }
        mHandler.removeCallbacks(action);
    }


}
