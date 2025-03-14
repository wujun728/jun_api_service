package io.github.wujun728.api.common.jdbc.factory;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.service.TableService;
import io.github.wujun728.api.common.jdbc.service.impl.DB2TableServiceImpl;
import io.github.wujun728.api.common.jdbc.service.impl.MySQLTableServiceImpl;
import io.github.wujun728.api.common.jdbc.service.impl.SQLServerTableServiceImpl;

/**
 * 数据库表操作类
 *
 * @version 1.0
 * @date 2021-11-03
 **/
public class TableInfoFactory {

    /**
     * 获取service
     * @return
     * @throws Exception
     */
    public static TableService getTableService(String dbType) throws Exception {
        TableService tableService = null;
        if (Connector.DB_TYPE_MYSQL.equals(dbType)) {
            tableService = new MySQLTableServiceImpl();
        }else if (Connector.DB_TYPE_DB2.equals(dbType)) {
            tableService = new DB2TableServiceImpl();
        } else if (Connector.DB_TYPE_SQLSERVER.equals(dbType)) {
            tableService = new SQLServerTableServiceImpl();
        }else{
            throw new Exception("dbType参数错误");
        }
        return tableService;
    }

}
