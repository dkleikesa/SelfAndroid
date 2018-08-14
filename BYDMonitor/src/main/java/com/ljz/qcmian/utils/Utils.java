package com.ljz.qcmian.utils;

import android.util.Base64;

/**
 * Created by jianzhang.ljz on 2017/12/1.
 */

public class Utils {
    //    public static String getProcessID() {
//        String pid = "";
//        String pName = ManagementFactory.getRuntimeMXBean().getName();
//        if ((pName != null) && (pName.indexOf('@') > 0)) {
//            pid = pName.substring(0, pName.indexOf('@'));
//        }
//        return pid;
//    }
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

}
