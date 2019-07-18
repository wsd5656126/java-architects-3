package com.wusd;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> userClazz = Class.forName("com.wusd.entity.po.User");
        Object newInstance = userClazz.newInstance();
        System.out.println();
    }
}
