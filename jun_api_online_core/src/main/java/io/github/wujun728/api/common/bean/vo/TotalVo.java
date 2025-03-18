package io.github.wujun728.api.common.bean.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * ID对象
 * @version 1.0
 * @date 2024-05-06
 */
@ApiModel(value = "Total对象", description = "Total对象")
public class TotalVo<T> {

    @ApiModelProperty(value = "总数")
    private T total;

    public T getTotal() {
        return total;
    }

    public void setTotal(T total) {
        this.total = total;
    }

    public TotalVo(T total) {
        this.total = total;
    }
}
