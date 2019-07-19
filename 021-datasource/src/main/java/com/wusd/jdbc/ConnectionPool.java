package com.wusd.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ConnectionPool implements IConnectionPool {
    private List<Connection> freeConnections = new Vector<>();
    private List<Connection> activeConnections = new Vector<>();
    private DbBean dbBean;
    private int connCnt = 0;

    public ConnectionPool(DbBean dbBean) {
        this.dbBean = dbBean;
        init();
    }

    private void init()  {
        if (dbBean == null) {
            return;
        }
        int initConnections = dbBean.getInitConnections();
        for (int i = 0; i < initConnections; i++) {
            Connection connection = newConnection();
            if (connection != null)
                freeConnections.add(connection);
        }
    }

    private Connection newConnection() {
        try {
            Class.forName(dbBean.getDriverName());
            Connection connection = DriverManager.getConnection(dbBean.getUrl(), dbBean.getUsername(),
                    dbBean.getPassword());
            return connection;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public synchronized Connection getConnection() {
        Connection connection;
        try {
            int maxActiveConnections = dbBean.getMaxActiveConnections();
            if (connCnt < maxActiveConnections) {
                if (freeConnections.size() > 0) {
                    connection = freeConnections.remove(0);
                } else {
                    connection = newConnection();
                }
                boolean available = isAvailable(connection);
                if (available) {
                    activeConnections.add(connection);
                    connCnt++;
                } else {
                    connCnt--;
                    connection = getConnection();//重试
                }
            } else {
                wait(dbBean.getConnTimeOut());
                connection = getConnection();
            }
            return connection;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isAvailable(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        try {
            boolean available = isAvailable(connection);
            if (available) {
                if (freeConnections.size() < dbBean.getMaxConnections()) {
                    freeConnections.add(connection);
                } else {
                    connection.close();
                }
            }
            activeConnections.remove(connection);
            connCnt--;
            notifyAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
