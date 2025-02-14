package com.websql.model;

import lombok.Data;

import java.util.List;

/**
 * @ClassName : SqlParserVo
 * @Description :
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/10/31 16:08
 */


@Data
public class SqlParserVo {

    private String methodType;

    private List<String> tableNameList;

    private List<String> tableColumns;

    private String sqlContent;

    private String dataBase;

    private String dataBaseKey;
}
