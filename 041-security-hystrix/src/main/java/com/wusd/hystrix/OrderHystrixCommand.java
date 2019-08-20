package com.wusd.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.*;
import com.wusd.service.MemberService;
import lombok.extern.slf4j.Slf4j;

public class OrderHystrixCommand extends HystrixCommand<JSONObject> {
//    @Autowired
    MemberService memberService;

    public OrderHystrixCommand(MemberService memberService) {
        super(setter());
        this.memberService = memberService;
    }

    @Override
    protected JSONObject run() throws Exception {
        JSONObject member = memberService.getMember();
        System.out.printf("OrderHystrixCommand.run...threadName->%s,member->%s", Thread.currentThread().getName(),
                member);
//        System.out.println("线程名称:" + Thread.currentThread().getName() + ",订单服务调用会员服务member:" + member);
        return member;
    }

    private static Setter setter() {
        //服务分组
        HystrixCommandGroupKey groupKey = HystrixCommandGroupKey.Factory.asKey("members");
        //服务标识
        HystrixCommandKey commandKey = HystrixCommandKey.Factory.asKey("member");
        //线程池名称
        HystrixThreadPoolKey threadPoolKey = HystrixThreadPoolKey.Factory.asKey("member-pool");
        //线程池配置,线程池大小为10,线程存活时间15秒 队列等待阈值为100,超过100执行拒绝策略
        HystrixThreadPoolProperties.Setter threadPoolProperties = HystrixThreadPoolProperties.Setter()
                .withCoreSize(10)
                .withKeepAliveTimeMinutes(15)
                .withQueueSizeRejectionThreshold(100);
        //命令属性配置Hystrix开启超时
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                //采用线程池方式实现服务隔离
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                //禁止
                .withExecutionTimeoutEnabled(false);
        return HystrixCommand.Setter
                .withGroupKey(groupKey)
                .andCommandKey(commandKey)
                .andThreadPoolKey(threadPoolKey)
                .andThreadPoolPropertiesDefaults(threadPoolProperties)
                .andCommandPropertiesDefaults(commandProperties);
    }

    @Override
    protected JSONObject getFallback() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "wusd");
        jsonObject.put("age", 26);
        return jsonObject;
    }
}




















