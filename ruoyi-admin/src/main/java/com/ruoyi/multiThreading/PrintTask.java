package com.ruoyi.multiThreading;

import java.util.concurrent.locks.ReentrantLock;

// 打印任务类
public class  PrintTask extends Thread {
    private boolean printOne;
    // 锁对象，用于同步
    private static final ReentrantLock lock = new ReentrantLock ();
    // flag 控制打印的顺序，true 代表打印 "1"，false 代表打印 "0"
    private static boolean flag = true;
    // 构造函数，决定当前线程是打印 "1" 还是打印 "0"
    public PrintTask(boolean printOne) {
        this.printOne = printOne;
    }

    @Override
    public void run() {
        // 无限循环，直到程序结束
        while (true) {
            //请求锁
            lock.lock();
            try {
                // 判断当前线程是否可以打印 线程一只有输出1线程二只会输出0
                if (printOne && flag) {
                    System.out.print("1"); // 打印 "1"
                    flag = false; // 设置 flag 为 false，表示下一个是 "0"
                } else if (!printOne && !flag) {
                    System.out.print("0"); // 打印 "0"
                    flag = true; // 设置 flag 为 true，表示下一个是 "1"
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                //释放锁
                lock.unlock();
            }
        }
    }
}

