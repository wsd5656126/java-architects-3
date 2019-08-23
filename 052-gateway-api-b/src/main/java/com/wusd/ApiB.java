package com.wusd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class ApiB {
    public static void main(String[] args) {
        SpringApplication.run(ApiB.class, args);
    }

    @RequestMapping("/index")
    public String index() {
        return "I am b";
    }
}
