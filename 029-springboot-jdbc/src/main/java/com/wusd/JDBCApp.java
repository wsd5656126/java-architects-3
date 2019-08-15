package com.wusd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.wusd")
@EnableAutoConfiguration
public class JDBCApp {
    public static void main(String[] args) {
        SpringApplication.run(JDBCApp.class, args);
    }
}
