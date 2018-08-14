package com.ljz.javarun;

import java.util.ArrayList;

/**
 * 反向输出链表内容问题：
 * 反向输出链表内容，输入一个链表，从尾到头打印链表每个节点的值。
 * 分析：
 * 1. 利用java API特性，List.add(int index,Object o),遍历链表，每次都把新数据插入到0位置
 * 2. 新建一个链表，反向保存所有数据(参考ReverseList())
 * 3. 递归，从最内部返回
 */

public class ListRevert {

    public static void run() {
        ListNode list = new ListNode(67);
        list.next = new ListNode(0);
        list.next.next = new ListNode(24);
        list.next.next.next = new ListNode(58);
        ArrayList<Integer> ret = printListFromTailToHeadRecurrent(list);
        for (int i = 0; i < ret.size(); i++) {
            System.out.println("ret:" + ret.get(i).intValue());
        }

    }

    public static ArrayList<Integer> printListFromTailToHead(ListNode listNode) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (listNode == null) {
            return list;
        }

        ListNode head = listNode;
        while (head.next != null) {
            list.add(0, head.val);
            head = head.next;
        }
        list.add(0, head.val);
        return list;
    }

    static ArrayList<Integer> list = new ArrayList<Integer>();

    public static ArrayList<Integer> printListFromTailToHeadRecurrent(ListNode listNode) {
        if (listNode == null) {
            return list;
        }
        printListFromTailToHeadRecurrent(listNode.next);
        list.add(listNode.val);
        return list;
    }


    public static class ListNode {
        int val;
        ListNode next = null;

        ListNode(int val) {
            this.val = val;
        }
    }

    public ListNode ReverseList(ListNode head) {

        ListNode node = head;
        ListNode p=null;
        while (node != null){
            if(p==null){
                p = node;
                node = node.next;
                p.next = null;
                continue;
            }
            ListNode c = node;
            node = node.next;
            c.next = p;
            p=c;
        }
        return p;

    }
}
