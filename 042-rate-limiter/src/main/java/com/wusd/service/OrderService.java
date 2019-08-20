package com.wusd.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
    public boolean addOrder() {
        System.out.printf("OrderService.addOrder...");
        return true;
    }
}
