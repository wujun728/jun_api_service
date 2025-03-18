package io.github.wujun728.api.core.vo;

import java.util.List;

public class SQLParamVo {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 注释
     */
    private String tableComment;

    /**
     * 表别名
     */
    private String tableAlias;

    /**
     * 查询字段集合
     */
    private List<QueryColumnVo> queryColumns;

    /**
     * 查询条件参数集合
     */
    private List<ParamVo> paramVos;

    /**
     * 关联表参数
     */
    private List<UnionParamVo> unionParamVos;

    /**
     * 是否主表
     */
    private Boolean master;

    public List<ParamVo> getParamVos() {
        return paramVos;
    }

    public void setParamVos(List<ParamVo> paramVos) {
        this.paramVos = paramVos;
    }

    public List<UnionParamVo> getUnionParamVos() {
        return unionParamVos;
    }

    public void setUnionParamVos(List<UnionParamVo> unionParamVos) {
        this.unionParamVos = unionParamVos;
    }

    public String getTableAlias() {
        return tableAlias;
    }

    public void setTableAlias(String tableAlias) {
        this.tableAlias = tableAlias;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<QueryColumnVo> getQueryColumns() {
        return queryColumns;
    }

    public void setQueryColumns(List<QueryColumnVo> queryColumns) {
        this.queryColumns = queryColumns;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

}
