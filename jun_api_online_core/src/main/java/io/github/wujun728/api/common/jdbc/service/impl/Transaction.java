package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.service.ITransaction;
import io.github.wujun728.api.common.jdbc.util.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务处理实现类
 *
 * @author 兰平
 * @version 1.0
 * @date 2017-08-22
 */
public class Transaction implements ITransaction {

    private Connection conn;

    public Transaction(Connector connector) throws DaoException {
        this.conn = new BaseDao(connector).getConnection();
        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(e, "Transaction设置连接AutoCommit时失败...");
        }
    }

    public Transaction(Connection conn) throws DaoException {
        this.conn = conn;
        try {
            this.conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException(e, "Transaction设置连接AutoCommit时失败...");
        }
    }

    @Override
    public void commit() throws DaoException {
        try {
            conn.commit();
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            throw new DaoException(e, "事务提交时失败...");
        }
    }

    @Override
    public void rollback() throws DaoException {
        try {
            conn.rollback();
            conn.setAutoCommit(true);
            conn.close();
        } catch (SQLException e) {
            throw new DaoException(e, "事务回滚时失败...");
        }
    }

    @Override
    public Connection getConnection() throws DaoException {
        return conn;
    }

    @Override
    public void close() throws DaoException {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new DaoException(e, "连接关闭时失败...");
            }
        }
    }

}