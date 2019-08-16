package com.wusd.service;

import com.wusd.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;

    @Test
    public void getUserByName() throws Exception {
        userService.getUserByName("lij");
    }

}