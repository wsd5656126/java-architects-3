package com.wusd.service.impl;

import com.wusd.orm.annotation.ExtResource;
import com.wusd.orm.annotation.ExtService;
import com.wusd.service.LogService;
import com.wusd.service.UserService;

@ExtService
public class UserServiceImpl implements UserService {
//    @ExtResource
    private LogService logServiceImpl;

    @Override
    public void add() {
        logServiceImpl.add();
        System.out.println("UserServiceImpl.add()...");
    }
}
