package com.wusd.ctrl;

import com.alibaba.fastjson.JSONObject;
import com.wusd.hystrix.OrderHystrixCommand;
import com.wusd.hystrix.OrderHystrixCommand2;
import com.wusd.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderCtrl {
    @Autowired
    MemberService memberService;
    @RequestMapping("/orderIndex")
    public Object orderIndex() throws InterruptedException {
        JSONObject member = memberService.getMember();
        System.out.printf("OrderCtrl.orderIndex...threadName->%s,member->%s",
                Thread.currentThread().getName(), member);
//        System.out.println("当前线程名称:" + Thread);
        return member;
    }

    @RequestMapping("/orderIndexHystrix")
    public Object orderIndexHystrix() throws InterruptedException {
        return new OrderHystrixCommand(memberService).execute();
    }

    @RequestMapping("/orderIndexHystrix2")
    public Object orderIndexHystrix2() throws InterruptedException {
        return new OrderHystrixCommand2(memberService).execute();
    }

    @RequestMapping("/findIndex")
    public Object findIndex() {
        System.out.printf("Order.findIndex...threadName->%s", Thread.currentThread().getName());
        return "findOrderIndex";
    }
}
