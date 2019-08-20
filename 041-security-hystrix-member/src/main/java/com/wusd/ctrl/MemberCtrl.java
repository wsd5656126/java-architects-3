package com.wusd.ctrl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberCtrl {
    @RequestMapping("/memberIndex")
    public JSONObject memberIndex() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "lij");
        jsonObject.put("age", 11);
        return jsonObject;
    }
}
