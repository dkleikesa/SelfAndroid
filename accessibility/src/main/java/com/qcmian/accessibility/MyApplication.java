package com.qcmian.accessibility;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;
import android.widget.Toast;

import static com.qcmian.accessibility.MyAccessibilityService.TAG;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


        if (!isStartAccessibilityService(this, "acb")) {
            Toast.makeText(this, "请打开无障碍开关", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    public static boolean isStartAccessibilityService(Context context, String name) {
        AccessibilityManager am = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        Log.d(TAG, "enable:" + am.isEnabled());
        return am.isEnabled();
    }
}
