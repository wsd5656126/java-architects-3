package com.wusd.service;

import com.wusd.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;

    @Transactional
    public void insert(String userId, String userName, Integer age) {
        int i = userMapper.insert(userId, userName, age);
        i = i / 0;
    }
}
