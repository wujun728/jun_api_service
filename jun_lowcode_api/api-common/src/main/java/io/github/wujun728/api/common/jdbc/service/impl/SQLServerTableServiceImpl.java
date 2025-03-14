package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.service.TableService;
import io.github.wujun728.api.common.jdbc.bean.TableInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLServer 数据库表基本接口实现类
 * @version 1.0
 * @date 2021-11-03
 **/
public class SQLServerTableServiceImpl implements TableService {

    @Override
    public List<TableInfo> selectTables(Connector connector) throws Exception {
        List<TableInfo> tableInfos = new ArrayList<>();

        BaseDao baseDao = new BaseDao(connector);
        Connection con = baseDao.getConnection();

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        String sql = "select a.name, cast([value] as varchar(500))[value] from " +
                " sys.tables a left join sys.extended_properties g on " +
                " (a.object_id = g.major_id AND g.minor_id = 0)";
        try {
            if (con == null) {
                System.out.println("数据库连接失败");
                return tableInfos;
            }
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                String tableComment = rs.getString("value");
                tableComment = tableComment == null ? "" : tableComment;

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
    public String getTableComment(Connection con, String schema, String tableName) throws Exception {
        PreparedStatement pstmt = null;
        String sql = "SELECT DISTINCT d.name, f.value FROM syscolumns a " +
                " LEFT JOIN systypes b ON a.xusertype= b.xusertype " +
                " INNER JOIN sysobjects d ON a.id= d.id " +
                " AND d.xtype= 'U' " +
                " AND d.name<> 'dtproperties'" +
                " LEFT JOIN syscomments e ON a.cdefault= e.id" +
                " LEFT JOIN sys.extended_properties g ON a.id= G.major_id " +
                " AND a.colid= g.minor_id" +
                " LEFT JOIN sys.extended_properties f ON d.id= f.major_id " +
                " AND f.minor_id= 0 and d.name = '"+tableName+"'";
        pstmt = con.prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        String comment = "";
        if (result != null && result.next()) {
            comment = result.getString("name");
            if (comment != null && !"".equals(comment)) {
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
