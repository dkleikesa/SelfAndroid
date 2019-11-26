package com.ljz.javarun;

import java.util.HashMap;

/**
 * 整数转罗马数字
 * 罗马数字包含以下七种字符： I， V， X， L，C，D 和 M。
 * <p>
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * <p>
 * 例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
 * <p>
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
 * <p>
 * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
 * 给定一个整数，将其转为罗马数字。输入确保在 1 到 3999 的范围内。
 */
public class IntToRoman {

    public static void run() {
        System.out.println(299 + " " + intToRoman(299));
        System.out.println(4 + " " + intToRoman(4));

        System.out.println(299 + " " + romanToInt(intToRoman(299)));
        System.out.println(4 + " " + romanToInt(intToRoman(4)));
    }

    public static String intToRoman(int num) {
        if (num > 3999 || num < 1) {
            return "";
        }
        int values[] = new int[]{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String strs[] = new String[]{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};

        StringBuilder str = new StringBuilder();
        //从最大的开始算。
        for (int i = 0; i < 13; i++) {
            while (num >= values[i]) {
                num -= values[i];
                str.append(strs[i]);
            }
        }
        return str.toString();
    }


    public static int romanToInt(String s) {
        HashMap<Character, Integer> romanMap = new HashMap<Character, Integer>();

        romanMap.put('I', 1);
        romanMap.put('V', 5);
        romanMap.put('X', 10);
        romanMap.put('L', 50);
        romanMap.put('C', 100);
        romanMap.put('D', 500);
        romanMap.put('M', 1000);
        char[] arr = s.toCharArray();
        int ret = 0;
        int last = 0;
        for (int i = arr.length-1; i >= 0; i--) {

            int current = romanMap.get(arr[i]);
            if (current < last) {
                ret -= current;
            } else {
                ret += current;
            }
            last = current;
        }

        return ret;
    }
}
