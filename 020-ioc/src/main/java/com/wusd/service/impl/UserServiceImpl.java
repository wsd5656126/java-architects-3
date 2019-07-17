package com.wusd.service.impl;

import com.wusd.annotation.ExtService;
import com.wusd.service.UserService;

@ExtService
public class UserServiceImpl implements UserService {
    @Override
    public void add() {
        System.out.println("UserServiceImpl.add()...");
    }
}
