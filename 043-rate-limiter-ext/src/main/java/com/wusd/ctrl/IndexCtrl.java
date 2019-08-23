package com.wusd.ctrl;

import com.wusd.annotation.ExtRateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexCtrl {
    @ExtRateLimiter(permitsPerSecond = 1.0, timeout = 500)
    @RequestMapping("/findIndex")
    public String findIndex() {
        System.out.println("findIndex" + System.currentTimeMillis());
        return "findIndex" + System.currentTimeMillis();
    }
}
