package com.wusd.jdbc;

import java.beans.beancontext.BeanContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

public class ConnectionPool implements IConnectionPool {
    private List<Connection> freeConnection = new Vector<>();
    private List<Connection> activeConnection = new Vector<>();
    private DbBean dbBean;
    private int countConnection = 0;

    public ConnectionPool(DbBean dbBean) {
        this.dbBean = dbBean;
        init();
    }

    private void init() {
        if (dbBean == null) {
            return;
        }
        for (int i = 0; i < dbBean.getInitConnections(); i++) {
            Connection newConnection = newConnection();
            if (newConnection != null) {
                freeConnection.add(newConnection);
            }
        }
    }

    private synchronized Connection newConnection() {
        try {
            Class.forName(dbBean.getDriverName());
            Connection connection = DriverManager.getConnection(dbBean.getUrl(), dbBean.getUserName(),
                    dbBean.getPassword());
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public Connection getConnection() {
        try {
            Connection connection = null;
            if (countConnection < dbBean.getMaxActiveConnections()) {
                if (freeConnection.size() > 0) {
                    connection = freeConnection.remove(0);
                } else {
                    connection = newConnection();
                }
                boolean available = isAvailable(connection);
                if (available) {
                    activeConnection.add(connection);
                    countConnection++;
                } else {
                    countConnection--;
                    connection = getConnection();
                }
            } else {
                wait(dbBean.getConnTimeOut());
                connection = getConnection();
            }
            return connection;
        } catch (Exception e) {

        }
        return null;
    }

    public boolean isAvailable(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public synchronized void releaseConnection(Connection connection) {
        try {
            if (isAvailable(connection)) {
                if (freeConnection.size() < dbBean.getMaxConnections()) {
                    freeConnection.add(connection);
                } else {
                    connection.close();
                }
                activeConnection.remove(connection);
                countConnection--;
                notifyAll();
            }
        } catch (Exception e) {

        }
    }
}
