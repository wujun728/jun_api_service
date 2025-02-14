package com.websql.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.PagerUtils;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.websql.model.SqlParserVo;
import com.websql.model.SysSetup;
import com.websql.util.CacheUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @ClassName : SqlParserHandler
 * @Description : sql语法解析
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/10/31 11:38
 */
public class SqlParserHandler {

    private static final Logger logger = LoggerFactory.getLogger(SqlParserHandler.class);

    public static final String SELECT = "SELECT";
    public static final String INSERT = "INSERT";
    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";


    public static List<SqlParserVo> getParserVo(String databaseKey, String sql) {
        DbType dbType = DbType.valueOf(DataSourceFactory.getDbType(databaseKey));
        List<SqlParserVo> resultList = new ArrayList<>();
        List<SQLStatement> statements = SQLUtils.parseStatements(sql, dbType);
        for (SQLStatement statement : statements) {
            SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            statement.accept(visitor);

            String methodType = getExecuteType(visitor.getTables(), statement);
            List<String> riskMethod = checkRiskMethod(methodType);
            if (ObjectUtil.isNotNull(riskMethod) && !riskMethod.isEmpty()) {
                throw new RuntimeException("不允许执行【" + riskMethod + "】SQL语句,请联系管理员!");
            }
            List<String> tableNameList = getTableName(visitor.getTables());
            List<String> tableColumns = getTableColumns(visitor.getColumns());

            SqlParserVo sqlParserVo = new SqlParserVo();
            sqlParserVo.setMethodType(methodType);
            sqlParserVo.setTableNameList(tableNameList);
            sqlParserVo.setTableColumns(tableColumns);
            if ("SELECT".equals(methodType)) {
                sqlParserVo.setSqlContent(pageLimitSql(statement));
            } else {
                String formatSql = SQLUtils.toSQLString(statement, statement.getDbType());
                sqlParserVo.setSqlContent(formatSql);
            }
            resultList.add(sqlParserVo);
        }
        return resultList;
    }

    /**
     * 获取操作类型
     *
     * @param tables    解析出来的表名
     * @param statement 节点
     * @return
     */
    private static String getExecuteType(Map tables, SQLStatement statement) {
        if (ObjectUtil.isNotNull(tables) && !tables.isEmpty()) {
            for (Object value : tables.values()) {
                if (ObjectUtil.isNotNull(value) && !value.toString().isEmpty()) {
                    return value.toString().toUpperCase();
                }
            }
        }
        if (ObjectUtil.isNull(statement)) {
            logger.error("解析语法错误，没有找到对应的执行类型!");
            return null;
        }
        String name = statement.getClass().getName();
        if (name.contains("Insert")) {
            return INSERT;
        } else if (name.contains("Update")) {
            return UPDATE;
        } else if (name.contains("Delete")) {
            return DELETE;
        } else if (name.contains("Create")) {
            return UPDATE;
        } else {
            return SELECT;
        }
    }


    /**
     * 默认查询分页限制
     *
     * @param sqlStatement
     * @return
     */
    private static String pageLimitSql(SQLStatement sqlStatement) {
        Pattern pattern = Pattern.compile("\\b(COUNT|SUM|AVG|MIN|MAX|GROUP)\\b", Pattern.CASE_INSENSITIVE);
        String sql = SQLUtils.toSQLString(sqlStatement, sqlStatement.getDbType());
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
            if (sqlSelectStatement.getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectStatement.getSelect().getQuery();
                // 聚合函数及分组不增加默认分页
                for (SQLSelectItem sqlSelectItem : sqlSelectQueryBlock.getSelectList()) {
                    String method = sqlSelectItem.getExpr().toString();
                    if (pattern.matcher(method).find()) {
                        return sql;
                    }
                }
                if (ObjectUtil.isNotNull(sqlSelectQueryBlock.getGroupBy())) {
                    return sql;
                }
                if (ObjectUtil.isNull(sqlSelectQueryBlock.getLimit()) && !sql.toUpperCase().contains("ROWNUM")) {
                    try {
                        Integer limitMax = 1000;
                        SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
                        if (ObjectUtil.isNotNull(sysSetup) && ObjectUtil.isNotNull(sysSetup.getPageLimitMax())) {
                            limitMax = sysSetup.getPageLimitMax();
                        }
                        return PagerUtils.limit(sql, getPageType(sqlSelectStatement.getDbType()), 0, limitMax);
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("生成分页拦截SQL失败，不支持{}数据库类型,可以关闭分页拦截继续使用。", sqlSelectStatement.getDbType());
                    }
                }
            }
        }
        return sql;
    }


    /**
     * 国产数据库druid工具类不支持，只能按照类似的数据库类型生成转换
     *
     * @param dbType
     * @return
     */
    private static DbType getPageType(DbType dbType) {
        Map<String, DbType> pageDbType = new HashMap<>();
        pageDbType.put(DbType.dm.name(), DbType.mysql);
        pageDbType.put(DbType.kingbase.name(), DbType.mysql);
        pageDbType.put(DbType.oscar.name(), DbType.mysql);
        if (ObjectUtil.isNotNull(pageDbType.get(dbType.name()))) {
            return pageDbType.get(dbType.name());
        } else {
            return dbType;
        }
    }

    /**
     * 黑名单语法过滤
     *
     * @param executeType
     * @return
     */
    private static List<String> checkRiskMethod(String executeType) {
        SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
        if (ObjectUtil.isNotNull(sysSetup) && ObjectUtil.isNotNull(sysSetup.getRiskText())) {
            List<String> riskList = new ArrayList<>(Arrays.asList(sysSetup.getRiskText().split(",")));
            return riskList.stream().filter(s -> s.toUpperCase().equals(executeType)).collect(Collectors.toList());
        }
        return null;
    }


    /**
     * 获取所有表名
     *
     * @return
     */
    private static List<String> getTableName(Map tables) {
        List<String> tableList = new ArrayList<>(tables.size());
        if (ObjectUtil.isNotNull(tables) && !tables.isEmpty()) {
            for (Object value : tables.keySet()) {
                if (ObjectUtil.isNotNull(value) && !value.toString().isEmpty()) {
                    tableList.add(value.toString());
                }
            }
        }
        return tableList;
    }


    /**
     * 获取所有列名
     *
     * @return
     */
    private static List<String> getTableColumns(Collection<TableStat.Column> columns) {
        List<String> columnList = new ArrayList<>(columns.size());
        for (TableStat.Column column : columns) {
            columnList.add(column.getName());
        }
        return columnList;
    }

}
