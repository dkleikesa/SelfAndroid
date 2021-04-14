package com.qcmian.happycolor.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadExecutor {
    public static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);


}
