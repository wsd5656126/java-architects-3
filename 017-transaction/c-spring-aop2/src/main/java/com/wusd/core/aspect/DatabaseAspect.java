package com.wusd.core.aspect;

import com.wusd.core.util.TransactionalUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Component
@Aspect
public class DatabaseAspect {
    @Autowired
    TransactionalUtils transactionalUtils;
    //事务回滚
    @AfterThrowing("execution(* com.wusd.service.impl.*.*(..))")
    public void afterThrowing() {
        System.out.println("事务回滚");
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }
    //事务提交
//    @Around("execution(* com.wusd.service.UserService.add(..))")
    @Around("execution(* com.wusd.service.impl.*.*(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("开启事务");
        TransactionStatus transactionStatus = transactionalUtils.begin();
        joinPoint.proceed();
        System.out.println("事务提交");
        transactionalUtils.commit(transactionStatus);
    }
}
