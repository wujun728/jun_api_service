package com.websql.model;

import lombok.Data;

/**
 * @ClassName : VersionModel
 * @Description : 版本检查实体
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 14:08
 */
@Data
public class VersionModel {

    private String version;

    private String localVersion;

    private String date;

    private String link;

    private Boolean push = false;

}
