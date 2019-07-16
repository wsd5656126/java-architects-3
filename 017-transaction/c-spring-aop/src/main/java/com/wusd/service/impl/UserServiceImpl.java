package com.wusd.service.impl;

import com.wusd.service.UserService;

//user 服务层
public class UserServiceImpl implements UserService {
    //spring 事务封装,aop技术
    @Override
    public void add() {
        System.out.println("userService.add()");
    }
}
