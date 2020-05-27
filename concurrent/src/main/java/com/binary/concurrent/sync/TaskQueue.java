package com.binary.concurrent.sync;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * @author Binary on 2020/5/26
 */
public class TaskQueue {

    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        queue.add(s);
        this.notifyAll();
    }

    public synchronized String getTask() throws InterruptedException {
        while (queue.isEmpty()) {
            this.wait(5000);
        }
        return queue.remove();
    }

    public static void main(String[] args) throws InterruptedException {
        final TaskQueue taskQueue = new TaskQueue();
        new Thread() {
            @Override
            public void run() {
                try {
                    taskQueue.getTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {

                    taskQueue.getTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {

                    taskQueue.getTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                try {
                    taskQueue.getTask();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Thread.sleep(5000);
        taskQueue.addTask("asdas");
    }
}
