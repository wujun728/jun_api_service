package io.github.wujun728.api.common.jdbc.factory;


import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.service.EntityService;
import io.github.wujun728.api.common.jdbc.service.impl.DB2EntityServiceImpl;
import io.github.wujun728.api.common.jdbc.service.impl.MySQLEntityServiceImpl;
import io.github.wujun728.api.common.jdbc.service.impl.SQLServerEntityServiceImpl;

public class EntityServiceFactory {


    /**
     * 获取entityService
     * @param dbType
     * @return
     */
    public static EntityService getEntityService(String dbType) throws Exception {
        if (Connector.DB_TYPE_MYSQL.equals(dbType)) {
            return new MySQLEntityServiceImpl();
        }else if (Connector.DB_TYPE_DB2.equals(dbType)) {
            return new DB2EntityServiceImpl();
        } else if (Connector.DB_TYPE_SQLSERVER.equals(dbType)) {
           return new SQLServerEntityServiceImpl();
        }
        throw new Exception("数据库类型 dbType 参数错误");
    }
}
