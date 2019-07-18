package com.wusd.mvc.core;

import com.wusd.mvc.annotation.ExtController;
import com.wusd.mvc.annotation.ExtRequestMapping;
import com.wusd.util.ClassUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义前端控制器
 * 手写springmvc原理分析
 * 1.创建一个DispatchServlet拦截所有请求
 * 2.初始化奥做 重写servlet init方法
 * 2.1.将扫包范围所有的类,注入到springmvc容器里面,存放在Map集合中.key为默认类名小写,value对象
 * 2.2.将url映射和方法进行关联
 * 2.2.1.判断类上是否有注解,使用java反射机制循环遍历方法,判断方法上是否存在注解,进行封装url和方法对应存入集合中
 * 3.处理请求 重写get或者是post方法
 * 3.1.获取请求url,从urlBeans集合获取实例对象,获取成功实例对象后,调用urlMethods集合获取方法名称,使用反射机制执行
 */
public class DispatchServlet extends HttpServlet {
    private ConcurrentHashMap<String, Object> controllerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Object> urlControllerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, String> urlMethodMap = new ConcurrentHashMap<>();

    @Override
    public void init() throws ServletException {
        List<Class<?>> clazzes = ClassUtils.getClazzesByPackage("com.wusd.controller");
        try {
            initController(clazzes);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        handlerMapping();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String requestURI = req.getRequestURI();
        Object obj = urlControllerMap.get(requestURI);
        if (obj == null) {
            resp.getWriter().println("404 not found controller");
            return;
        }
        String methodName = urlMethodMap.get(requestURI);
        if (methodName == null) {
            resp.getWriter().println("404 not found methodName");
            return;
        }
        String resultPage = (String) methodInvoke(obj, methodName);
        String jspUrl = "/" + resultPage + ".jsp";
        req.getRequestDispatcher(jspUrl).forward(req, resp);
    }

    private Object methodInvoke(Object obj, String methodName) {
        Class<?> objClass = obj.getClass();
        try {
            Method method = objClass.getDeclaredMethod(methodName);
            Object result = method.invoke(obj);
            return result;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void initController(List<Class<?>> clazzes) throws IllegalAccessException, InstantiationException {
        for (Class clazz : clazzes) {
            ExtController extController = (ExtController) clazz.getDeclaredAnnotation(ExtController.class);
            if (extController != null) {
                Object newInstance = clazz.newInstance();
                String simpleName = clazz.getSimpleName();
                String beanId = ClassUtils.firstLetterToLowerCase(simpleName);
                controllerMap.put(beanId, newInstance);
            }
        }
    }

    private void handlerMapping() {
        Set<Map.Entry<String, Object>> entries = controllerMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            Object obj = entry.getValue();
            Class<?> objClass = obj.getClass();
            ExtRequestMapping baseMapping = objClass.getDeclaredAnnotation(ExtRequestMapping.class);
            String baseUrl = baseMapping.value();
            Method[] methods = objClass.getDeclaredMethods();
            for (Method method : methods) {
                ExtRequestMapping subMapping = method.getDeclaredAnnotation(ExtRequestMapping.class);
                String methodName = method.getName();
                String url = baseUrl + subMapping.value();
                urlControllerMap.put(url, obj);
                urlMethodMap.put(url, methodName);
            }
        }
    }
}
