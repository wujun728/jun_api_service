package com.websql.service;

import com.websql.model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DbSourceService
 * @Description 数据源接口
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 17:33
 **/
public interface DbSourceService {

    List<DataSourceModel> reloadDataSourceList();

    Result<DataSourceModel> selectDbSourceList(DataSourceModel model);

    void addDbSource(DataSourceModel model,Long id);

    List<Map<String, String>> dbsourceSqlList(DataSourceModel model);

    void saveSqlText(DbSqlText model);

    List<Map<String, String>> sqlTextList(DataSourceModel model);

    Result<DbSqlText> getDbSqlText(DbSqlText model);

    void deleteDataBaseSource(Long id);

    SysSetup initSysSetup();

    void deleteSqlText(String id);

    Result<SysLog> getLogList(SysLog model);

    void delSysLog();

    void delUserLog();

    Integer selectDbByName(String dbName);

    AjaxResult findTableField(String database);

    void updateDataSourceName(Long id, String name) throws SQLException;

    AjaxResult findMetaTable(String database, String table);

    AjaxResult showTableSql(String database, String table);

    void sqlTextDeleteAll();

    AjaxResult executeSqlNew(ExecuteSql sql);

    DataSourceModel selectDbById(Long id);

    List<DbSqlText> sqlTextListAll();

    void deleteDataSourceAll();

    Map<String, Object> createAsyncExport(ExecuteSql executeSql);

    SysExportModel exportAsyncData(Long id);

    Result<SysExportModel> exportFilesLogList(SysExportModel model);

    List<MetaTreeTable> metaTreeTableList();

}
