package com.wusd.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void add() {
        System.out.println("userDao.add()...");
        jdbcTemplate.execute("INSERT INTO USER(userId, userName, age) VALUES ('rur', 'lij', 25)");
        System.out.println(1 / 0);
    }
}
