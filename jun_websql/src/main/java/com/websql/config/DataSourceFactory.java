package com.websql.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.stat.DruidDataSourceStatManager;
import com.alibaba.druid.util.JdbcUtils;
import com.websql.model.DataSourceModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DataSourceFactory
 * @Description 数据源管理
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 16:29
 **/
@Slf4j
public class DataSourceFactory {

    private static final ConcurrentHashMap<String, DruidDataSource> DATA_SOURCE_MAP = new ConcurrentHashMap<String, DruidDataSource>(1);

    /**
     * 根据jdbc参数获得数据源连接池dbcp，并放入ConcurrentHashMap
     */
    @Primary
    public static void initDataSource(List<DataSourceModel> dataSourceModels) {
        DruidDataSource ds = null;
        int index = 0;
        for (DataSourceModel config : dataSourceModels) {
            removeDataSource(config.getDbName());
            ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbAccount().trim());
            ds.setPassword(config.getDbPassword().trim());
            ds.setUrl(config.getDbUrl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery(config.getDbCheckUrl());
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            ds.setNotFullTimeoutRetryCount(-1);
            try {
                //druid不支持某些数据库防火墙功能
                DbType dbTypeRaw = getDbTypeByJdbcUrl(config.getDbUrl().trim(), config.getDriverClass().trim());
                if (dbTypeRaw.equals(DbType.dm)
                        || dbTypeRaw.equals(DbType.kingbase)
                        || dbTypeRaw.equals(DbType.oscar)
                        || dbTypeRaw.equals(DbType.odps)
                        || dbTypeRaw.equals(DbType.clickhouse)) {
                    //ds.setFilters("stat");
                } else {
                    ds.setFilters("stat,wall");
                }
                ds.setConnectionProperties("druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000");
                ds.init();
                DATA_SOURCE_MAP.put(config.getDbName().trim(), ds);
                index++;
            } catch (SQLException e) {
                log.error(config.getDbName() + "数据源初始化失败：" + e.getMessage());
                DruidDataSourceStatManager.removeDataSource(ds);
            }
        }
        log.info("Successful initialization  " + index + " DbSources ");
    }


    /**
     * 插入数据源连接池到缓存
     */
    public static void saveDataSource(DataSourceModel config) throws SQLException {
        if (config != null) {
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbAccount().trim());
            ds.setPassword(config.getDbPassword().trim());
            ds.setUrl(config.getDbUrl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery(config.getDbCheckUrl());
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            ds.setFailFast(true);
            try {
                ds.init();
                DATA_SOURCE_MAP.put(config.getDbName().trim(), ds);
            } catch (Exception e) {
                DruidDataSourceStatManager.removeDataSource(ds);
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * 从缓存里删除指定的数据源连接池
     */
    public static void removeDataSource(String sourceKey) {
        DruidDataSource ds = DATA_SOURCE_MAP.get(sourceKey.trim());
        if (ds == null) {
            return;
        }
        //shutdownDataSource(ds);
        DATA_SOURCE_MAP.remove(sourceKey.trim());
        DruidDataSourceStatManager.removeDataSource(ds);
        log.info(" Delete DataSource {} Successful.", sourceKey);
    }


    /**
     * 修改数据源连接池到缓存,未实现修改方法
     */

    public static void updateDataSource(DataSourceModel config) throws Exception {
        if (config != null) {
            //先将原有数据源连接池从缓存中删除
            removeDataSource(config.getDbName());
            DruidDataSource ds = new DruidDataSource();
            ds.setDriverClassName(config.getDriverClass().trim());
            ds.setUsername(config.getDbAccount().trim());
            ds.setPassword(config.getDbPassword().trim());
            ds.setUrl(config.getDbUrl().trim());
            ds.setInitialSize(config.getInitialSize());
            ds.setMaxActive(config.getMaxActive());
            ds.setMaxWait(config.getMaxWait());
            ds.setValidationQuery("select 1");
            ds.setConnectionErrorRetryAttempts(1);
            ds.setNotFullTimeoutRetryCount(-1);
            ds.setBreakAfterAcquireFailure(true);
            ds.setConnectionErrorRetryAttempts(0);
            DATA_SOURCE_MAP.put(config.getDbName().trim(), ds);
        }
        log.info("Successful Update  DbSources ");
    }


    /**
     * 获得连接池
     */

    public static DruidDataSource getDataSource(String sourceKey) {
        return DATA_SOURCE_MAP.get(sourceKey.trim());
    }

    /**
     * 获取连接
     *
     * @param source
     * @return
     */
    public static Connection getConnection(DruidDataSource source) {
        Connection con = null;
        if (source != null) {
            try {
                con = source.getConnection();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            return con;

        }
        return con;
    }

    protected static void shutdownDataSource(DruidDataSource source) {
        if (source != null) {
            source.close();
        }
    }


    public static String getDbType(String sourceKey) {
        DruidDataSource druidDataSource = DATA_SOURCE_MAP.get(sourceKey);
        if (ObjectUtil.isNotNull(druidDataSource)) {
            return getDbTypeByJdbcUrl(druidDataSource.getRawJdbcUrl(), "").name();
        }
        return null;
    }

    public static DbType getDbTypeByJdbcUrl(String jdbcUrl, String className) {
        DbType dbType = JdbcUtils.getDbTypeRaw(jdbcUrl, className);
        if (ObjectUtil.isNotEmpty(dbType)) {
            return dbType;
        }
        //druid对神通缺失判断
        String jdbcOscar = "jdbc:oscar:";
        if (jdbcUrl.startsWith(jdbcOscar)) {
            return DbType.oscar;
        }
        throw new RuntimeException("根据url[" + jdbcUrl + "]无法找到对应的数据库类型!");
    }

    /**
     * 获取数据库名称
     *
     * @param sourceKey
     * @return
     */
    public static String getDataBaseName(String sourceKey) {
        try {
            DruidDataSource dataSource = getDataSource(sourceKey);
            return dataSource.getConnection().getCatalog();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
