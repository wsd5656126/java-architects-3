package com.wusd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan({"com.wusd.mapper"})
@SpringBootApplication
public class AccessTokenApp {
    public static void main(String[] args) {
        SpringApplication.run(AccessTokenApp.class, args);
    }
}
