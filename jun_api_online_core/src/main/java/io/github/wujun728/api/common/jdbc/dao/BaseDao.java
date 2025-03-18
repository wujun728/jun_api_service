package io.github.wujun728.api.common.jdbc.dao;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.service.ITransaction;
import io.github.wujun728.api.common.jdbc.util.DaoException;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 * 数据库连接
 *
 * @version 1.0
 * @date 2019-06-30
 **/
public class BaseDao {

    private Connector connector;

    private Logger logger = LoggerFactory.getLogger(BaseDao.class);
    /**
     * 构造方法
     *
     * @param connector 数据库连接器
     */
    public BaseDao(Connector connector) {
        this.connector = connector;
    }


    //静态快，用来加载驱动
    /*static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            try {
                throw new DaoException(e, "加载SQL驱动时异常...");
            } catch (DaoException e1) {
                e1.printStackTrace();
            }
        }
    }*/

    /**
     * 获取连接
     */
    public Connection getConnection() throws DaoException {
        Connection con = null;
        try {
            try {
                Class.forName(connector.getDriver());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(connector.getUrl(), connector.getUser(),
                    connector.getPassword());
        } catch (SQLException e) {
            throw new DaoException(e, "数据库连接失败");
        }
        return con;
    }

    /**
     * 关闭的方法
     */
    public void closeAll(Connection con, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置PreparedStatement对象的sql语句中的参数？
     */
    public void setValues(PreparedStatement pstmt, List<Object> params) throws DaoException {
        if (pstmt != null && params != null && params.size() > 0) {
            for (int i = 0; i < params.size(); i++) {
                try {
                    if (params.get(i) != null) {
                        String paramType = params.get(i).getClass().getName();
                        if ("java.lang.Integer".equals(paramType)) {
                            pstmt.setInt(i + 1, (Integer) params.get(i));
                        } else if ("java.lang.String".equals(paramType)) {
                            pstmt.setString(i + 1, (String) params.get(i));
                        } else if ("java.math.BigDecimal".equals(paramType)) {
                            pstmt.setBigDecimal(i + 1, (BigDecimal) params.get(i));
                        } else if ("java.sql.Timestamp".equals(paramType)) {
                            pstmt.setTimestamp(i + 1, (Timestamp) params.get(i));
                        } else if ("java.lang.Double".equals(paramType)) {
                            pstmt.setDouble(i + 1, (Double) params.get(i));
                        } else if ("java.lang.Float".equals(paramType)) {
                            pstmt.setFloat(i + 1, (Float) params.get(i));
                        } else if ("java.lang.Date".equals(paramType)) {
                            pstmt.setString(i + 1, (String) params.get(i));
                        } else {
                            pstmt.setObject(i + 1, params.get(i));
                        }
                    } else {
                        pstmt.setObject(i + 1, null);
                    }
                } catch (SQLException e) {
                    throw new DaoException(e, "设置参数时异常");
                }
            }
        }
    }

    /**
     * 多表增删改
     *
     * @param sql    sql语句集合，里面可以加？
     * @param params 参数列表，用来替换sql中的?（占位符）
     * @return int 返回的值。成功>0，失败<=0
     */
    public synchronized int batchUpdate(List<String> sql, List<List<Object>> params) throws DaoException {
        logger.info("batchUpdate sql = {}",sql);

        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            con.setAutoCommit(false); // 事务处理
            for (int i = 0; i < sql.size(); i++) {
                List<Object> param = params.get(i);
                pstmt = con.prepareStatement(sql.get(i));
                setValues(pstmt, param); // 设置参数
                pstmt.executeUpdate();
            }
            result = 1;
            con.commit(); // 没有错误执行
        } catch (SQLException e) {
            try {
                result = 0;
                e.printStackTrace();
                con.rollback(); // 出错回滚
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new DaoException(e, "SQL语句执行失败");
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeAll(con, pstmt, rs);
        }
        logger.info("batchUpdate result = {}", result);
        return result;
    }

    /**
     * 单表增删改
     *
     * @param sql    sql语句，里面可以加？
     * @param params 参数列表，用来替换sql中的?（占位符）
     * @return int 返回的值。成功>0，失败<=0
     */
    public synchronized int singleUpdate(String sql, List<Object> params) throws DaoException {
        logger.info("singleUpdate sql = {}",sql);
        logger.info("singleUpdate params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");
        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            setValues(pstmt, params); // 设置参数
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e, "操作异常，SQL语句执行失败");
        } finally {
            closeAll(con, pstmt, null);
        }
        logger.info("singleUpdate result = {}", result);
        return result;
    }

