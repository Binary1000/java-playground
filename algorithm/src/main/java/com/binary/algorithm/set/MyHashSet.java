package com.binary.algorithm.set;

class MyHashSet {

    private class Node {
        private Integer value;
        private Node left;
        private Node right;

        public Node(int value) {
            this.value = value;
        }

        public Node() {

        }

    }

    private int size = 0;

    private Node root;

    public MyHashSet() {

    }

    public void add(int key) {
        if (root == null) {
            root = new Node(key);
        } else {
            add(root, key);
        }
    }

    private void add(Node node, int key) {
        if (key < node.value) {
            if (node.left == null) {
                node.left = new Node(key);
            } else {
               add(node.left, key);
            }
        } else if (key > node.value) {
            if (node.right == null) {
                node.right = new Node(key);
            } else {
                add(node.right, key);
            }
        }

    }

    public void remove(int key) {
        root = remove(root, key);
    }

    private Node remove(Node node, int key) {
        if (node == null)
            return null;

        if (key < node.value) {
            node.left = remove(node.left, key);
            return node;
        } else if (key > node.value) {
            node.right = remove(node.right, key);
            return node;
        } else {
            if (node.left == null) {
                Node rightNode = node.right;
                node.right = null;
                return rightNode;
            }
            if (node.right == null) {
                Node leftNode = node.left;
                node.left = null;
                return leftNode;
            }

            Node successor = minimum(node.right);
            successor.right = removeMin(node.right);
            successor.left = node.left;

            node.left = node.right = null;
            return successor;
        }
    }

    public int removeMin() {
        int min = minimum(root).value;
        removeMin(root);
        return min;
    }

    public Node removeMin(Node node) {

        if (node.left == null) {
            Node rightNode = node.right;
            node.right = null;
            return rightNode;
        }

        node.left = removeMin(node.left);
        return node;
    }

    public Node minimum(Node node) {
        Node cur = node;
        while (cur.left != null) {
            cur = cur.left;
        }
        return cur;
    }

    /** Returns true if this set contains the specified element */
    public boolean contains(int key) {
        return contains(root, key);
    }

    private boolean contains(Node node, int key) {
        if (node == null) {
            return false;
        }

        if (node.value == key) {
            return true;
        }

        if (key < node.value) {
            return contains(node.left, key);
        } else {
            return contains(node.right, key);
        }

    }

}

