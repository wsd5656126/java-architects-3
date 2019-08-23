package com.wusd.controller;

import com.alibaba.fastjson.JSONObject;
import com.wusd.base.BaseApiService;
import com.wusd.base.ResponseBase;
import com.wusd.entity.App;
import com.wusd.mapper.AppMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/auth")
public class AuthController extends BaseApiService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private int timeToken = 60 * 60 * 2;
    @Autowired
    private AppMapper appMapper;

    @RequestMapping("/getAccessToken")
    public ResponseBase getAccessToken(@RequestBody App app) {
        app = appMapper.findApp(app);
        if (app == null) {
            return setResultError("没有对应机构的认证信息");
        }
        int isFlag = app.getIsFlag();
        if (isFlag == 1) {
            return setResultError("您现在没有对应的权限生产accessToken");
        }
        String oldAccessToken = app.getAccessToken();
        if (StringUtils.isNotEmpty(oldAccessToken))
            redisTemplate.delete(oldAccessToken);
        String newAccessToken = newAccessToken(app.getAppId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accessToken", newAccessToken);
        return setResultSuccessData(jsonObject);
    }

    private String newAccessToken(String appId) {
        String accessToken = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(accessToken, appId, timeToken, TimeUnit.SECONDS);
        appMapper.updateAccessToken(accessToken, appId);
        return accessToken;
    }
}
