package com.binary.concurrent.thread;

/**
 * @author Binary on 2020/5/27
 */
public class TestThread {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread("test") {
            @Override
            public void run() {
                while (true) {

                }
            }
        };

        thread.start();
        thread.join();
        System.out.println(1);
    }
}
