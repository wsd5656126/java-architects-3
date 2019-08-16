package com.wusd.mapper;

import com.wusd.BaseTest;
import com.wusd.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserMapperTest extends BaseTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public void findUserByName() throws Exception {
        User user = userMapper.findUserByName("lij");
        System.out.println();
    }

}