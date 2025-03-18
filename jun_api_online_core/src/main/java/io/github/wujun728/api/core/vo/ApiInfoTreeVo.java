package io.github.wujun728.api.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API信息树形Vo",description = "API信息树形Vo")
public class ApiInfoTreeVo {

    @ApiModelProperty(value = "数据源名称")
    private String sourceName;

    @ApiModelProperty(value = "数据源ID")
    private Integer sourceId;
    
    @ApiModelProperty(value = "API数据库集合")
    private List<ApiDatabaseTreeVo> databases;

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

    public List<ApiDatabaseTreeVo> getDatabases() {
        return databases;
    }

    public void setDatabases(List<ApiDatabaseTreeVo> databases) {
        this.databases = databases;
    }
}
