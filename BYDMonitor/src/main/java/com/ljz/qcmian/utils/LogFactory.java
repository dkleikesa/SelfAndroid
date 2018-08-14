package com.ljz.qcmian.utils;

/**
 * Created by jianzhang.ljz on 2017/10/9.
 */

public class LogFactory {

    private static LLog mLog = null;
    private static Object mLock = new Object();

    public static LLog getLLog() {

        if (mLog == null) {
            synchronized (mLock) {
                if (mLog == null) {

                    LLog.Config config = new LLog.Config();
                    config.mSaveFile = true;

                    LLog.DefaultWriter.Config wconfig = new LLog.DefaultWriter.Config();
                    wconfig.mLogPathFolder = "/sdcard/qcmian";

                    try {
                        mLog = new LLog(config, new LLog.DefaultPrinter(), new LLog.DefaultWriter(wconfig));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return mLog;
    }

}
