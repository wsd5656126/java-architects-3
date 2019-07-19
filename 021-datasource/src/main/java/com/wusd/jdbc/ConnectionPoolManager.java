package com.wusd.jdbc;

import java.sql.Connection;

public class ConnectionPoolManager {
    private static DbBean dbBean = new DbBean();
    private static ConnectionPool connectionPool = new ConnectionPool(dbBean);
    public static Connection getConnection() {
        return connectionPool.getConnection();
    }
    public static void releaseConnection(Connection connection) {
        connectionPool.releaseConnection(connection);
    }
}
