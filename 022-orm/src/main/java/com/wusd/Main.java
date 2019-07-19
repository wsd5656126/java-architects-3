package com.wusd;

import com.wusd.entity.po.User;
import com.wusd.mapper.UserMapper;
import com.wusd.orm.sql.SqlSession;

public class Main {
    public static void main(String[] args) {
        UserMapper mapper = SqlSession.getMapper(UserMapper.class);
        User lij = mapper.select("lij", 25);
        System.out.println();
    }
}
