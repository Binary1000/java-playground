package com.binary.concurrent.positive;

import java.util.concurrent.locks.StampedLock;

public class Point {

    /**
     * StampedLock与ReadWriteLock相比，改进之处在于，提观了乐观读锁，读的过程也允许获取写锁后写入
     * StampedLock是不可重入锁
     */
    private final StampedLock stampedLock = new StampedLock();

    private double x;
    private double y;

    public void move(double deltaX, double deltaY) {
        // 获取写锁
        long stamp = stampedLock.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            // 释放写锁
            stampedLock.unlockWrite(stamp);
        }
    }

    public double distanceFromOrigin() {
        // 获得一个悲观写锁
        long stamp = stampedLock.tryOptimisticRead();

        double currentX = x;
        double currentY = y;

        // 检查乐观读锁后有其他写锁发生
        if (!stampedLock.validate(stamp)) {
            // 获得一个悲观读锁
            stamp = stampedLock.readLock();
            try {
                currentX = x;
                currentY = y;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }

        return Math.sqrt(currentX * currentX + currentY + currentY);
    }

}
