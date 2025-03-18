package io.github.wujun728.api.common.bean;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 分页数据
 * @version 1.0
 * @date 2024-05-06
 */
@ApiModel(value = "分页数据", description = "分页数据")
public class Page<T> {

    @ApiModelProperty(value = "数据集合")
    private List<T> records;

    @ApiModelProperty(value = "总数")
    private long total;

    public Page(List<T> records, long total) {
        this.records = records;
        this.total = total;
    }

    public Page() {
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
