package com.ljz.qcmian;

import android.app.Application;

import com.ljz.qcmian.db.DBManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBManager.getInstance().init(getApplicationContext());
    }
}
