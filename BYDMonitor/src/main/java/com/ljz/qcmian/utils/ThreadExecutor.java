package com.ljz.qcmian.utils;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {
    private static final ScheduledExecutorService mExecutor;

    static {
        mExecutor = Executors.newScheduledThreadPool(3);
    }

    /**
     * post到子线程
     * <p>
     * 解耦<code>TaskManager#run(Runnable)</code>
     *
     * @param action 回调
     */
    public static void post(Runnable action) {
        mExecutor.execute(action);
        mExecutor.schedule(action, 0L, TimeUnit.MILLISECONDS);
    }

    /**
     * post到子线程延时处理
     *
     * @param action     回调
     * @param delayMills 延时
     */
    public static void postDelayed(Runnable action, long delayMills) {
        mExecutor.schedule(action, delayMills, TimeUnit.MILLISECONDS);
    }

}
