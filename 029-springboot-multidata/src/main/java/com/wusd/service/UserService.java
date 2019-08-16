package com.wusd.service;

import com.wusd.entity.User;
import com.wusd.mapper1.UserMapper;
import com.wusd.mapper2.UserMapper2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserMapper2 userMapper2;

    public void getUserByName(String userName) {
        User user = userMapper.findUserByName(userName);
        User user2 = userMapper2.findUserByName(userName);
        System.out.println();
    }
}
