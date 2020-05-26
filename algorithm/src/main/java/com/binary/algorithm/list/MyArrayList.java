package com.binary.algorithm.list;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyArrayList<AnyType> implements Iterable<AnyType> {

    private static final int DEFAULT_CAPACITY = 10;

    private int size;
    private AnyType[] items;

    public MyArrayList() {
        clear();
    }

    public void clear() {
        size = 0;
        ensureCapacity(DEFAULT_CAPACITY);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void rangeCheck(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public AnyType get(int index) {
        rangeCheck(index);
        return items[index];
    }

    public AnyType set(int index, AnyType newItem) {
        rangeCheck(index);
        AnyType old = items[index];
        items[index] = newItem;
        return old;
    }

    @Override
    public String toString() {
        return "MyArrayList{" +
                "size=" + size +
                ", items=" + Arrays.toString(Arrays.copyOf(items, size)) +
                '}';
    }

    public void ensureCapacity(int newCapacity) {
        if (newCapacity < size) return;

        AnyType[] old = items;
        items = (AnyType[]) new Object[newCapacity];
        for (int i = 0; i < size; i++) {
            items[i] = old[i];
        }
    }

    public boolean add(AnyType newItem) {
        add(size, newItem);
        return true;
    }

    public void add(int index, AnyType newItem) {
        if (size == items.length) {
            ensureCapacity(size * 2 + 1);
        }
        for (int i = size; i > index; i--) {
            items[i] = items[i - 1];
        }
        items[index] = newItem;

        size++;
    }

    public AnyType remove(int index) {
        AnyType removedItem = items[index];
        for (int i = index; i < size; i++) {
            items[i] = items[i + 1];
        }
        size--;
        return removedItem;
    }

    @Override
    public Iterator<AnyType> iterator() {
        return new ArrayListIterator();
    }

    private class ArrayListIterator implements Iterator<AnyType> {

        private int current = 0;

        @Override
        public boolean hasNext() {
            return current < size;
        }

        @Override
        public AnyType next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items[current++];
        }

        public void remove() {
            MyArrayList.this.remove(--current);
        }


    }
}
