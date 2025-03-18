package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.TableInfo;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.service.TableService;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql 数据库表基本接口实现类
 * @version 1.0
 * @date 2021-11-03
 **/
public class MySQLTableServiceImpl implements TableService {

    @Override
    public List<TableInfo> selectTables(Connector connector) throws Exception {
        List<TableInfo> tableInfos = new ArrayList<>();
        BaseDao baseDao = new BaseDao(connector);
        Connection con = baseDao.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            if (con == null) {
                System.out.println("数据库连接失败");
                return tableInfos;
            }
            String schema = con.getCatalog();
            DatabaseMetaData dmd = con.getMetaData();
            rs = dmd.getTables(schema, schema, "%", null);
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                String tableComment = rs.getString("REMARKS");
                /*//查询表注释
                String tableComment = this.getTableComment(con, schema, tableName);*/

                TableInfo tableInfo = new TableInfo();
                tableInfo.setTableName(tableName);
                tableInfo.setTableComment(tableComment);
                tableInfos.add(tableInfo);

            }
        } finally {
            baseDao.closeAll(con, pstmt, rs);
        }
        return tableInfos;
    }

    @Override
    public String getTableComment(Connection connection, String schema, String tableName) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "select table_name,table_comment from information_schema.tables " +
                " where table_schema = '" + schema + "' and table_name ='" + tableName + "'";
        pstmt = connection.prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        String comment = "";
        if (result != null && result.next()) {
            comment = result.getString("table_comment");
            if (comment != null && !"".equals(comment)) {
                comment = comment.replaceAll("\r\n","");
                if (comment.substring(comment.length() - 1).equals("表")) {
                    comment = comment.substring(0, comment.length() - 1);
                }
            } else {
                comment = "";
            }
        }
        return comment;
    }
}
