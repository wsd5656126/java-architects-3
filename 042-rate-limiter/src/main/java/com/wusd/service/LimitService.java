package com.wusd.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LimitService {
    private int limitCnt = 60;
    AtomicInteger atomicInteger = new AtomicInteger(0);
    private long start = System.currentTimeMillis();
    private int interval = 60;

    public boolean acquire() {
        long newTime = System.currentTimeMillis();
        if (newTime > (start + interval)) {
            start = newTime;
            atomicInteger.set(0);
            return true;
        }
        atomicInteger.incrementAndGet();
        return atomicInteger.get() <=limitCnt;
    }

    static LimitService limitService = new LimitService();

    public static void main(String[] args) {
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 1; i < 100; i++) {
            final int tempI = i;
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    if (limitService.acquire()) {
                        System.out.println("你没有被限流,可以正常访问逻辑i:" + tempI);
                    } else {
                        System.out.println("你已经被限流呢 i:" + tempI);
                    }
                }
            });
        }
    }
}
