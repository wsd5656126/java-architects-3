package com.wusd.proxy;

import com.wusd.service.MemberService;
import com.wusd.service.impl.MemberServiceImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//方法的调用在这里执行
public class InvocationHandlerImpl implements InvocationHandler {
    //被代理的对象
    private Object target;
    public InvocationHandlerImpl(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        System.out.println("jdk动态代理 事务开始");
        result = method.invoke(target, args);
        System.out.println("jdk动态代理 事务结束");
        return result;
    }

    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        InvocationHandler invocationHandler = new InvocationHandlerImpl(memberService);

        Class<?> clazz = memberService.getClass();
        ClassLoader classLoader = clazz.getClassLoader();
        Class<?>[] interfaces = clazz.getInterfaces();

        MemberService newProxyInstance = (MemberService) Proxy.newProxyInstance(classLoader, interfaces, invocationHandler);
        newProxyInstance.memberAdd();
    }
}
