package com.binary.algorithm.list;

import java.util.Iterator;

public class MyLinkedList<AnyType> implements Iterable<AnyType> {

    private class Node {

        private AnyType value;
        private Node next = null;

        Node(AnyType value) {
            this.value = value;
        }

        Node(AnyType value, Node next) {
            this.value = value;
            this.next = next;
        }


    }

    private Node head = null;
    private Node dummyHead;
    private int size;

    public MyLinkedList() {
        size = 0;
        dummyHead = new Node(null, head);
    }

    public void add(AnyType value) {

        Node current = dummyHead;
        while (current.next != null) {
            current = current.next;
        }
        current.next = new Node(value);
        size++;
    }

    public void add(int index, AnyType value) {

        Node prev = dummyHead;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }
        prev.next = new Node(value, prev.next);
        size++;
    }

    public void addFirst(AnyType value) {
        head = new Node(value, head);
        size++;
    }

    public AnyType getFirst() {
        return head.value;
    }

    public AnyType removeFirst() {
        if (head == null) {
            return null;
        } else {
            AnyType value = head.value;
            head = head.next;
            return value;
        }
    }

    public int getSize() {
        return size;
    }

    public void print() {
        Node current = dummyHead.next;
        while (current != null) {
            System.out.println(current.value);
            current = current.next;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<AnyType> iterator() {
        return null;
    }
}
