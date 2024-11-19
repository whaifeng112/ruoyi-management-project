package com.ruoyi.multiThreading;

public class Test {
    public static void main(String[] args) {
        //线程一用于输出1
        PrintTask thread1 = new PrintTask(true);
        PrintTask thread0 = new PrintTask(false);
        thread1.start();
        try {
            Thread.sleep(100);
            thread0.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread0.start();
    }
}
