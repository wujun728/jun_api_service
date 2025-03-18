package io.github.wujun728.api.common.bean;

import io.github.wujun728.api.common.jdbc.util.StringUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页排序参数
 *
 * @version 1.0
 * @date 2024-05-06
 */
@ApiModel(value = "分页排序参数",description="分页排序参数")
public class Wrapper {

    public static final String SORT_ORDER_DESC = "DESC";

    public static final String SORT_ORDER_ASC = "ASC";

    @ApiModelProperty(value = "分页参数 第几页",example = "1")
    private Integer pageIndex;

    @ApiModelProperty(value = "分页参数 每页条数",example = "10")
    private Integer pageSize;

    @ApiModelProperty(value = "排序名",example = "create_time")
    private String sortName;

    @ApiModelProperty(value = "排序顺序",allowableValues = "desc,asc",allowEmptyValue = true)
    private String sortOrder;

    public Wrapper(Integer pageIndex, Integer pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public Wrapper(String sortName, String sortOrder) {
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public Wrapper(Integer pageIndex, Integer pageSize, String sortName, String sortOrder) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.sortName = sortName;
        this.sortOrder = sortOrder;
    }

    public Wrapper() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortName() {
        //驼峰命名 改为 下划线名称
        if(StrUtil.isNotBlank(sortName)){
            sortName = StringUtil.camelToSnake(sortName);
        }
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void clearWrapper(){
        this.pageIndex = null;
        this.pageSize = null;
        this.sortName = null;
        this.sortOrder = null;
    }

}
