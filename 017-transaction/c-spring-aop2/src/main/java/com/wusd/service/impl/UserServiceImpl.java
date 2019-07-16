package com.wusd.service.impl;

import com.wusd.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

//user 服务层
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    //spring 事务封装,aop技术
    @Override
    public void add() {
        System.out.println("userService.add()");
    }
    public void error() {
        String sql = "UPDATE `user` set userName = 'li\\'s husband' where `age` = 26";
        jdbcTemplate.execute(sql);
//        System.out.println(1 / 0);
    }
}
