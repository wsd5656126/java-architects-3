package com.wusd.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * 作用在于让Spring Boot 根据应用所声明的依赖来对Spring框架进行自动配置
 */
//@EnableAutoConfiguration
public class HelloController {
    @RequestMapping("/hello")
    public String index() {
        return "Hello World";
    }

//    public static void main(String[] args) {
//        SpringApplication.run(HelloController.class, args);
//    }
}
