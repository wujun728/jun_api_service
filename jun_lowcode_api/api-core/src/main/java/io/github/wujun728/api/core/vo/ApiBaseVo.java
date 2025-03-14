package io.github.wujun728.api.core.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API基础信息Vo",description = "API基础信息Vo")
public class ApiBaseVo {

    @ApiModelProperty(value = "接口ID")
    private Integer apiId;

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口地址 支持多级")
    private String apiPath;

    @ApiModelProperty(value = "请求方式GET/POST")
    private String apiType;

    @ApiModelProperty(value = "数据源名称")
    private String sourceName;

    @ApiModelProperty(value = "数据源ID")
    private Integer sourceId;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

    @ApiModelProperty(value = "操作表 多表用,分割")
    private String tableName;

    @ApiModelProperty(value = "接口创建类型 sql语句/param自定义参数/auto 自动生成")
    private String paramType;

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getGroupName() {
        return StrUtil.isBlank(groupName)?"未分组":groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getApiType() {
        return apiType;
    }

    public void setApiType(String apiType) {
        this.apiType = apiType;
    }
}
