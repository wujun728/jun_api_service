package io.github.wujun728.api.common.jdbc.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库连接对象
 */
public class Connector {
    private String dbType = "mysql"; //数据库类型 db2 mysql sqlserver 默认mysql
    private String driver = "com.mysql.cj.jdbc.Driver"; //数据库驱动  com.mysql.cj.jdbc.Driver
    private String url;  //数据库连接地址
    private String schema; //数据库模式  DB2数据库必须
    private String user; //用户名
    private String password; //密码


    /**
     * 数据库类型 DB2
     */
    public static final String DB_TYPE_DB2 = "db2";

    /**
     * 数据库类型 MYSQL
     */
    public static final String DB_TYPE_MYSQL = "mysql";

    /**
     * 数据库类型 SQLSERVER
     */
    public static final String DB_TYPE_SQLSERVER = "sqlserver";

    public static Map<String,String> DRIVER_MAP = new HashMap(){{
        put(DB_TYPE_MYSQL,"com.mysql.cj.jdbc.Driver");
        put(DB_TYPE_SQLSERVER,"com.microsoft.sqlserver.jdbc.SQLServerDriver");
        put(DB_TYPE_DB2,"com.ibm.db2.jcc.DB2Driver");
    }};

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
