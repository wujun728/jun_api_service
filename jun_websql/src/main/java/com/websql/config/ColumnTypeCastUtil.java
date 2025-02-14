package com.websql.config;


import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

import java.io.Reader;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @ClassName : ColumnTypeCastUtil
 * @Description : 数据列转换特殊类型
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2024/2/28 17:35
 */
public class ColumnTypeCastUtil {


    public static Object getDataTypeResult(ResultSet resultSet, int index, ResultSetMetaData metaData) throws SQLException {
        Object colsValue = resultSet.getObject(index);
        if (ObjectUtil.isEmpty(colsValue)) {
            return "";
        }
        String columnType = metaData.getColumnTypeName(index);
        if ("DATETIME".equals(columnType)) {
            if (colsValue instanceof ZonedDateTime) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                colsValue = formatter.format((ZonedDateTime) colsValue);
            }
            if (colsValue instanceof LocalDateTime) {
                colsValue = DateUtil.formatLocalDateTime((LocalDateTime) colsValue);
            }
            if (colsValue instanceof Date) {
                colsValue = DateUtil.format((Date) colsValue, "yyyy-MM-dd hh:mm:ss");
            }
            return colsValue;
        }
        if ("DATE".equals(columnType)) {
            return DateUtil.format(resultSet.getDate(index), "yyyy-MM-dd");
        }
        if ("CLOB".equals(columnType)) {
            return clobToString(resultSet.getClob(index));
        }
        if ("BLOB".equals(columnType)) {
            Blob blob = resultSet.getBlob(index);
            return new String(blob.getBytes(1, (int) blob.length()));
        }
        return colsValue;
    }


    /**
     * clob 转string
     *
     * @param clob 类型
     * @return String 类型
     */
    public static String clobToString(Clob clob) {
        StringBuffer sb = new StringBuffer(1024);
        try (Reader stream = clob.getCharacterStream()) {
            char[] buffer = new char[(int) clob.length()];
            int length;
            while ((length = stream.read(buffer)) != -1) {
                sb.append(buffer, 0, length);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


}
