package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @ClassName DataSourceMeta
 * @Description 数据表元数据描述
 * @Author rabbit boy_0214@sina.com
 * @Date 2023/10/26 14:15
 **/

@Data
@Accessors(chain = true)
public class DataSourceMeta {


    private String productName;

    private String productVersion;

    private String driverName;

    private String driverVersion;

    private Boolean readOnly;

    private Boolean supportsTransactions;

    private String tableName;

    private String dataSourceKey;

    private String databaseName;

    private String tableType;

    private String tableSchema;

    private String tableComment;

    private List<DataSourceTableMeta> tablesMetaList;

    private List<DataSourceTableMeta> tablesKeysMetaList;

    private List<DataSourceIndexMeta> tablesIndexMetaList;
}

