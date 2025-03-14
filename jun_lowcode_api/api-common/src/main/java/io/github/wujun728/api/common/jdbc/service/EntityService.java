package io.github.wujun728.api.common.jdbc.service;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;

/**
 * @version 1.0
 * @date 2019-07-06
 **/
public interface EntityService {

    /**
     *  获取EntityInfo
     *
     * @param connector
     * @param tableName 表名
     * @return 实体信息
     * @throws Exception
     */
    public EntityInfo getEntityInfo(Connector connector, String tableName) throws Exception;
}
