package com.qcmian.happycolor;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Utils {
    private static int[] mScreenSize;

    public static int[] getScreenWidth(Activity context) {
        if (mScreenSize == null) {
            mScreenSize = new int[2];
            WindowManager manager = context.getWindowManager();
            DisplayMetrics outMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(outMetrics);
            mScreenSize[0] = outMetrics.widthPixels;
            mScreenSize[1] = outMetrics.heightPixels;
        }
        return mScreenSize;
    }
}
