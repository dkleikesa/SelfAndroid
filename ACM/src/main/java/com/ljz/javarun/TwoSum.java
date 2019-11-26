package com.ljz.javarun;

/**
 * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
 * <p>
 * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
 * <p>
 * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
 * <p>
 * 示例：
 * <p>
 * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
 * 输出：7 -> 0 -> 8
 * 原因：342 + 465 = 807
 */
public class TwoSum {

    public static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }


    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        if (l1 == null) {
            return null;
        }
        if (l2 == null) {
            return null;
        }

        ListNode ret = null;
        ListNode cur = null;
        ListNode cur1 = l1;
        ListNode cur2 = l2;
        int j = 0;
        while ((cur1 != null )||( cur2 != null)) {
            int i = j;
            if (cur1 != null) {
                i += cur1.val;
                cur1 = cur1.next;
            }
            if (cur2 != null) {
                i += cur2.val;
                cur2 = cur2.next;
            }

            if(i>9){
                j = i/10;
            }else{
                j = 0;
            }


            ListNode tem = new ListNode((i % 10));


            if (ret == null) {
                ret = tem;
                cur = tem;
            } else {
                cur.next = tem;
                cur = tem;
            }
        }
        if(j!=0){
            ListNode tem = new ListNode((j % 10));
            cur.next = tem;
        }
        return ret;

    }


    public static void run() {

        ListNode l1 = new ListNode(9);
        ListNode l2 = new ListNode(1);
        ListNode t;
        t = l2.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        t = t.next = new ListNode(9);
        ListNode r = addTwoNumbers(l1, l2);
        while (r != null) {
            System.out.println("" + r.val);
            r = r.next;
        }

    }
}
