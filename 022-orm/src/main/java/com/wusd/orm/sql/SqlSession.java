package com.wusd.orm.sql;

import com.wusd.orm.aop.OrmInvocationHandler;

import java.lang.reflect.Proxy;

public class SqlSession {
    public static <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] {clazz},
                new OrmInvocationHandler(clazz));
    }
}
