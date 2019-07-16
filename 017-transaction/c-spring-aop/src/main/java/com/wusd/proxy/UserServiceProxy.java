package com.wusd.proxy;

import com.wusd.service.UserService;
import com.wusd.service.impl.UserServiceImpl;

public class UserServiceProxy {
    private UserService userService;
    public UserServiceProxy(UserService userService) {
        this.userService = userService;
    }
    public void add() {
        System.out.println("开始事务");
        userService.add();
        System.out.println("提交事务");
    }

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        UserServiceProxy userServiceProxy = new UserServiceProxy(userService);
        userServiceProxy.add();
    }
}