    /**
     * 单表增删改
     *
     * @param sql    sql语句，里面可以加？
     * @param params 参数列表，用来替换sql中的?（占位符）
     * @param conn   数据库连接对象
     * @return int 返回的值。成功>0，失败<=0
     */
    public synchronized int singleUpdate(String sql, List<Object> params,
                                         Connection conn) throws DaoException {
        logger.info("singleUpdate sql = {}",sql);
        logger.info("singleUpdate params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");

        int result = 0;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            setValues(pstmt, params);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e, "操作异常，SQL语句执行失败");
        }
        logger.info("singleUpdate result = {}", result);
        return result;
    }

    /**
     * 单表增删改
     *
     * @param sql    sql语句，里面可以加？
     * @param params 参数列表，用来替换sql中的?（占位符）
     * @param tran   事务对象
     * @return int 返回的值。成功>0，失败<=0
     */
    public synchronized int singleUpdate(String sql, List<Object> params,
                                         ITransaction tran) throws DaoException {
        logger.info("singleUpdate sql = {}",sql);
        logger.info("singleUpdate params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");
        int result = 0;
        PreparedStatement pstmt = null;
        try {
            Connection conn = tran.getConnection();
            pstmt = conn.prepareStatement(sql);
            setValues(pstmt, params);
            result = pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e, "操作异常，SQL语句执行失败");
        }
        logger.info("singleUpdate result = {}", result);
        return result;
    }

    /**
     * 聚合查询
     *
     * @param sql    sql语句，里面可以加？
     * @param params 参数列表，用来替换sql中的?（占位符）
     * @return list 返回的结果集
     */
    public List<String> uniqueResult(String sql, List<Object> params) throws DaoException {

        logger.info("uniqueResult sql = {}",sql);
        logger.info("uniqueResult params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");

        List<String> list = new ArrayList<String>();
        Connection con = this.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = con.prepareStatement(sql);
            setValues(pstmt, params); // 设置参数
            rs = pstmt.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); // 数据库元数据
            int count = md.getColumnCount(); // 取出结果集中列的数量
            if (rs != null && rs.next()) {
                for (int i = 1; i <= count; i++) {
                    list.add(rs.getString(i));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e, "SQL语句查询失败");
        } finally {
            closeAll(con, pstmt, rs);
        }
        logger.info("uniqueResult total = {}", list.size());
        return list;
    }

    /**
     * 查询单个表
     *
     * @param sql         sql语句，里面可以加？
     * @param params      参数列表，用来替换sql中的?（占位符）
     * @param c           泛型类型所对应的反射对象
     * @param isCamelCase 是否驼峰命名
     * @return 存储了对象的集合
     */
    public <T> List<T> findSingleTable(String sql, List<Object> params,
                                       Class<T> c, boolean isCamelCase) throws DaoException {

        logger.info("findSingleTable sql = {}",sql);
        logger.info("findSingleTable params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");

        List<T> list = new ArrayList<T>(); // 要返回的结果集
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection(); // 获取连接
            pstmt = con.prepareStatement(sql);
            setValues(pstmt, params); // 设置参数
            rs = pstmt.executeQuery();

            Method[] ms = c.getMethods(); // 取出这个反射实例的所有方法

            ResultSetMetaData md = rs.getMetaData(); // 获取结果集的元数据
            String[] colnames = new String[md.getColumnCount()];
            for (int i = 0; i < colnames.length; i++) { // 将列名保存到colnames数组中
                colnames[i] = md.getColumnLabel(i + 1);
            }

            T t;
            String mname = null;
            String cname = null;
            String ctypename = null;
            while (rs.next()) {
                t = (T) c.newInstance(); // 通过反射实例来获取一个对象 然后将数据库中的对象的值 注入到这个对象中
                for (int i = 0; i < colnames.length; i++) {// 循环方法名
                    if (!isCamelCase) {
                        cname = "set" + colnames[i]; // 取出列名并在前面加上set
                    } else {
                        cname = "set" + StringUtil.toUnderscoreToCamelCase(colnames[i]); // 取出列名并在前面加上set
                    }
                    for (Method m : ms) {// 循环列名
                        mname = m.getName(); // 取出方法名
                        if (cname.equalsIgnoreCase(mname) && rs.getObject(colnames[i]) != null) {// 判断方法名和列名是否一样，相同则激活方法，注入数据
                            ctypename = rs.getObject(colnames[i]).getClass().getName();// 获取当前列的类型名
                            if ("java.lang.Integer".equals(ctypename)) {
                                m.invoke(t, rs.getInt(colnames[i]));// invoke激活
                            } else if ("java.lang.String".equals(ctypename)) {
                                m.invoke(t, rs.getString(colnames[i]));
                            } else if ("java.math.BigDecimal".equals(ctypename)) {
                                try {
                                    m.invoke(t, rs.getBigDecimal(colnames[i]));
                                } catch (Exception e1) {
                                    m.invoke(t, rs.getDouble(colnames[i]));
                                }
                            } else if ("java.sql.Timestamp".equals(ctypename)) {
                                m.invoke(t, rs.getString(colnames[i]));
                            } else if ("java.sql.Date".equals(ctypename)) {
                                m.invoke(t, rs.getString(colnames[i]));
                            } else if ("java.sql.Time".equals(ctypename)) {
                                m.invoke(t, rs.getString(colnames[i]));
                            } else if ("java.lang.Float".equals(ctypename)) {
                                m.invoke(t, rs.getFloat(colnames[i]));
                            } else if ("java.lang.Double".equals(ctypename)) {
                                m.invoke(t, rs.getDouble(colnames[i]));
                            } else {
                                m.invoke(t, rs.getString(colnames[i]));
                            }
                            break;
                        }
                    }
                }
                list.add(t);
            }
        } catch (Exception e) {
            throw new DaoException(e, "数据库查询失败");
        } finally {
            closeAll(con, pstmt, rs);
        }
        logger.info("findSingleTable total = {}", list.size());
        return list;
    }

    /**
     * 多表查询
     *
     * @param sql         sql语句，里面可以加？
     * @param params      参数列表，用来替换sql中的?（占位符）
     * @param isCamelCase 是否驼峰命名
     * @return 结果集，存在一个List表中，用Map一对一的存放
     */
    public List<LinkedHashMap<String, Object>> findResult(String sql, List<Object> params, boolean isCamelCase) throws DaoException {

        logger.info("findResult sql = {}",sql);
        logger.info("findResult params = {}", ObjectUtil.isNotEmpty(params)?ArrayUtil.join(params.toArray(new Object[0]), ", "):"");

        List<LinkedHashMap<String, Object>> list = new ArrayList<LinkedHashMap<String, Object>>();// 查询的结果集
        LinkedHashMap<String, Object> map;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = getConnection(); // 获取连接
            pstmt = con.prepareStatement(sql);
            setValues(pstmt, params); // 设置参数
            rs = pstmt.executeQuery();

            ResultSetMetaData md = rs.getMetaData(); // 获取结果集的元数据
            String[] colnames = new String[md.getColumnCount()]; // 获取结果集中的列名
            for (int i = 0; i < colnames.length; i++) { // 将列名保存到colnames数组中
                colnames[i] = md.getColumnLabel(i + 1);
            }

            if (rs != null) {
                while (rs.next()) {
                    map = new LinkedHashMap<String, Object>();
                    for (int i = 0; i < colnames.length; i++) {// 循环列名
                        if (rs.getObject(colnames[i]) != null) {
                            String ctypename = rs.getObject(colnames[i]).getClass().getName();// 获取当前列的类型名
                            if ("java.lang.Integer".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getInt(colnames[i]), isCamelCase);
                            } else if ("java.lang.String".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getString(colnames[i]), isCamelCase);
                            } else if ("java.math.BigDecimal".equals(ctypename)) {
                                try {
                                    putMap(map, colnames[i], rs.getBigDecimal(colnames[i]), isCamelCase);
                                    map.put(colnames[i], rs.getBigDecimal(colnames[i]));
                                } catch (Exception e1) {
                                    putMap(map, colnames[i], rs.getDouble(colnames[i]), isCamelCase);
                                }
                            } else if ("java.sql.Timestamp".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getString(colnames[i]), isCamelCase);
                            } else if ("java.sql.Date".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getString(colnames[i]), isCamelCase);
                            } else if ("java.sql.Time".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getString(colnames[i]), isCamelCase);
                            } else if ("java.lang.Float".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getFloat(colnames[i]), isCamelCase);
                            } else if ("java.lang.Double".equals(ctypename)) {
                                putMap(map, colnames[i], rs.getDouble(colnames[i]), isCamelCase);
                            } else {
                                putMap(map, colnames[i], rs.getString(colnames[i]), isCamelCase);
                            }
                        } else {
                            putMap(map, colnames[i], null, isCamelCase);
                        }
                    }
                    list.add(map);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e, "数据库查询失败");
        } finally {
            closeAll(con, pstmt, rs);
        }
        logger.info("findResult total = {}", list.size());
        return list;
    }

    private void putMap(Map<String, Object> map, String key, Object value, boolean isCamelCase) {
        if (!isCamelCase) {
            map.put(key, value);
            return;
        }
        String caseKey = StringUtil.toUnderscoreToCamelCase(key);
        map.put(caseKey, value);
    }

    public List<String> getDatabases() throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            List<String> databases = new ArrayList<>();
            con = this.getConnection();
            pstmt = con.prepareStatement("show databases");
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String database = rs.getString(1);
                databases.add(database);
            }
            return databases;
        } catch (SQLException e) {
            throw new DaoException(e, "数据库连接失败");
        } finally {
            closeAll(con, pstmt, rs);
        }
    }

}
