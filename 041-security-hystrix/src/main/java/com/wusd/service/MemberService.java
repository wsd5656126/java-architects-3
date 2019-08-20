package com.wusd.service;

import com.alibaba.fastjson.JSONObject;
import com.wusd.util.HttpClientUtils;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    public JSONObject getMember() {
        JSONObject jsonObject = HttpClientUtils.httpGet("http://127.0.0.1:8081/member/memberIndex");
        return jsonObject;
    }
}
