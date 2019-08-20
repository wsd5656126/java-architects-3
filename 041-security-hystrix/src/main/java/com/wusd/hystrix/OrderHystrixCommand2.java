package com.wusd.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.*;
import com.wusd.service.MemberService;

public class OrderHystrixCommand2 extends HystrixCommand<JSONObject> {
//    @Autowired
    MemberService memberService;

    public OrderHystrixCommand2(MemberService memberService) {
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
        HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter()
                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                //使用一个原子计数器来记录当前有多少个线程在运行,当请求进来时先判断计数器
                //的数值,若超过设置的最大线程个数则拒绝该请求,若不超过则通行,这时候计数器+1,请求返回成功后的计数器
                .withExecutionIsolationSemaphoreMaxConcurrentRequests(50);
        return  HystrixCommand.Setter
                .withGroupKey(groupKey)
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




















