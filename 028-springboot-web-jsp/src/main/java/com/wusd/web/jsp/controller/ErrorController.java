package com.wusd.web.jsp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/error")
public class ErrorController {
    @RequestMapping("/index")
    @ResponseBody
    public String index() {
        return "" + 1 / 0;
    }
}
