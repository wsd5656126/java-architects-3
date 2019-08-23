package com.wusd.controller;

import com.wusd.AccessTokenApp;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccessTokenApp.class)
public class AuthControllerTest {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @org.junit.Test
    public void getAccessToken() throws Exception {
        String wusd = redisTemplate.opsForValue().get("wusd");
        redisTemplate.opsForValue().set("test", "wusx");
        System.out.println();
    }

}