package com.wusd.service;

import com.wusd.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.Assert.*;

public class UserServiceTest extends BaseTest {
    @Autowired
    UserService userService;
    @Test
    public void insert() throws Exception {
        userService.insert(UUID.randomUUID().toString(), "love lij", 25);
    }

}