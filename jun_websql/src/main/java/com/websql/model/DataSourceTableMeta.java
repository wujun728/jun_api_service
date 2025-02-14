package com.websql.model;

import lombok.Data;

/**
 * @ClassName DataSourceMeta
 * @Description 数据列描述
 * @Author rabbit boy_0214@sina.com
 * @Date 2023/10/26 14:32
 **/
@Data
public class DataSourceTableMeta {


    private String tableName;

    private String columnName;

    private String typeName;

    private String nullable;

    private String autoIncrement;

    private String comment;

    private int keySeq;

    private String pkName;

}
