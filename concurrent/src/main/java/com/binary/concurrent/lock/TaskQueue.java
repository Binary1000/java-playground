package com.binary.concurrent.lock;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskQueue {

    Queue<String> queue = new LinkedList<>();
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    public void addTask(String e) {
        lock.lock();
        try {
            queue.add(e);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask(long time, TimeUnit timeUnit) throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                if (!condition.await(time, timeUnit)) {
                    return null;
                }
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TaskQueue taskQueue = new TaskQueue();
        System.out.println(taskQueue.getTask(5, TimeUnit.SECONDS));
        System.out.println(taskQueue.getTask(5, TimeUnit.SECONDS));
        taskQueue.addTask("asdasdas");
        System.out.println(taskQueue.getTask(5, TimeUnit.SECONDS));
    }
}
