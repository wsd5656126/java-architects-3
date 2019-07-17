package com.wusd;

import com.wusd.core.ClassPathXmlApplicationContext;
import com.wusd.service.UserService;
import com.wusd.service.impl.UserServiceImpl;

public class Test {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("com.wusd.service");
        UserService userService = (UserServiceImpl) applicationContext.getBean("userServiceImpl");
        userService.add();
    }
}
