package com.binary.concurrent.positive;

import java.util.concurrent.locks.StampedLock;

public class Point {

    /**
     * StampedLock与ReadWriteLock相比，改进之处在于，读的过程也允许获取写锁后写入
     * StampedLock是不可重入锁
     */
    private final StampedLock stampedLock = new StampedLock();

}
