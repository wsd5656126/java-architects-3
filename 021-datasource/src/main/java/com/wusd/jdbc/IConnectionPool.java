package com.wusd.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

public interface IConnectionPool {
    //获取连接
    Connection getConnection() throws InterruptedException;
    //释放连接
    void releaseConnection(Connection connection) throws SQLException;
}
