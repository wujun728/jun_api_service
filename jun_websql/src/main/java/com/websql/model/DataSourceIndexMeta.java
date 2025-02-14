package com.websql.model;

import lombok.Data;

/**
 * @ClassName DataSourceIndexMeta
 * @Description 表索引描述
 * @Author rabbit boy_0214@sina.com
 * @Date 2023/10/26 16:35
 **/
@Data
public class DataSourceIndexMeta {


    private String tableName;

    private String columnName;

    private Boolean nonUnique;

    private String indexName;

}
