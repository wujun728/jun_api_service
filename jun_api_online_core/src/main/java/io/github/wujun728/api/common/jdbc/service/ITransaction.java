package io.github.wujun728.api.common.jdbc.service;


import io.github.wujun728.api.common.jdbc.util.DaoException;

import java.sql.Connection;

/**
 * 事务处理
 *
 * @author 兰平
 * @version 1.0
 * @date 2017-08-22
 */
public interface ITransaction {

    /**
     * 提交
     */
    public void commit() throws DaoException;

    /**
     * 回滚
     */
    public void rollback() throws DaoException;

    /**
     * 获取连接
     */
    public Connection getConnection() throws DaoException;

    /**
     * 关闭连接
     */
    public void close() throws DaoException;

}