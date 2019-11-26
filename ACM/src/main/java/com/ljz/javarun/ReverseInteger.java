package com.ljz.javarun;

/**
 * 整数反转
 * <p>
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。

 * 举例：
 * 输入: 123
 * 输出: 321
 *
 * 输入: -123
 * 输出: -321
 *
 * 注意:
 *
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−231,  231 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
 */
public class ReverseInteger {

    public static void run(){
        System.out.println(123+" "+reverse(123));
        System.out.println(-123+" "+reverse(-123));
    }


    public static int reverse(int x) {

        int r = 0;
        while (x != 0) {
            int s = x % 10;
            x /= 10;

            if(r >Integer.MAX_VALUE /10 || r<Integer.MIN_VALUE/10){
                return 0;
            }


            if ((r*10 + s) > Integer.MAX_VALUE||(r*10 + s) < Integer.MIN_VALUE) {
                return 0;
            }
            r *= 10;
            r += s;

        }
        return (int) r;

    }

}
