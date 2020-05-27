package com.binary.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockingQueue {

    public static void main(String[] args) {
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue<>(20);
        arrayBlockingQueue.add("asd");
        arrayBlockingQueue.add("asd");
        arrayBlockingQueue.add("asd");
        String poll = null;
        try {
            poll = arrayBlockingQueue.poll(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(poll);

    }
}
