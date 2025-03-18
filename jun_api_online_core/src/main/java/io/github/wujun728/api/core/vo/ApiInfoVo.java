package io.github.wujun728.api.core.vo;

import io.github.wujun728.api.core.entity.ApiInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API信息Vo",description = "API信息Vo")
public class ApiInfoVo extends ApiInfo {

    @ApiModelProperty(value = "数据源名称")
    private String sourceName;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
