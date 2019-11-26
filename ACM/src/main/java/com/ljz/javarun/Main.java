package com.ljz.javarun;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {


    public static void main(String[] args) {
//        TwoSum.run();
//        System.out.println(uncommonFromSentences("apple apple", "sour"));
//        System.out.println(299 + " " + intToRoman(299));
//        ReverseInteger.run();
//        IntToRoman.run();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("2222");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("3333");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        while (true) {
            System.out.println("1111");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

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

}

