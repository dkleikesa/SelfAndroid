package com.ljz.javarun;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 平衡二叉树判断问题：
 * 判断是否为平衡二叉树，平衡二叉树特性为左子树和右子树的深度差不超过1，并且其每个子树必须都为平衡二叉树。
 * 分析：
 * 由于平衡二叉树的每棵子树都是平衡二叉树，从下往上遍历，只要发现任意一个子树不是平衡二叉树即可终止遍历返回结果。
 * 1. 利用递归方式遍历每一个节点。
 * 2. 从最底层节点开始计数，统计每棵子树，左右节点的深度。
 * 3. 判断此树是否平衡，平衡则返回此树的深度，不平衡直接返回-1，终止遍历
 */

public class BalanceTree {

    static void run() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.right = new TreeNode(6);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.left.right.left = new TreeNode(7);
        boolean ret = isBalanced(root);
        System.out.println("ban:" + ret);
    }

    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    //广度优先遍历
    private static int getDepthNode(TreeNode root) {
        if (root == null)
            return 0;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int dep = 0;

        while (!queue.isEmpty()) {
            dep++;

            int sizeOfLevel = queue.size();
            int index = 0;

            //遍历一层
            while (index < sizeOfLevel) {
                index++;

                //每遍历一层，便将下一层所有值放到队列中。
                TreeNode node = queue.poll();
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }

            }
        }
        return dep;
    }

    static boolean isBalanced1(TreeNode root) {
        if (root == null)
            return true;
        return getDepth1(root) != -1;
    }

    //深度优先遍历
    private static int getDepth1(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 如果已经不平衡，直接返回null即可
        int lnum = getDepth1(root.left);
        if (lnum == -1) {
            return -1;
        }

        int rnum = getDepth1(root.right);
        if (rnum == -1) {
            return -1;
        }
        // 判断当前节点如果平衡则返回树高度， 否则返回-1，表示不平衡
        return Math.abs(lnum - rnum) > 1 ? -1 : Math.max(lnum, rnum) + 1;
    }


    static boolean isBalanced(TreeNode root) {
        if (root == null)
            return true;
        int left = getDepth(root.left);
        int right = getDepth(root.right);
        int diff = left - right;
        if (diff > 1 || diff < -1)
            return false;
        return isBalanced(root.left) && isBalanced(root.right);
    }


    //深度优先遍历
    private static int getDepth(TreeNode root) {
        if (root == null) {
            return 0;
        }

        int lnum = 0;
        int rnum = 0;
        if (root.left != null) {
            lnum = getDepth(root.left);
        }

        if (root.right != null) {
            rnum = getDepth(root.right);
        }
        return Math.max(lnum, rnum) + 1;

    }
}
