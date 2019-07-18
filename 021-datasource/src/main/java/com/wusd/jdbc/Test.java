package com.wusd.jdbc;

import java.sql.Connection;

public class Test {
    public static void main(String[] args) {
        ThreadConnection threadConnection = new ThreadConnection();
        for (int i = 1; i < 3; i++) {
            Thread thread = new Thread(threadConnection, "线程i:" + i);
            thread.start();
        }
    }
}
class ThreadConnection implements Runnable {

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            Connection connection = ConnectionPoolManager.getConnection();
            System.out.println(Thread.currentThread().getName() + ",connection:" + connection);
            ConnectionPoolManager.releaseConnection(connection);
        }
    }
}
