package io.github.wujun728.api.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API信息数据库树形Vo",description = "API信息数据库树形Vo")
public class ApiDatabaseTreeVo {

    @ApiModelProperty(value = "数据库名称")
    private String databaseName;

    @ApiModelProperty(value = "API分组集合")
    private List<ApiGroupTreeVo> groups;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<ApiGroupTreeVo> getGroups() {
        return groups;
    }

    public void setGroups(List<ApiGroupTreeVo> groups) {
        this.groups = groups;
    }
}
