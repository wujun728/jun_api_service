package io.github.wujun728.api.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * API信息Vo
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API分组信息Vo",description = "API分组信息Vo")
public class ApiGroupTreeVo {

    @ApiModelProperty(value = "分组名称")
    private String groupName;

    @ApiModelProperty(value = "API接口集合")
    private List<ApiBaseVo> apis ;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ApiBaseVo> getApis() {
        return apis;
    }

    public void setApis(List<ApiBaseVo> apis) {
        this.apis = apis;
    }
}
