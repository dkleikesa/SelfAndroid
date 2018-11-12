package com.ljz.qcmian.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;

/**
 * Created by jianzhang.ljz on 2017/12/1.
 */

public class Utils {
    public static String getProcessID() {
        return String.valueOf(android.os.Process.myPid());
    }

    public static String base64EncodeToString(byte[] buff) {
        return Base64.encodeToString(buff, Base64.DEFAULT);
    }

    public static byte[] base64Encode(byte[] buff) {
        return Base64.encode(buff, Base64.DEFAULT);
    }

    public static byte[] base64Decode(byte[] buff) {
        return Base64.decode(buff, Base64.DEFAULT);
    }

    public static String getDeviceID(Context context) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                String id = telephonyManager.getDeviceId();
                if (TextUtils.isEmpty(id)) {
                    id = telephonyManager.getSimSerialNumber();
                }
                return id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
