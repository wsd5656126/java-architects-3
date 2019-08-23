package com.wusd.aop;

import com.google.common.util.concurrent.RateLimiter;
import com.sun.xml.internal.ws.client.ResponseContextReceiver;
import com.wusd.annotation.ExtRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RateLimiterAspect {
    private Map<String, RateLimiter> rateHashMap = new ConcurrentHashMap<>();

    @Pointcut("execution(public * com.wusd.ctrl.*.*(..))")
    public void rlAop() {
    }

    @Around("rlAop()")
    public Object around(ProceedingJoinPoint joinPoint) throws Exception {
        //1.如果有注解
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        if (method == null) return null;
        //2.获取注解的参数
        ExtRateLimiter extRateLimiter = method.getDeclaredAnnotation(ExtRateLimiter.class);
        //获取requestURI
        String requestURI = getRequest().getRequestURI();
        //3.获取原生的RateLimiter,如果没有获取到则创建
        RateLimiter rateLimiter;
        if (rateHashMap.containsKey(requestURI)) {
            rateLimiter = rateHashMap.get(requestURI);
        } else {
            rateLimiter = RateLimiter.create(extRateLimiter.permitsPerSecond(), extRateLimiter.timeout(), TimeUnit.MILLISECONDS);
        }
        //4.RateLimiter获取令牌,获取到就执行,获取不到则服务降级
        boolean tryAcquire = rateLimiter.tryAcquire(extRateLimiter.timeout(), TimeUnit.MILLISECONDS);
        if (!tryAcquire) {
            fallback();
            return null;
        }
        try {
            Object proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            throw new Exception(throwable);
        }
    }

    public HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return requestAttributes.getRequest();
    }

    public void fallback() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = requestAttributes.getResponse();
        try {
            System.out.println("服务降级,抢不到咯,回家养猪吧");
            response.getWriter().println("服务降级,抢不到咯,回家养猪吧");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
