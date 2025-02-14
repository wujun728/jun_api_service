package com.websql.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.config.DataSourceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName TableFieldSqlUtils
 * @Description sql组装
 * @Author rabbit boy_0214@sina.com
 * @Date 2023/08/08 14:15
 **/
public class TableFieldSqlUtils {

    /**
     * 查询列名
     */
    private static final Map<String, String> DATA_BASE_TABLE_VIEW_SQL = MapUtil.builder(new HashMap<String, String>(5))
            .put("mysql", " SELECT TABLE_NAME AS TABLE_NAME,COLUMN_NAME TABLE_FIELD from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA= (select database())  ")

            .put("oracle", " SELECT table_name AS TABLE_NAME, column_name AS TABLE_FIELD from user_col_comments ")

            .put("h2", " SELECT TABLE_NAME AS TABLE_NAME,COLUMN_NAME TABLE_FIELD from INFORMATION_SCHEMA.COLUMNS where TABLE_SCHEMA='PUBLIC' ")

            .put("dm", " SELECT TABLE_NAME AS TABLE_NAME,COLUMN_NAME AS TABLE_FIELD from all_tab_columns where owner not in('SYS','SYSDBA','CTISYS') ")

            .put("kingbase", " SELECT table_name AS TABLE_NAME,column_name AS TABLE_FIELD from  INFORMATION_SCHEMA.COLUMNS where table_schema='public' ")

            .put("postgresql", " SELECT table_name AS \"TABLE_NAME\",column_name AS \"TABLE_FIELD\"  from  INFORMATION_SCHEMA.COLUMNS where table_schema='public' ")

            .put("oscar", " SELECT TABLE_NAME AS TABLE_NAME,COLUMN_NAME AS TABLE_FIELD from all_tab_columns where owner not in('SYS','SYSDBA','CTISYS') ")

            .put("clickhouse"," SELECT t.name AS TABLE_NAME,c.name AS TABLE_FIELD FROM system.tables t JOIN system.columns c ON t.database = c.database AND t.name = c.table WHERE t.database = (select database()) ")
            .build();


    //生成默认的insert语句
    private static final Map<String, String> DATA_BASE_TABLE_INSERT_SQL = MapUtil.builder(new HashMap<String, String>(5))
            .put("mysql", " SELECT CONCAT( 'insert into ', table_name, '(', GROUP_CONCAT( COLUMN_NAME ),') value (', GROUP_CONCAT( '#{',COLUMN_NAME,'}' ), ')' ) as insertText FROM information_schema.COLUMNS WHERE table_schema = '{}' AND table_name = '{}'  ")

            .build();

    public static String getViewSql(String database) {
        String dbType = DataSourceFactory.getDbType(database);
        if (ObjectUtil.isEmpty(dbType)) {
            return null;
        }
        return DATA_BASE_TABLE_VIEW_SQL.get(dbType);
    }

    public static String getInertSql(String database) {
        String dbType = DataSourceFactory.getDbType(database);
        if (ObjectUtil.isEmpty(dbType)) {
            return null;
        }
        return DATA_BASE_TABLE_INSERT_SQL.get(dbType);
    }
}
