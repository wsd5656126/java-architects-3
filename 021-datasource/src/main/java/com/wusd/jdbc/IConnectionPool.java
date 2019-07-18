package com.wusd.jdbc;

import java.sql.Connection;

public interface IConnectionPool {
    Connection getConnection();
    void releaseConnection(Connection connection);
}
