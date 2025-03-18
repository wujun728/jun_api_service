package io.github.wujun728.api.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ID对象
 * @version 1.0
 * @date 2024-05-06
 */
@ApiModel(value = "ID对象", description = "ID对象")
public class IdVo<T> {

    @ApiModelProperty(value = "主键或数据ID")
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public IdVo(T id) {
        this.id = id;
    }

}
