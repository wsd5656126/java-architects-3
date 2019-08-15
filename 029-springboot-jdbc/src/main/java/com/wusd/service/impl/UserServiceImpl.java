package com.wusd.service.impl;

import com.wusd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void createUser(String name, Integer age) {
        String userId = UUID.randomUUID().toString();
        String sql = "insert into user(userId,userName,age) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, userId, name, age);
    }
}
