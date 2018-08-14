package com.ljz.javarun;

/**
 * 问题：
 * 在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，
 * 输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
 * 解析：
 * 数组从左到右递增，从上到下递增，选取右上角的数字a[row][col]与target进行比较，
 * target小于元素a[row][col]时，那么target必定在元素a所在行的左边, 即col--；
 * target大于元素a[row][col]时，那么target必定在元素a所在列的下边, 即row++；
 */
public class TwoDimArrayFind {
    static int[][] array = {{1, 2, 3}, {4, 5, 6}, {7, 9, 11}, {12, 14, 20}};

    public static void run() {
        int taget = 20;
        boolean b = find(taget, array);
        System.out.println("contains " + taget + " : " + b);
    }

    public static boolean find(int target, int[][] array) {
        int row = 0;
        int col = array[0].length - 1;
        while (row <= array.length - 1 && col >= 0) {
            if (target == array[row][col])
                return true;
            else if (target > array[row][col])
                row++;
            else
                col--;
        }
        return false;
    }
}
