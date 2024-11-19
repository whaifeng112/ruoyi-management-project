package main.java.com.ruoyi.problem_system.controller;

public class Test implements Runnable {

    @Override
    public void run() {
        System.out.println(11);
    }

    public static void main(String[] args) {
        Test test1 = new Test();
        test1.run();
        Test test2= new Test();
        test2.run();
    }
}
