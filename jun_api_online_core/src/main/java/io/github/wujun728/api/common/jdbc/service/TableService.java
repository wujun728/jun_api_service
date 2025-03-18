package io.github.wujun728.api.common.jdbc.service;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.TableInfo;

import java.sql.Connection;
import java.util.List;

/**
 * 数据库表基本接口
 * @version 1.0
 * @date 2021-11-03
 **/
public interface TableService {

    /**
     * 获取所有表
     * @param connector
     * @return
     * @throws Exception
     */
    public List<TableInfo> selectTables(Connector connector) throws Exception;

    /**
     * 获取数据库表注释
     * @param con
     * @param schema
     * @param tableName
     * @return
     * @throws Exception
     */
    public String getTableComment(Connection con, String schema, String tableName) throws Exception;
}
