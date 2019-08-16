package com.wusd.web.jsp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MemberService {

    @Async
    //使用AOP技术在运行时 创建一个单独线程进行执行
    public void send() {
        for (int i = 0; i < Integer.MAX_VALUE; i++)
            System.out.println(i);
    }
}
