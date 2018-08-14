package com.ljz.javarun;

/**
 * 扑克牌问题：
 * 手里有一堆扑克牌（编号连续1-n），取一张放到桌子上，然后取一张放到牌堆底，再取一张放到桌子上，以此循环一直到手里没有牌，桌子上的牌是1 - n按顺序排列的。 求原来手里牌堆的顺序。
 * 解析：
 * 1. 按照操作顺序，
 * 第一轮操作后，牌堆里面一半牌（奇数位置），会放到桌子上，顺序就是 1 ~ n/2
 * 第二轮操作后，牌堆（原牌堆里偶数位置的牌）里面的一半又会放到桌子上，顺序是(n/2+1) ~ (n/2+n/4)
 * 依此类推直到手里没有牌
 * 2. 解决思路
 * 新建一个链表模拟原始牌堆，不过里面牌序号都是0
 * 对链表模拟第一轮操作，得到奇数位置的牌序号是 1 ~ n/2，同时从链表中移除他们。
 * 对链表剩余的进行第二轮操作，又得到一部分牌的序号(n/2+1) ~ (n/2+n/4)，同时从链表中移除他们
 * 循环执行，直到链表仅剩余1个元素，就是序号n的牌。
 */

public class Poker {


    public static void run(int num) {
        Node head = init(num);
        print(head);
        int[] buf = new int[num];
        int idx = 0;
        int leave = num;
        boolean first = true;
        int od = 0;
        while (leave > 1) {
            int half = 0;
            boolean oddEven;
            if (leave % 2 == 0) {
                half = leave / 2;
            } else {
                half = (leave + 1) / 2;
            }
            if (num % 2 == 0) {
                oddEven = true;
            } else {
                if (od % 2 == 0)
                    oddEven = true;
                else
                    oddEven = false;
                od++;
            }
            if (first) {
                oddEven = true;
                first = false;
            }
            Poker.Node p = head;
            for (int i = 1; i <= half; i++) {
                if (oddEven) {
                    buf[idx] = head.data;
                    idx++;
                    head = head.next;
                    p = head;
                    oddEven = false;
                } else {
                    buf[idx] = p.next.data;
                    idx++;
                    p.next = p.next.next;
                    p = p.next;
                }
            }
            leave = leave - half;
            System.out.println("----------------------------------");
            print(head);
            printbuf(buf);


        }
        buf[idx] = head.data;
        printbuf(buf);
        int[] sx = new int[num];
        for (int i = 0; i < buf.length; i++) {
            sx[buf[i] - 1] = i + 1;
        }
        printbuf(sx);
    }

    static class Node {
        public int data;
        public Node next;
    }

    static Node init(int n) {
        Node head = null;
        Node last = null;
        for (int i = 1; i <= n; i++) {
            if (head == null) {
                head = last = new Node();
                head.data = i;
                head.next = null;
            } else {
                last.next = new Node();
                last = last.next;
                last.data = i;
                last.next = null;
            }

        }

        return head;
    }

    static void print(Node node) {
        System.out.println("hels start ****");
        while (node.next != null) {
            System.out.println("hels:" + node.data);
            node = node.next;
        }
        System.out.println("hels:" + node.data);
    }

    static void printbuf(int[] buf) {
        System.out.println("buf start ******");
        for (int i = 0; i < buf.length; i++) {
            System.out.println("buf:" + buf[i]);
        }
    }

}
