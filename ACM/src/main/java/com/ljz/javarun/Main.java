package com.ljz.javarun;


import java.util.ArrayList;
import java.util.HashSet;

public class Main {


    public static void main(String[] args) {
//   LongestTurbulenceSubarray.run();;

        B<? super C> b = new B<C>();
//        b.set(new A());
        System.out.println(b.getClass().getDeclaredFields()[0].getType().getName());
    }

    public static class A {

    }

    public static class C extends A {

    }

    public static class B<T extends C> {
//        public T a;
//
//        public void set(T t) {
//            a = t;
//        }
    }

    public String[] permutation(String s) {
        char[] buf = s.toCharArray();
        // 转成List 方便增删
        ArrayList<Character> list = new ArrayList<>();
        for (char c : buf) {
            list.add(c);
        }

        ArrayList<ArrayList<Character>> temp = dfs(list);
        HashSet<String> set = new HashSet();
        for (ArrayList<Character> l : temp) {
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : l) {
                stringBuilder.append(c);
            }
            set.add(stringBuilder.toString());
        }
        return (String[]) set.toArray(new String[set.size()]);
    }

    ArrayList<ArrayList<Character>> dfs(ArrayList<Character> list) {
        ArrayList<ArrayList<Character>> ret = new ArrayList();
        // 如果list只有最后一个字符了，无法分裂。
        if (list.size() == 1) {
            ret.add(list);
            return ret;
        }
        // 分裂：每次去除list 一个字符，将剩余的字符分裂成多个list
        // 分裂后的list递归调用此方法，直到list大小为1，无法再分裂
        for (int i = 0; i < list.size(); i++) {
            ArrayList<Character> temp = (ArrayList<Character>) list.clone();
            Character c = list.get(i);
            temp.remove(c);
            ArrayList<ArrayList<Character>> split = dfs(temp);
            for (ArrayList l : split) {
                //分裂后的子串需要增加当前去除的字符
                l.add(0, c);
                ret.add(l);
            }
        }
        return ret;
    }

    public static int subarraysWithKDistinct(int[] A, int k) {
        int len = A.length;
        if (k > len) {
            return 0;
        }
        int left = 0;
        int right = 0;
        int ret = 0;
        HashSet<Integer> set = new HashSet();
        while (left <= len - k) {
            set.add(A[right]);
            if (set.size() < k) {
                right++;
                if (right == len) {
                    set.clear();
                    left++;
                    right = left;
                }
            } else if (set.size() == k) {
                ret++;
                right++;
                if (right == len) {
                    set.clear();
                    left++;
                    right = left;
                }
            } else {
                set.clear();
                left++;
                right = left;
            }
        }

        return ret;

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


}

