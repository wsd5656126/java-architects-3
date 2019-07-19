package com.wusd.core;

import com.wusd.orm.annotation.ExtService;
import com.wusd.util.ClassUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathXmlApplicationContext {
    private String packageName;
    private ConcurrentHashMap<String, Object> beanMap;

    public ClassPathXmlApplicationContext(String packageName) {
        this.packageName = packageName;
    }

    public Object getBean(String beanId) throws Exception {
        //找到含有ExtService的所有类
        List<Class<?>> extServiceClazzes = findExtServiceClazzes();
        //初始化beanMap
        beanMap = initBean(extServiceClazzes);
        //从beanMap中获取对象
        Object obj = beanMap.get(beanId);
        //依赖注入
        attributeObject(obj);
        return obj;
    }

    public List<Class<?>> findExtServiceClazzes() throws Exception {
        List<Class<?>> extServiceClazzes = new ArrayList<>();
        if (StringUtils.isEmpty(packageName)) {
            throw new Exception("包路径不能为空");
        }
        //找到包路径下所有类
        List<Class<?>> clazzesByPackage = ClassUtils.getClazzesByPackage(packageName);
        for (Class<?> clazz : clazzesByPackage) {
            ExtService extService = clazz.getDeclaredAnnotation(ExtService.class);
            if (extService != null) {
                extServiceClazzes.add(clazz);
            }
        }
        return extServiceClazzes;
    }

    public ConcurrentHashMap<String, Object> initBean(List<Class<?>> clazzes) throws Exception {
        if (CollectionUtils.isEmpty(clazzes)) {
            throw new Exception("没有初始化的bean");
        }
        ConcurrentHashMap<String, Object> objectMap = new ConcurrentHashMap<>();
        for (Class<?> clazz : clazzes) {
            Object newInstance = clazz.newInstance();
            String beanId = firstLetterToLowerCase(clazz.getSimpleName());
            objectMap.put(beanId, newInstance);
        }

        return objectMap;
    }

    public void attributeObject(Object obj) throws IllegalAccessException {
        Class<?> objClass = obj.getClass();
        Field[] declaredFields = objClass.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object fieldRef = beanMap.get(fieldName);
            field.set(obj, fieldRef);
        }
    }

    public String firstLetterToLowerCase(String name) {
        if (StringUtils.isNotEmpty(name)) {
            name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        }
        return name;
    }
}
