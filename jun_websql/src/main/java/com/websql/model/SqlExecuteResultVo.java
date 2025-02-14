package com.websql.model;

import lombok.Data;

/**
 * @ClassName : SqlExecuteResultVo
 * @Description : 返回对象
 * @Author :  rabbit boy_0214@sina.com
 * @Date: 2023/10/31 16:32
 */

@Data
public class SqlExecuteResultVo {

    /**
     * 呈现类型  0列表 1信息
     */
    private int type;

    /**
     * 执行类型
     */
    private String executeType;

    /**
     * 查询的表名
     */
    private String tableNameList;

    /**
     * 数据字段
     */
    private Object data;

    /**
     * 执行状态
     */
    private int status;

    /**
     * 执行失败的错误信息
     */
    private String errorMessage;


    /**
     * 执行SQL
     */
    private String parserSql;

    /**
     * 数据源KEY
     */
    private String dataBaseKey;

    /**
     * 耗时
     */
    private long time;
}
