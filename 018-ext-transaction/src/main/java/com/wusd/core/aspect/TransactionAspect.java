package com.wusd.core.aspect;

import com.wusd.annotation.ExtTransaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.wusd.core.util.TransactionUtils;
import org.springframework.transaction.TransactionStatus;

import java.lang.reflect.Method;

@Aspect
@Component
public class TransactionAspect {
    @Autowired
    TransactionUtils transactionUtils;

    @AfterThrowing("execution(* com.wusd.service.*.*(..))")
    public void afterThrowing() {
        transactionUtils.rollback();
    }

    @Around("execution(* com.wusd.service.*.*(..))")
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        ExtTransaction extTransaction = getExtTransaction(joinPoint);
        TransactionStatus transactionStatus = begin(extTransaction);
        joinPoint.proceed();
        commit(transactionStatus);
    }

    private ExtTransaction getExtTransaction(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        String methodName = joinPoint.getSignature().getName();
        Class<?> clazz = joinPoint.getTarget().getClass();
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        Method method = clazz.getMethod(methodName, parameterTypes);
        ExtTransaction extTransaction = method.getDeclaredAnnotation(ExtTransaction.class);
        return extTransaction;
    }

    private TransactionStatus begin(ExtTransaction extTransaction) {
        if (extTransaction != null) {
            return transactionUtils.begin();
        }
        return null;
    }

    private void commit(TransactionStatus transactionStatus) {
        if (transactionStatus != null) {
            transactionUtils.commit(transactionStatus);
        }

    }

}
