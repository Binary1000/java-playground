package com.binary.algorithm.heap;

import java.util.ArrayList;
import java.util.List;

public class MaxHeap<E extends Comparable<E>> {

    private List<E> data;

    public MaxHeap() {
        data = new ArrayList<>();
    }

    public MaxHeap(E[] arr) {
        for (int i = parent(arr.length -1); i >= 0; i--) {
            siftDown(i);
        }
    }

    private int parent(int index) {
        if (index == 0) {
            throw new IllegalArgumentException();
        }
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        return index * 2 + 1;
    }

    private int rightChild(int index) {
        return index * 2 + 2;
    }

    public int size() {
        return data.size();
    }

    public boolean isEmpty() {
        return data.size() == 0;
    }

    public void add(E e) {
        data.add(e);
        siftUp(size() - 1);
    }

    private void swap(int i, int j) {
        E tmp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, tmp);
    }

    private void siftUp(int index) {
        while (index > 0 && data.get(parent(index)).compareTo(data.get(index)) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    private void siftDown(int index) {

        while (leftChild(index) < data.size()) {

            int left = leftChild(index);
            int right = left + 1;

            if (right < data.size() && data.get(right).compareTo(data.get(left)) > 0) {
                left = rightChild(index);
            }

            if (data.get(index).compareTo(data.get(left)) >= 0) {
                break;
            }

            swap(index, left);
            index = left;

        }

    }

    public E replace(E e) {
        E max = getMax();
        data.set(0, e);
        siftDown(0);
        return max;
    }

    public E getMax() {
        return data.get(0);
    }

    public E extractMax() {
        E max = getMax();
        swap(0, data.size() - 1);
        data.remove(data.size() - 1);
        siftDown(0);
        return max;
    }

}
