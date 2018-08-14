package com.ljz.qcmian;

/**
 * Created by jianzhang.ljz on 2017/11/30.
 */

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;


public class Md5Utility {
    public Md5Utility() {
    }

    public static String hexdigest(String string) {
        String s = null;
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(string.getBytes());
            byte[] tmp = md.digest();
            char[] str = new char[32];
            int k = 0;

            for (int i = 0; i < 16; ++i) {
                byte byte0 = tmp[i];
                str[k++] = hexDigits[byte0 >>> 4 & 15];
                str[k++] = hexDigits[byte0 & 15];
            }

            s = new String(str);
        } catch (Exception var9) {
        }

        return s;
    }

    public static String getFileMD5(String fileName) {
        FileInputStream fileStream = null;

        Object var3;
        try {
            fileStream = new FileInputStream(fileName);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];

            int length;
            while ((length = fileStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }

            byte[] md5Bytes = md5.digest();
            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            String var15 = hexValue.toString();
            return var15;
        } catch (Exception var12) {
            var3 = null;
        } finally {
        }

        return (String) var3;
    }

    public static String getFileMD5(File file) {
        FileInputStream fileStream = null;

        Object var3;
        try {
            fileStream = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];

            int length;
            while ((length = fileStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }

            fileStream.close();
            byte[] md5Bytes = md5.digest();
            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            String var15 = hexValue.toString();
            return var15;
        } catch (Exception var12) {
            var3 = null;
        } finally {
        }

        return (String) var3;
    }

    public static String getStringMD5(String str) {
        char[] charArray = str.toCharArray();
        return getCharArrayMD5(charArray);
    }

    public static String getCharArrayMD5(char[] charArray) {
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; ++i) {
            byteArray[i] = (byte) charArray[i];
        }

        return getByteArrayMD5(byteArray);
    }

    public static String getByteArrayMD5(byte[] byteArray) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteArray);
            byte[] md5Bytes = md5.digest();
            StringBuffer hexValue = new StringBuffer();

            for (int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            return hexValue.toString();
        } catch (Exception var6) {
            return null;
        }
    }

    public static String create32BitMD5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception var5) {
            ;
        }

        if (messageDigest == null) {
            return "";
        } else {
            byte[] byteArray = messageDigest.digest();
            StringBuffer md5StrBuff = new StringBuffer();

            for (int i = 0; i < byteArray.length; ++i) {
                if (Integer.toHexString(255 & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(255 & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(255 & byteArray[i]));
                }
            }

            return md5StrBuff.toString();
        }
    }
}