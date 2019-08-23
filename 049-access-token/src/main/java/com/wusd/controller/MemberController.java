package com.wusd.controller;

import com.wusd.base.BaseApiService;
import com.wusd.base.ResponseBase;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/openApi")
public class MemberController extends BaseApiService {
    @RequestMapping("/getUser")
    public ResponseBase getUser() {
        return setResultSuccess("获取会员信息接口");
    }
}
