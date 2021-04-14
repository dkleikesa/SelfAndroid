package com.ljz.javarun;

import java.util.HashMap;
import java.util.HashSet;

public class LongestTurbulenceSubarray {
/*
当 A 的子数组 A[i], A[i+1], ..., A[j] 满足下列条件时，我们称其为湍流子数组：

若 i <= k < j，当 k 为奇数时， A[k] > A[k+1]，且当 k 为偶数时，A[k] < A[k+1]；
或 若 i <= k < j，当 k 为偶数时，A[k] > A[k+1] ，且当 k 为奇数时， A[k] < A[k+1]。
也就是说，如果比较符号在子数组中的每个相邻元素对之间翻转，则该子数组是湍流子数组。

返回 A 的最大湍流子数组的长度。

示例 1：
输入：[9,4,2,10,7,8,8,1,9]
输出：5
解释：(A[1] > A[2] < A[3] > A[4] < A[5])

示例 2：
输入：[4,8,12,16]
输出：2

示例 3：
输入：[100]
输出：1

来源：力扣（LeetCode）
链接：https://leetcode-cn.com/problems/longest-turbulent-subarray
著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。

 */

    public static void run() {
        int[] arr = {0, 8, 45, 88, 48, 68, 28, 55, 17, 24};
        System.out.println(maxTurbulenceSize(arr));
    }


    public static int maxTurbulenceSize(int[] arr) {
        int right = 1;
        int num = 0;
        int max = 0;
        if (arr.length <= 1) {
            return arr.length;
        } else if (arr.length < 3) {
            if (arr[0] == arr[1]) {
                return 1;
            } else {
                return 2;
            }
        }
        int less = -2;

        for (int i = 1; i < arr.length; i++) {
            int temp;
            if (arr[right - 1] > arr[right]) {
                temp = -1;
            } else if (arr[right - 1] < arr[right]) {
                temp = 1;
            } else {
                temp = 0;
            }

            if (less == -2) {
                if (temp == 0) {
                    num = 1;
                } else {
                    num = 2;
                }
            } else {
                if (temp == 0) {
                    num = 1;
                } else if (temp != less) {
                    num++;
                } else {
                    num = 2;
                }
            }
            if (num > max) {
                max = num;
            }
            less = temp;
            right++;

        }
        return max;
    }
}
