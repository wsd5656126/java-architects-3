package com.wusd.service.impl;

import com.wusd.annotation.ExtService;
import com.wusd.service.LogService;

@ExtService
public class LogServiceImpl implements LogService {
    @Override
    public void add() {
        System.out.println("增加日志");
    }
}
