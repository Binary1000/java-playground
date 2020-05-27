package com.binary.concurrent.positive;

import java.util.Arrays;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class Counter {

    /**
     * 使用ReadWriteLock可以提高读取效率
     * 只允许一个线程写入，允许多个线程同时读取
     * 适用于读多写少的场景。
     * 如果有线程在读，写线程需要等待读线程释放锁后才能获得锁，即读的过程不允许写，这是一种悲观的读锁。
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private int[] counts = new int[10];

    /**
     * 同一时间只有一个线程能调用该方法
     * @param index
     */
    public void inc(int index) {
        // 获得写锁
        writeLock.lock();
        try {
            counts[index] += 1;
        } finally {
            writeLock.unlock();
        }
    }


    public int[] get() {
        // 获得读锁
        readLock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            readLock.unlock();
        }
    }
}
