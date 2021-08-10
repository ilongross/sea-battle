package com.ilongross.seafight.concurrency;

public class ThreadAction {

    public static void sleep(long delay) {
        if(delay == 0)
            return;
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
