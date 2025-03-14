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
 * DB2 数据库表基本接口实现类
 * @version 1.0
 * @date 2021-11-03
 **/
public class DB2TableServiceImpl implements TableService {

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
            String sql = "select * from sysibm.systables where type='T' and creator='" + connector.getSchema() + "'";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String tableName = rs.getString("name");
                tableName = tableName.toLowerCase();
                String tableComment = rs.getString("remarks");
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
        //无需实现
        return "";
    }
}
