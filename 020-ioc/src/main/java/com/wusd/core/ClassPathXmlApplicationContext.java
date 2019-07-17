package com.wusd.core;

import com.wusd.annotation.ExtService;
import com.wusd.util.ClassUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ClassPathXmlApplicationContext {
    private String packageName;
    private ConcurrentHashMap<String, Object> initBean;

    public ClassPathXmlApplicationContext(String packageName) {
        this.packageName = packageName;
    }

    public void attriAssign(Object obj) throws IllegalAccessException {
        Class<? extends Object> clazz = obj.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();
            Object bean = initBean.get(name);
            if (bean != null) {
                field.setAccessible(true);
                field.set(obj, bean);
                continue;
            }
        }
    }

    public List<Class> findClassExtService() throws Exception {
        if (StringUtils.isEmpty(packageName)) {
            throw new Exception("扫包地址不能为空");
        }

        List<Class<?>> classesByPackageName = ClassUtils.getClasses(packageName);

        List<Class> extClassAnnotation = new ArrayList<>();
        for (Class clazz : classesByPackageName) {
            ExtService extService = (ExtService) clazz.getDeclaredAnnotation(ExtService.class);
            if (extService != null) {
                extClassAnnotation.add(clazz);
                continue;
            }
        }
        return extClassAnnotation;
    }

    public Object getBean(String beanId) throws Exception {
        List<Class> listClassesAnnotation = findClassExtService();
        if (listClassesAnnotation == null || listClassesAnnotation.isEmpty()) {
            throw new Exception("没有需要初始化的bean");
        }
        initBean = initBean(listClassesAnnotation);
        if (initBean == null || initBean.isEmpty()) {
            throw new Exception("初始化bean为空");
        }
        Object object = initBean.get(beanId);
        attriAssign(object);
        return object;
    }

    public ConcurrentHashMap<String, Object> initBean(List<Class> clazzList)
            throws IllegalAccessException, InstantiationException {
        ConcurrentHashMap<String, Object> concurrentHashMap = new ConcurrentHashMap();
        for (Class<?> clazz : clazzList) {
            Object newInstance = clazz.newInstance();
            String beanId = toLowerCaseFirstOne(clazz.getSimpleName());
            concurrentHashMap.put(beanId, newInstance);
        }
        return concurrentHashMap;
    }

    public String toLowerCaseFirstOne(String classSimpleName) {
        if (StringUtils.isNotEmpty(classSimpleName)) {
            classSimpleName = classSimpleName.substring(0, 1).toLowerCase()
                    + classSimpleName.substring(1, classSimpleName.length());
        }
        return classSimpleName;
    }
}
