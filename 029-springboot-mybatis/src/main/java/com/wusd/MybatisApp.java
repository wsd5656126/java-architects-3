package com.wusd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan("com.wusd")
@EnableAutoConfiguration
@MapperScan(basePackages = {"com.wusd.mapper"})
public class MybatisApp {
}
