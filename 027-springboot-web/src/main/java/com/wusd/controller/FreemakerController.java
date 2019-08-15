package com.wusd.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/freemaker")
public class FreemakerController {
    @RequestMapping("/test")
    public String test(Map<String, Object> map) {
        map.put("name", "我的李洁");
        return "index";
    }
    @RequestMapping("/listTest")
    public String index(Map<String, Object> result) {
        result.put("name", "我的李洁");
        result.put("sex", 1);
        List<String> listResult = new ArrayList<>();
        listResult.add("I");
        listResult.add("love");
        listResult.add("lij");
        result.put("listResult", listResult);
        return "listIndex";
    }
}
