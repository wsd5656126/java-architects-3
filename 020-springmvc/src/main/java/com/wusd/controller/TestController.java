package com.wusd.controller;

import com.wusd.mvc.annotation.ExtController;
import com.wusd.mvc.annotation.ExtRequestMapping;

@ExtController
@ExtRequestMapping("/test")
public class TestController {
    @ExtRequestMapping("/add")
    public String add() {
        System.out.println("手写spring-mvc框架...");
        return "index";
    }

}
