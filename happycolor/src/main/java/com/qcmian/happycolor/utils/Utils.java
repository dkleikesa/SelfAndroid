package com.qcmian.happycolor.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class Utils {
    private static int[] screenSize;
    private static DisplayMetrics displayMetrics;

    public static int[] getScreenSize() {
        if (screenSize == null) {
            screenSize = new int[2];
            DisplayMetrics outMetrics = Resources.getSystem().getDisplayMetrics();
            screenSize[0] = outMetrics.widthPixels;
            screenSize[1] = outMetrics.heightPixels;
        }
        return screenSize;
    }

    public static int dp2px(float dp) {
        if (displayMetrics == null) {
            displayMetrics = Resources.getSystem().getDisplayMetrics();
        }
        return (int) (dp * displayMetrics.density + 0.5);
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());

    }
}
