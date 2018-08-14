package com.ljz.javarun;

/**
 * LRU问题:
 * 缓存淘汰策略，优先淘汰缓存中最近最少使用的部分
 * 解析：
 * 定义一个链表，保存所有缓存，一共提供两个操作方法add、remove、get。add的元素保存在head，get的元素移动到head，满了以后淘汰last位置。
 */

public class LRU {

    static class Node {
        int data;
        Node next;
        Node prev;
    }

    Node root;
    int size = 0;
    int max = 10;

    void add(Node n) {

        if (size < max) {
            size++;
        } else {
            Node n1 = root;
            while (n1.next != null) {
                n1 = n1.next;
            }
            n1.prev.next = null;

        }
        n.next = root;
        n.prev = null;
        root.prev = n;
        root = n;
    }

    void get(Node n) {
        Node t = root;
        Node f = null;
        while (t.next != null) {
            if (t.equals(n)) {
                f = t;
                break;
            }

            t = t.next;
        }

        if (f != null) {
            f.prev.next = f.next;
            f.next.prev = f.prev;
            f.next = root;
            f.prev = null;
            root = f;
        }


    }

}
