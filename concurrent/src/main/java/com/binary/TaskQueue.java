package com.binary;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Binary on 2020/5/26
 */
public class TaskQueue {

    Queue<String> queue = new LinkedList<>();

    public synchronized void addTask(String s) {
        queue.remove();
    }

    public synchronized String getTask() {
        while (queue.isEmpty()) {

        }
        return queue.remove();
    }
}
