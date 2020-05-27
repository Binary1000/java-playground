package com.binary.concurrent.negative;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {

    private final Lock lock = new ReentrantLock();
    private int[] counts = new int[10];

    /**
     * 同一时间只有一个线程能调用该方法
     * @param index
     */
    public void inc(int index) {
        lock.lock();
        try {
            counts[index] += 1;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 有性能问题，同一时间只有一个线程能调用读方法，实际这个方法可以多个线程同时调用。
     */
    public int[] get() {
        lock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            lock.unlock();
        }
    }

}
