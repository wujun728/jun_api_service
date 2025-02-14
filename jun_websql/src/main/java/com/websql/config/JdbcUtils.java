package com.websql.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.websql.model.DataSourceIndexMeta;
import com.websql.model.DataSourceMeta;
import com.websql.model.DataSourceTableMeta;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName JdbcUtils
 * @Description 数据库操作
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 16:31
 **/
@Slf4j
public class JdbcUtils {

    public static Map<String, Object> updateByPreparedStatement(String sourceKey, String sql, List<Object> params) {
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", "1");
        map.put("msg", "执行成功");
        map.put("rawSql", sql);
        Connection connection = getConnections(sourceKey);
        int result = -1;
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            result = pstmt.executeUpdate();
            map.put("data", result);
        } catch (SQLException e) {
            log.error("执行异常," + e.getMessage());
            map.put("code", "2");
            map.put("msg", e.getMessage());
        } finally {
            releaseConn(null, pstmt, connection);
        }
        return map;

    }

    public static Map<String, Object> findMoreResult(String sourceKey, String sql, List<Object> params) {
        Connection connection = getConnections(sourceKey);
        Map<String, Object> map = new HashMap<>(4);
        JSONArray list = new JSONArray();
        map.put("code", "1");
        map.put("msg", "执行成功");
        map.put("rawSql", sql);
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colsLength = metaData.getColumnCount();
            String colsName;
            Object colsValue;
            while (resultSet.next()) {
                //update 2020.06.12 感谢Mr.Guo 提出顺序展示问题
                JSONObject json = new JSONObject(new LinkedHashMap<>(colsLength));
                //新建数组
                Map<String, Integer> temp = new HashMap<>(colsLength);
                for (int i = 0; i < colsLength; i++) {
                    int rowIndex = i + 1;
                    colsName = metaData.getColumnLabel(rowIndex);
                    colsValue = ColumnTypeCastUtil.getDataTypeResult(resultSet, rowIndex, metaData);
                    //查询的sql中的列可能是重复的，处理下列名后面自动追加数字。
                    if (json.containsKey(colsName)) {
                        temp.put(colsName, temp.containsKey(colsName) ? temp.get(colsName) + 1 : 0);
                        json.put(colsName + temp.get(colsName), colsValue);
                    } else {
                        json.put(colsName, colsValue);
                    }
                }
                list.add(json);
            }
            //add 2023.08.12 0条数据，但展示列头字段 用于前端呈现
            if (resultSet.getRow() == 0 && list.isEmpty()) {
                JSONObject json = new JSONObject(new LinkedHashMap<>(colsLength));
                for (int i = 1; i <= colsLength; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    json.put(columnName, "WEB_SQL_PLACEHOLDER");
                }
                list.add(json);
            }
            map.put("data", list);
        } catch (Exception e) {
            log.error("执行异常," + e.getMessage());
            e.printStackTrace();
            map.put("code", "2");
            map.put("msg", e.getMessage());
            map.put("data", "执行失败,无返回结果.");
        } finally {
            releaseConn(resultSet, pstmt, connection);
        }
        return map;

    }


    /**
     * 已知返回一条数据情况使用
     *
     * @param sourceKey
     * @param sql
     * @param params
     * @return
     */
    public static Map<String, Object> findOneResult(String sourceKey, String sql, List<Object> params) {
        Connection connection = getConnections(sourceKey);
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", "1");
        map.put("msg", "执行成功");
        map.put("rawSql", sql);
        int index = 1;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        try {
            pstmt = connection.prepareStatement(sql);
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            int colsLength = metaData.getColumnCount();
            String colsName;
            while (resultSet.next()) {
                for (int i = 0; i < colsLength; i++) {
                    int rowIndex = i + 1;
                    colsName = metaData.getColumnLabel(rowIndex);
                    map.put(colsName, ColumnTypeCastUtil.getDataTypeResult(resultSet, rowIndex, metaData));
                }
            }
            return map;
        } catch (Exception e) {
            log.error("执行异常," + e.getMessage());
            map.put("code", "2");
            map.put("msg", e.getMessage());
            map.put("data", "执行失败,无返回结果.");
        } finally {
            releaseConn(resultSet, pstmt, connection);
        }
        return map;
    }

    /**
     * 执行计划
     *
     * @param sourceKey 数据源
     * @param sql       执行sql
     * @param params    参数
     * @return
     */
    public static Map<String, Object> updateTimers(String sourceKey, String sql, List<Object> params, StringBuffer field) {
        Connection connection = getConnections(sourceKey);
        Map<String, Object> map = new HashMap<>(4);
        map.put("code", "1");
        map.put("msg", "执行成功");
        map.put("rawSql", sql);
        PreparedStatement pstmt = null;
        try {
            pstmt = connection.prepareStatement(sql);
            String[] items = field.toString().split(",");
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    int index = 1;
                    JSONObject item1 = (JSONObject) params.get(i);
                    for (int j = 0; j < items.length; j++) {
                        Object itemObj = item1.get(items[j]);
                        if (itemObj == "") {
                            itemObj = null;
                        }
                        pstmt.setObject(index++, itemObj);
                    }
                    pstmt.addBatch();//添加到批次
                }
                pstmt.executeBatch();
            } else {
                throw new NullPointerException("属性数据为空，无效插入.");
            }
        } catch (Exception e) {
            log.error("执行异常," + e.getMessage());
            map.put("code", "2");
            map.put("msg", e.getMessage());
        } finally {
            releaseConn(null, pstmt, connection);
        }
        return map;

    }

    /**
     * 未使用。 通过count(1)获取记录总数
     */
    public static long getCount(String sourceKey, String sql, List<Object> params) {
        Connection connection = getConnections(sourceKey);
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        long count = 0L;
        try {
            pstmt = connection.prepareStatement(sql);
            int index = 1;
            if (params != null && !params.isEmpty()) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(index++, params.get(i));
                }
            }
            resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        } finally {
            releaseConn(resultSet, pstmt, connection);
        }
        return count;

    }

    public static void releaseConn(ResultSet resultSet, PreparedStatement pstmt, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.error(e.getMessage());
            }
        }
    }

    public static Connection getConnections(String sourceKey) {
        DruidDataSource source = DataSourceFactory.getDataSource(sourceKey);
        if (source == null) {
            throw new NullPointerException(sourceKey + "：未获取到有效数据源");
        }
        Connection connection = DataSourceFactory.getConnection(source);
        if (connection == null) {
            throw new NullPointerException(sourceKey + "：未获取到有效连接");
        }
        return connection;
    }

    /**
     * 获取数据源元数据
     *
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    public static DataSourceMeta getDataSourceMeta(String database, String table) {
        DataSourceMeta dataSourceMeta = new DataSourceMeta();
        Connection connection = JdbcUtils.getConnections(database);
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            dataSourceMeta.setTableName(table);
            dataSourceMeta.setDataSourceKey(database);
            dataSourceMeta.setProductName(dbMetaData.getDatabaseProductName());
            dataSourceMeta.setProductVersion(dbMetaData.getDatabaseProductVersion());
            dataSourceMeta.setDriverName(dbMetaData.getDriverName());
            dataSourceMeta.setDriverVersion(dbMetaData.getDriverVersion());
            dataSourceMeta.setReadOnly(dbMetaData.isReadOnly());
            dataSourceMeta.setSupportsTransactions(dbMetaData.supportsTransactions());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(null, null, connection);
        }
        return dataSourceMeta;
    }

    /**
     * 获取表元数据
     *
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    public static DataSourceMeta getTableMeta(String database, String table) {
        DataSourceMeta dataSourceMeta = null;
        Connection connection = JdbcUtils.getConnections(database);
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            //表描述信息
            ResultSet tables = dbMetaData.getTables(connection.getCatalog(), connection.getSchema(), table, new String[]{"TABLE"});
            while (tables.next()) {
                dataSourceMeta = new DataSourceMeta();
                dataSourceMeta.setDatabaseName(tables.getString(1));
                dataSourceMeta.setTableType(tables.getString("TABLE_TYPE"));
                dataSourceMeta.setTableComment(tables.getString("REMARKS"));
                dataSourceMeta.setTableSchema(tables.getString("TABLE_SCHEM"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(null, null, connection);
        }
        return dataSourceMeta;
    }

    /**
     * 获取列元数据
     *
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    public static List<DataSourceTableMeta> getColumnsMeta(String database, String table) {
        List<DataSourceTableMeta> tableMetas = new ArrayList<>(0);
        Connection connection = JdbcUtils.getConnections(database);
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet columns = dbMetaData.getColumns(connection.getCatalog(), connection.getSchema(), table, null);
            while (columns.next()) {
                DataSourceTableMeta tableMeta = new DataSourceTableMeta();
                tableMeta.setTableName(columns.getString("TABLE_NAME"));
                tableMeta.setColumnName(columns.getString("COLUMN_NAME"));
                tableMeta.setTypeName(columns.getString("TYPE_NAME"));
                tableMeta.setNullable(columns.getString("IS_NULLABLE"));
                tableMeta.setAutoIncrement(columns.getString("IS_AUTOINCREMENT"));
                tableMeta.setComment(columns.getString("REMARKS"));
                tableMetas.add(tableMeta);
            }
            return tableMetas.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(o -> o.getColumnName() + o.getTableName()))), ArrayList::new));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(null, null, connection);
        }
        return tableMetas;
    }

    /**
     * 获取索引元数据
     *
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    public static List<DataSourceIndexMeta> getIndexInfoMeta(String database, String table) {
        List<DataSourceIndexMeta> indexMetas = new ArrayList<>(1);
        Connection connection = JdbcUtils.getConnections(database);
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            ResultSet indexInfo = dbMetaData.getIndexInfo(connection.getCatalog(), connection.getSchema(), table, false, true);
            while (indexInfo.next()) {
                DataSourceIndexMeta indexMeta = new DataSourceIndexMeta();
                indexMeta.setTableName(table);
                indexMeta.setNonUnique(indexInfo.getBoolean("NON_UNIQUE"));
                indexMeta.setIndexName(indexInfo.getString("INDEX_NAME"));
                indexMeta.setColumnName(indexInfo.getString("COLUMN_NAME"));
                indexMetas.add(indexMeta);
            }
            return indexMetas.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(o -> o.getIndexName() + o.getColumnName()))), ArrayList::new));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(null, null, connection);
        }
        return indexMetas;
    }

    /**
     * 获取主键元数据
     *
     * @param database
     * @param table
     * @return
     * @throws SQLException
     */
    public static List<DataSourceTableMeta> getKeyMeta(String database, String table) {
        List<DataSourceTableMeta> keysMetas = new ArrayList<>(1);
        Connection connection = JdbcUtils.getConnections(database);
        try {
            DatabaseMetaData dbMetaData = connection.getMetaData();
            //主键描述
            ResultSet foreignKeys = dbMetaData.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), table);
            while (foreignKeys.next()) {
                DataSourceTableMeta keyMeta = new DataSourceTableMeta();
                keyMeta.setKeySeq(foreignKeys.getInt("KEY_SEQ"));
                keyMeta.setPkName(foreignKeys.getString("PK_NAME"));
                keyMeta.setTableName(foreignKeys.getString("TABLE_NAME"));
                keyMeta.setColumnName(foreignKeys.getString("COLUMN_NAME"));
                keysMetas.add(keyMeta);
            }
            return keysMetas.stream().collect(
                    Collectors.collectingAndThen(Collectors.toCollection(
                            () -> new TreeSet<>(Comparator.comparing(o -> o.getKeySeq() + o.getPkName() + o.getColumnName()))), ArrayList::new));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseConn(null, null, connection);
        }
        return keysMetas;
    }

}

