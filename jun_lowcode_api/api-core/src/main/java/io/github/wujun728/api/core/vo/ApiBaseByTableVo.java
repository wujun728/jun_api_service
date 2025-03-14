package io.github.wujun728.api.core.vo;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API数据表基础信息Vo",description = "API数据表基础信息Vo")
public class ApiBaseByTableVo {

    @ApiModelProperty(value = "接口名称")
    private String apiName;

    @ApiModelProperty(value = "接口地址 支持多级")
    private String apiPath;

    @ApiModelProperty(value = "请求方式GET/POST")
    private String apiType;

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "操作类型（增删改查INSERT/DELETE/UPDATE/SELECT/SELECT_ONE）")
    private String opType;

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
