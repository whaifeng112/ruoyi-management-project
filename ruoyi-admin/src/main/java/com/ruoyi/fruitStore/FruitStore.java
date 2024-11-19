package com.ruoyi.fruitStore;

import com.alibaba.fastjson2.reader.FieldReader;

// 定义一个FruitStore类，表示超市
public class FruitStore {


    // 计算A顾客购买的苹果和草莓总价
    public static double calculateTotalPriceA(int apples, int strawberries) {
        //First
        Fruits apple = new Fruits("苹果", apples,8.00,1);
        Fruits strawberry = new Fruits("芒果",strawberries,13.00,1);
        double total = apple.calculateTotalPrice()+strawberry.calculateTotalPrice();
        return total;
    }

    // 计算B顾客购买的苹果、草莓和芒果总价
    public static double calculateTotalPriceB(int apples, int strawberries, int mangoes) {
        Fruits apple = new Fruits("苹果", apples,8.00,1);
        Fruits strawberry = new Fruits("芒果",strawberries,13.00,1);
        Fruits mangos = new Fruits("草莓",mangoes,20,1);
        double total = apple.calculateTotalPrice()+strawberry.calculateTotalPrice()+mangos.calculateTotalPrice();
        return total;
    }

    // 计算C顾客购买的苹果、草莓和芒果总价（草莓打8折）
    public static double calculateTotalPriceC(int apples, int strawberries, int mangoes) {
        Fruits apple = new Fruits("苹果", apples,8.00,1);
        Fruits strawberry = new Fruits("芒果",strawberries,13.00,1);
        Fruits mangos = new Fruits("草莓",mangoes,20,0.8);
        double total = apple.calculateTotalPrice()+strawberry.calculateTotalPrice()+mangos.calculateTotalPrice();
        return total;
    }

    // 计算D顾客购买的苹果、草莓和芒果总价（草莓打8折，满100减10）
    public static double calculateTotalPriceD(int apples, int strawberries, int mangoes) {
        Fruits apple = new Fruits("苹果", apples,8.00,1);
        Fruits strawberry = new Fruits("芒果",strawberries,13.00,1);
        Fruits mangos = new Fruits("草莓",mangoes,20,0.8);
        double total = apple.calculateTotalPrice()+strawberry.calculateTotalPrice()+mangos.calculateTotalPrice();
        // 如果总价超过100元，减10元
        if (total >= 100) {
            total -= 10;
        }

        return total;
    }

    public static void main(String[] args) {
        // 模拟不同顾客的购买情况

        // 顾客A购买了3斤苹果和2斤草莓
        int applesA = 3, strawberriesA = 2;
        double totalA = calculateTotalPriceA(applesA, strawberriesA);
        System.out.println("顾客A的总价：" + totalA + "元");

        // 顾客B购买了2斤苹果、3斤草莓和1斤芒果
        int applesB = 2, strawberriesB = 3, mangoesB = 1;
        double totalB = calculateTotalPriceB(applesB, strawberriesB, mangoesB);
        System.out.println("顾客B的总价：" + totalB + "元");

        // 顾客C购买了2斤苹果、3斤草莓和1斤芒果（草莓打8折）
        int applesC = 2, strawberriesC = 3, mangoesC = 1;
        double totalC = calculateTotalPriceC(applesC, strawberriesC, mangoesC);
        System.out.println("顾客C的总价：" + totalC + "元");

        // 顾客D购买了4斤苹果、5斤草莓和2斤芒果（草莓打8折，满100减10）
        int applesD = 4, strawberriesD = 5, mangoesD = 2;
        double totalD = calculateTotalPriceD(applesD, strawberriesD, mangoesD);
        System.out.println("顾客D的总价：" + totalD + "元");
    }
}
