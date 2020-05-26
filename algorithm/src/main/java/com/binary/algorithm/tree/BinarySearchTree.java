package com.binary.algorithm.tree;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Binary
 */
public class BinarySearchTree<E extends Comparable<E>> {

    private class Node {
        public E e;
        public Node left;
        public Node right;

        public Node(E e) {
            this.e = e;
        }

    }

    private Node root;
    private int size;

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void remove(Node node, E e) {
        if (node == null) {
            return;
        }
        if (e.compareTo(node.e) < 0) {
            remove(node.left, e);
        } else if (e.compareTo(node.e) > 0){
            remove(node.right, e);
        }
        if (node.e.equals(e)) {
            node = null;
        }
    }
    // 二分搜索树的层序遍历
    public void levelOrder() {
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Node cur = queue.remove();
            System.out.println(cur.e);

            if (cur.left != null) {
                queue.add(cur.left);
            }
            if (cur.right != null) {
                queue.add(cur.right);
            }
        }
    }

    public E min() {
        if (root == null) {
            return null;
        }
        Node cur = root;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur.e;
    }

    public E max() {
        if (root == null) {
            return null;
        }
        Node cur = root;
        while (cur.right != null) {
            cur = cur.right;
        }
        return cur.e;
    }

    public void add(E e) {
        if (root == null) {
            root = new Node(e);
        } else {
            add(root, e);
        }
        size++;
    }

    private void add(Node node, E e) {
        if (e.compareTo(node.e) < 0) {
            if (node.left == null) {
                node.left = new Node(e);
            } else {
                add(node.left, e);
            }
        } else if (e.compareTo(node.e) > 0) {
            if (node.right == null) {
                node.right = new Node(e);
            } else {
                add(node.right, e);
            }
        }
    }

    public boolean contains(E e) {
        return contains(root, e);
    }

    private boolean contains(Node node, E e) {
        if (node == null) {
            return false;
        }
        if (e.compareTo(node.e) < 0) {
            return contains(node.left, e);
        } else if (e.compareTo(node.e) > 0){
            return contains(node.right, e);
        }
        return true;
    }

    public void preOrder() {
        preOrder(root);
    }

    public void inOrder() {
        inOrder(root);
    }

    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node node) {
        if (node == null) {
            return;
        }

        postOrder(node.left);
        postOrder(node.right);
        System.out.println(node.e);
    }

    private void inOrder(Node node) {
        if (node == null) {
            return;
        }

        inOrder(node.left);
        System.out.println(node.e);
        inOrder(node.right);
    }

    // 前序遍历以node为根节点的二叉树
    private void preOrder(Node node) {
        if (node == null) {
            return;
        }
        System.out.println(node.e);
        preOrder(node.left);
        preOrder(node.right);
    }

}
