package com.ljz.javarun;

/**
 * 二进制统计问题：
 * 输入一个整数，输出该数二进制表示中1的个数。其中负数用补码表示。
 * 解析：
 * 循环判断整数每一位是否为1即可
 */
public class NumberOf1 {

    public static int numberOf1(int n) {
        int num = 0;
        for (int i = 0; i < 32; i++) {
            if ((n & 0x01) == 1) {
                num++;
            }
            n = n >> 1;
        }
        return num;
    }
}
