package com.ljz.javarun;

/**
 * 问题：
 * 请实现一个函数，将一个字符串中的空格替换成“%20”。例如，当字符串为We Are Happy.则经过替换之后的字符串为We%20Are%20Happy。
 * 分析：
 * 此问题有多种解法。
 * 1. 使用java String API来做，不太符合出题意图，但是很简单（replaceSpace）
 * 2. 新建数组，通过copy的形式来做，遇到空格就替换成%20
 * 3. 同一个数组扩展长度，通过移动的方式来做，此时需要从后往前移动，移动次数比较少。
 */


public class ReplaceSpace {

    public static void run() {
        StringBuffer str = new StringBuffer(" we are help ");
        char[] chr = new char[str.length()];
        str.getChars(0, str.length(), chr, 0);
        System.out.println(replaceSpace(str));
        System.out.println(replaceSpaceByCopy(chr));
        System.out.println(replaceSpaceByMove(chr));
    }

    public static String replaceSpace(StringBuffer str) {
        StringBuilder newStr = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == ' ') {
                newStr.append('%');
                newStr.append('2');
                newStr.append('0');
            } else {
                newStr.append(str.charAt(i));
            }
        }
        return newStr.toString();
    }

    public static String replaceSpaceByCopy(char[] str) {
        int num = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                num++;
            }
        }
        int newLen = str.length + num * 2;
        char[] buf = new char[newLen];

        int skipCount = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                buf[i + skipCount] = '%';
                skipCount++;
                buf[i + skipCount] = '2';
                skipCount++;
                buf[i + skipCount] = '0';
            } else {
                buf[i + skipCount] = str[i];
            }
        }
        return new String(buf);
    }

    //C或者C++ 可能会用到此实现，java没意义
    public static String replaceSpaceByMove(char[] str) {
        int num = 0;
        for (int i = 0; i < str.length; i++) {
            if (str[i] == ' ') {
                num++;
            }
        }
        int newLen = str.length + num * 2;
        char[] buf = new char[newLen];
        for (int i = 0; i < str.length; i++) {
            buf[i] = str[i];
        }
        num *= 2;

        for (int i = str.length - 1; i >= 0; i--) {
            if (buf[i] == ' ') {
                buf[i + num] = '0';
                num--;
                buf[i + num] = '2';
                num--;
                buf[i + num] = '%';

            } else {
                buf[i + num] = buf[i];
            }
        }
        return new String(buf);
    }

}
