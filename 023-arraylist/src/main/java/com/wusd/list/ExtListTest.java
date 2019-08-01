package com.wusd.list;

public class ExtListTest {
    public static void main(String[] args) {
        ExtList extList = new ExtArrayList();
        for (int i = 0; i < 100; i++) {
//            extList.add("wusd" + i);
            extList.add("lij" + i);
        }
        for (int i = 0; i < 100; i++) {
//            extList.add("wusd" + i);
            System.out.println(extList.get(i));
        }
    }
}
