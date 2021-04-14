package com.qcmian.happycolor;

import android.app.Application;
import android.content.Context;

import com.qcmian.happycolor.utils.TTSHelper;

public class MyApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        TTSHelper.init(this);
    }
}
