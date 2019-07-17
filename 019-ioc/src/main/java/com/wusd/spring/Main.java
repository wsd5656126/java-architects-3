package com.wusd.spring;

import com.wusd.entity.po.User;
import com.wusd.spring.core.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring.xml");
        try {
            User user = (User) applicationContext.getBean("user");
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
