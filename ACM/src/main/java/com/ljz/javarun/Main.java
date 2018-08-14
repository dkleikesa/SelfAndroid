package com.ljz.javarun;


import java.util.ArrayList;
import java.util.HashMap;

public class Main {


    public static void main(String[] args) {
        HashMap map = new HashMap();
        A a = new A();
        String str1 = "dfee";
        String str2 = new String(str1);
        map.put(str1, "start");

//        System.out.println(str2);
//        System.out.println(map.get(str2));
//        a.aa = "ee";
//        System.out.println(map.get(a));
//        a.b();
        int[] arr = {1, 2, 3, 4, 5, 6};
        System.out.println("index:" + get(arr, 0, arr.length-1, 7));
    }


    public static int get(int[] arr, int start, int end, int target) {
        if ((end - start) <= 1) {
            if (arr[start] == target) {
                return start;
            }
            if (arr[end] == target) {
                return end;
            }
            return -1;
        }
        int mid = (start + end) / 2;
        if (arr[mid] > target) {
            mid = get(arr, start, mid, target);
        } else if (arr[mid] < target) {
            mid = get(arr, mid, end, target);
        }
        return mid;
    }


    static class A {
        String aa = "dd";

        void b() {
            System.out.print(aa);
        }
    }

    static class B extends A {
        void b() {
            System.out.print("ee");
        }
    }


/**
 *  问题：
 *  顺时针打印矩阵
 *  解析：
 *  按圈，每一圈分为上下左右4部分，分别打印，每打印一圈向内缩小，循环一直到最后一行。
 */


}

