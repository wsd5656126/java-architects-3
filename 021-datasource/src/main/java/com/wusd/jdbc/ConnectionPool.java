package com.wusd.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;

/**
 * 数据库连接池实现原理
 * 核心参数
 * 1.空闲线程 容器 没有被使用的连接存放 2.活动线程 容器正在使用的连接
 * 核心步骤
 * 2.1.初始化线程池(初始化空闲线程)
 * 3.1.调用getConnection方法 --- 获取连接
 *  3.1.1.先去freeConnection获取当前连接,存放在activeConnection
 * 4.1.调用releaseConnection方法 ---释放连接---资源回收
 *  4.1.1.获取activeConnection集合连接,转移到freeConnection集合中
 */
public class ConnectionPool implements IConnectionPool {
    //使用线程安全的集合 空闲线程 容器 没有被使用的连接存放
    private List<Connection> freeConnection = new Vector<>();
    //使用线程安全的集合 活动线程 容器 容器正在使用的线程
    private List<Connection> activeConnection = new Vector<>();
    private DbBean dbBean;
    private int countConnection = 0;

    public ConnectionPool(DbBean dbBean) {
        //获取配置信息
        this.dbBean = dbBean;
        init();
    }

    //初始化线程池(初始化空闲线程)
    private void init() {
        if (dbBean == null) {
            //注意:最好抛出异常
            return;
        }
        //获取初始化连接数
        for (int i = 0; i < dbBean.getInitConnections(); i++) {
            //创建connection连接
            Connection newConnection = newConnection();
            if (newConnection != null) {
                //存放在freeConnection集合
                freeConnection.add(newConnection);
            }
        }
    }

    //创建connection连接
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
    //调用getConnection方法---获取连接
    @Override
    public Connection getConnection() {
        try {
            Connection connection = null;
            //思考:这么知道当前创建的连接>最大连接数
            if (countConnection < dbBean.getMaxActiveConnections()) {//小于最大活动连接数
                //判断空闲线程是否有数据
                if (freeConnection.size() > 0) {
                    //空闲线程有存在连接
                    //拿到再删除
                    connection = freeConnection.remove(0);
                } else {
                    //创建新的连接
                    connection = newConnection();
                }
                //判断连接是否可用
                boolean available = isAvailable(connection);
                if (available) {
                    //存放再活动线程池
                    activeConnection.add(connection);
                    countConnection++;
                } else {
                    countConnection--;
                    connection = getConnection();//怎么使用重试?递归算法
                }
            } else {
                //大于最大活动连接数,进行等待
                wait(dbBean.getConnTimeOut());
                //重试
                connection = getConnection();
            }
            return connection;
        } catch (Exception e) {

        }
        return null;
    }

    //判断连接是否可用
    public boolean isAvailable(Connection connection) {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    //释放连接 回收
    @Override
    public synchronized void releaseConnection(Connection connection) {
        try {
            //判断连接是否可用
            if (isAvailable(connection)) {
                //判断空闲线程是否已满
                if (freeConnection.size() < dbBean.getMaxConnections()) {
                    //空闲线程没有满
                    freeConnection.add(connection);//回收连接
                } else {
                    //空闲线程已满
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
