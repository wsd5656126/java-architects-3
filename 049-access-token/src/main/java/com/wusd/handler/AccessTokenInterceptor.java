package com.wusd.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wusd.base.BaseApiService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AccessTokenInterceptor extends BaseApiService implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse resp, Object handler) throws Exception {
        System.out.println("开始进入请求地址拦截");
        String accessToken = request.getParameter("accessToken");
        if (StringUtils.isEmpty(accessToken)) { // accessToken 不存在
            resultError("accessToken is null", resp);
            return false;
        } else {
            String appId = redisTemplate.opsForValue().get(accessToken);
            if (StringUtils.isEmpty(appId)) { // accessToken失效
                resultError("accessToken is invalid", resp);
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
        //解析视图前
        System.out.println("解析视图前");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        //解析视图后
        System.out.println("解析视图后");
    }

    public void resultError(String errorMsg, HttpServletResponse resp) throws IOException {
        PrintWriter printWriter = resp.getWriter();
        printWriter.write(new JSONObject().toJSONString(setResultError(errorMsg)));
    }
}
