package com.wusd.orm.aop;

import com.wusd.orm.annotation.Insert;
import com.wusd.orm.annotation.Param;
import com.wusd.orm.annotation.Select;
import com.wusd.orm.util.JDBCUtils;
import com.wusd.orm.util.SQLUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrmInvocationHandler implements InvocationHandler {
    private Object target;
    public OrmInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("使用动态代理技术拦截接口方法开始");
        Insert insert = method.getDeclaredAnnotation(Insert.class);
        if (insert != null) {
            return insert(insert, proxy, method, args);
        }
        Select select = method.getDeclaredAnnotation(Select.class);
        if (select != null) {
            String selectSQL = select.value();
            ConcurrentHashMap<Object, Object> paramsMap = paramsMap(proxy, method, args);
            List<String> sqlSelectParamter = SQLUtils.sqlSelectParamter(selectSQL);
            List<Object> sqlParams = new ArrayList<>();
            for (String parameterName : sqlSelectParamter) {
                Object parameterValue= paramsMap.get(parameterName);
                sqlParams.add(parameterValue);
            }
            String newSql = SQLUtils.paramQuestion(selectSQL, sqlSelectParamter);
            System.out.println("newSql:" + newSql + ",sqlParams:" + sqlParams.toString());

            ResultSet rs = JDBCUtils.query(newSql, sqlParams);
            if (!rs.next()) {
                return null;
            }
            rs.previous();
            Class<?> returnType = method.getReturnType();
            Object obj = returnType.newInstance();
            while (rs.next()) {
                Field[] declaredFields = returnType.getDeclaredFields();
                for (Field field : declaredFields) {
                    String fieldName = field.getName();
                    Object fieldValue = rs.getObject(fieldName);
                    field.setAccessible(true);
                    field.set(obj, fieldValue);
                }
            }
            return obj;
        }
        return null;
    }

    private Object insert(Insert insert, Object proxy, Method method, Object[] args) {
        String insertSql = insert.value();
        ConcurrentHashMap<Object, Object> paramsMap = paramsMap(proxy, method, args);
        String[] sqlInsertParameter = SQLUtils.sqlInsertParameter(insertSql);
        List<Object> sqlParams = sqlParams(sqlInsertParameter, paramsMap);
        String newSQL = SQLUtils.paramQuestion(insertSql, sqlInsertParameter);
        System.out.println("newSQL:" + newSQL + ",sqlParams:" + sqlParams.toString());
        return JDBCUtils.insert(newSQL, false, sqlParams);
    }

    private ConcurrentHashMap<Object, Object> paramsMap(Object proxy, Method method, Object[] args) {
        ConcurrentHashMap<Object, Object> paramsMap = new ConcurrentHashMap<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Param param = parameter.getDeclaredAnnotation(Param.class);
            if (param != null) {
                String paramName = param.value();
                Object paramValue = args[i];
                paramsMap.put(paramName, paramValue);
            }
        }
        return paramsMap;
    }

    private List<Object> sqlParams(String[] sqlInsertParameter, ConcurrentHashMap<Object, Object> paramsMap) {
        List<Object> sqlParams = new ArrayList<>();
        for (String paramName : sqlInsertParameter) {
            Object paramValue = paramsMap.get(paramName);
            sqlParams.add(paramValue);
        }
        return sqlParams;
    }

}
