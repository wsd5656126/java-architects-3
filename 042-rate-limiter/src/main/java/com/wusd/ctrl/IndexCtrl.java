package com.wusd.ctrl;

import com.google.common.util.concurrent.RateLimiter;
import com.wusd.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class IndexCtrl {
    @Autowired
    private OrderService orderService;
    RateLimiter rateLimiter = RateLimiter.create(100);

    @RequestMapping("/addOrder")
    public String addOrder() {
        //1.限流处理 限流正常要放在网关 客户端从桶中获取对应的令牌,为什么返回double结果,
        // 结果表示:从桶中拿到令牌等待时间.
        //2.如果获取不到令牌,就会一直等待.设置服务降级处理(相当于配置在规定时间内如果没有
        //获取到令牌的话,直接走服务降级.
        double acquire = rateLimiter.acquire();
        System.out.printf("从桶中等待时间%s\n", acquire);
        //如果在500毫秒没有获取到令牌,直接走服务降级处理
        boolean tryAcquire = rateLimiter.tryAcquire(500, TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            System.out.printf("抢不到了\n");
            return "别抢了";
        }
        boolean addOrderResult = orderService.addOrder();
        if (addOrderResult) {
            System.out.printf("抢购成功!等待时间:%s\n", rateLimiter.acquire());
            return "抢购成功";
        }
        return "抢购成功";
    }
}
