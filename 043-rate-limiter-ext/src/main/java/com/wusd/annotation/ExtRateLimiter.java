package com.wusd.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExtRateLimiter {
    //以每秒未单位固定的速率值往令牌桶中添加令牌
    double permitsPerSecond();
    //以规定的毫秒数中,如果没有获取到令牌的话,则直接提示服务降级处理
    long timeout();
}
