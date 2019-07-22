package com.wusd.orm.util;

import java.sql.*;
import java.util.List;

public class JDBCUtils {
    private static String connect;
    private static String driverClassName;
    private static String URL;
    private static String username;
    private static String password;
    private static boolean autoCommit;

    /**
     * 声明一个Connection类型的静态属性,用来缓存一个已经存在的连接对象
     */
    private static Connection conn;

    static {
        config();
    }

    /**
     * 开头配置自己的数据库信息
     */
    private static void config() {
        //获取驱动
        driverClassName = "com.mysql.jdbc.Driver";
        //获取URL
        URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncode=utf-8";
        //获取用户名
        username = "root";
        //获取密码
        password = "wusd123..";
        //设置是否自动提交,一般为false不用改
        autoCommit = false;
    }

    /**
     * 载入数据库驱动类
     * @return
     */
    private static boolean load() {
        try {
            Class.forName(driverClassName);
            return true;
        } catch (ClassNotFoundException e) {
            System.err.println("驱动类 " + driverClassName + " 加载失败");
        }
        return false;
    }

    /**
     * 专门检查缓存的连接是否不可以被使用,不可以被使用,返回true
     * @return
     */
    private static boolean invalid() {
        if (conn != null) {
            try {
                if (conn.isClosed() || !conn.isValid(3)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }

    /**
     * 建立数据库连接
     * @return
     */
    public static Connection connect() {
        if (invalid()) {
            load();
            try {
                conn = DriverManager.getConnection(URL, username, password);
            } catch (Exception e) {
                System.err.println("建立 " + connect + " 数据库连接失败, " + e.getMessage());
            }
        }
        return conn;
    }

    /**
     * 设置事务是否自动提交
     */
    public static void transaction() {
        try {
            conn.setAutoCommit(autoCommit);
        } catch (Exception e) {
            System.out.println("设置事务的提交方式为: " + (autoCommit ? "自动提交" : "手动提交") + " 时失败"
                    + e.getMessage());
        }
    }

    /**
     * 创建statement对象
     * @return
     */
    public static Statement statement() {
        Statement st = null;
        connect();
        transaction();
        try {
            st = conn.createStatement();
        } catch (Exception e) {
            System.err.println("创建statement对象失败: " + e.getMessage());
        }
        return st;
    }

    /**
     * 创建preparedStatement对象
     * @param SQL
     * @param autoGeneratedKeys
     * @return
     */
    private static PreparedStatement prepare(String SQL, boolean autoGeneratedKeys) {
        PreparedStatement ps = null;
        connect();
        transaction();
        try {
            if (autoGeneratedKeys) {
                ps = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(SQL);
            }
        } catch (Exception e) {
            System.out.println("创建 PreparedStatement 对象失败: " + e.getMessage());
        }
        return ps;
    }

    /**
     * 查询,返回ResultSet
     * @param SQL
     * @param params
     * @return
     */
    public static ResultSet query(String SQL, List<Object> params) {
        if (SQL == null || SQL.trim().isEmpty() || !SQL.trim().toLowerCase().startsWith("select")) {
            throw new RuntimeException("你的SQL语句为空或不适查询语句");
        }
        ResultSet rs = null;
        if (params.size() > 0) {
            PreparedStatement ps = prepare(SQL, false);
            try {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
                rs = ps.executeQuery();
            } catch (Exception e) {
                System.out.println("执行SQL失败:" + e.getMessage());
            }
        } else {
            Statement st = statement();
            try {
                rs = st.executeQuery(SQL);
            } catch (Exception e) {
                System.out.println("执行SQL失败:" + e.getMessage());
            }
        }

        return rs;
    }

    /**
     * 数据对象转型
     * @param o
     * @return
     */
    private static Object typeof(Object o) {
        Object r = o;
        if (o instanceof java.sql.Timestamp) {
            return r;
        }
        if (o instanceof java.util.Date) {
            java.util.Date d = (java.util.Date) o;
            r = new java.sql.Date(d.getTime());
            return r;
        }
        if (o instanceof Character || o.getClass() == char.class) {
            r = String.valueOf(o);
            return r;
        }
        return r;
    }

    /**
     * 执行语句
     * @param SQL
     * @param params
     * @return
     */
    public static boolean execute(String SQL, Object... params) {
        if (SQL == null || SQL.trim().isEmpty() || SQL.trim().startsWith("select")) {
            throw new RuntimeException("你的SQL语句为空或有错");
        }
        boolean r = false;
        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        String prefix = SQL.substring(0, SQL.indexOf(" "));
        String operation = "";
        switch (prefix) {
            case "create":
                operation = "create table";
                break;
            case "alter":
                operation = "update table";
                break;
            case "drop":
                operation = "drop table";
                break;
            case "truncate":
                operation = "truncate table";
                break;
            case "insert":
                operation = "insert :";
                break;
            case "update":
                operation = "update :";
                break;
            case "delete":
                operation = "delete :";
                break;
        }
        if (params.length > 0) {
            PreparedStatement ps = prepare(SQL, false);
            Connection c = null;
            try {
                c = ps.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.length; i++) {
                    Object p = params[i];
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                ps.executeUpdate();
                commit(c);
                r = true;
            } catch (SQLException e) {
                System.out.println(operation + " 失败: " + e.getMessage());
                rollback(c);
            }
        }
        return r;
    }

    /**
     * 数据库插入
     * @param SQL
     * @param autoGeneratedKeys
     * @param params
     * @return
     */
    public static int insert(String SQL, boolean autoGeneratedKeys, List<Object> params) {
        int var = -1;
        if (SQL == null || SQL.trim().isEmpty()) {
            throw new RuntimeException("你没有指定SQL语句,请检查是否指定了需要执行的SQL语句");
        }
        if (!SQL.trim().toLowerCase().startsWith("insert")) {
            System.out.println(SQL.toLowerCase());
            throw new RuntimeException("你指定的SQL语句不是插入语句,请检查你的SQL语句");
        }
        SQL = SQL.trim();
        SQL = SQL.toLowerCase();
        if (params.size() > 0) {
            PreparedStatement ps = prepare(SQL, autoGeneratedKeys);
            Connection c = null;
            try {
                c = ps.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                for (int i = 0; i < params.size(); i++) {
                    Object p = params.get(i);
                    p = typeof(p);
                    ps.setObject(i + 1, p);
                }
                int count = ps.executeUpdate();
                if (autoGeneratedKeys) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        var = rs.getInt(1);
                    }
                } else {
                    var = count;
                }
                commit(c);
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c);
            }
        } else {
            Statement st = statement();
            Connection c = null;
            try {
                c = st.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                int count = st.executeUpdate(SQL);
                if (autoGeneratedKeys) {
                    ResultSet rs = st.getGeneratedKeys();
                    if (rs.next()) {
                        var = rs.getInt(1);
                    }
                } else {
                    var = count;
                }
                commit(c);
            } catch (SQLException e) {
                System.out.println("数据保存失败: " + e.getMessage());
                rollback(c);
            }
        }
        return var;
    }

    /**
     * 提交事务
     * @param c
     */
    private static void commit(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void rollback(Connection c) {
        if (c != null && !autoCommit) {
            try {
                c.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源,resultSet,Statement,preparedStatement,connection
     * @param closeable
     */
    public static void release(Object closeable) {
        if (closeable != null) {
            if (closeable instanceof ResultSet) {
                ResultSet rs = (ResultSet) closeable;
                try {
                    rs.close();;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (closeable instanceof Statement) {
                Statement st = (Statement) closeable;
                try {
                   st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (closeable instanceof Connection) {
                Connection c = (Connection) closeable;
                try {
                    c.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

