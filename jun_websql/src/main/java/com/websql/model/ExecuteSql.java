package com.websql.model;

import lombok.Data;

import javax.persistence.Transient;

/**
 * @ClassName ExecuteSql
 * @Description 执行器BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/21 0021 20:33
 **/
@Data
public class ExecuteSql {

    /**
     * 执行的数据源名称
     */
    @Transient
    private String dataBaseName;

    /**
     * 执行的sql文本
     */
    @Transient
    private String sqlText;

    private boolean isExport;

}
