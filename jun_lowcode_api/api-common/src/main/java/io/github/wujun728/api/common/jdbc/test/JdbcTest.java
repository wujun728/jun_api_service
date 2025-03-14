package io.github.wujun728.api.common.jdbc.test;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import com.alibaba.fastjson.JSON;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedHashMap;
import java.util.List;

public class JdbcTest {

    public static void main(String[] args) throws Exception {
        Connector connector = new Connector();
        connector.setDbType("mysql");
        connector.setDriver("com.mysql.cj.jdbc.Driver");
        connector.setUrl("jdbc:mysql://127.0.0.1:3306/api_server?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        connector.setUser("root");
        connector.setPassword("root123456");

       /* try {
            List<TableInfo> tableInfos = TableInfoFactory.getTableService(Connector.DB_TYPE_MYSQL).selectTables(connector);
            System.out.println(JSON.toJSONString(tableInfos));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

//        String sql = "select * from sys_user where 1=1 ";

        String sql = "select count(1) as total, user_name from sys_user where 1=1 group by user_name";
        BaseDao baseDao = new BaseDao(connector);
        Connection connection = baseDao.getConnection();
        PreparedStatement statement = connection.prepareStatement("use api_server");
        statement.execute();

        try {
           /* Connection connection = baseDao.getConnection();
            PreparedStatement statement = connection.prepareStatement("show databases");
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                String database = rs.getString(1);
                System.out.println(database);
            }*/
            
            List<LinkedHashMap<String,Object>> list = baseDao.findResult(sql, null, true);
            System.out.println(JSON.toJSONString(list));
           /* List<SysUser> sysUsers = baseDao.findSingleTable(sql, null, SysUser.class, true);
            System.out.println(JSON.toJSONString(sysUsers));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
