package io.github.wujun728.api.core.vo;

import java.util.List;

public class UnionParamVo {

    /**
     * 表名
     */
    private String linkTableName;

    /**
     * 表名
     */
    private String linkTableAlias;

    /**
     * 连接方式
     */
    private String joinType ; //LEFT JOIN /  RIGHT JOIN / INNER JOIN

    /**
     * 列集合
     */
    private List<UnionColumnVo> unionColumns;

    public String getLinkTableName() {
        return linkTableName;
    }

    public void setLinkTableName(String linkTableName) {
        this.linkTableName = linkTableName;
    }

    public String getLinkTableAlias() {
        return linkTableAlias;
    }

    public void setLinkTableAlias(String linkTableAlias) {
        this.linkTableAlias = linkTableAlias;
    }

    public String getJoinType() {
        return joinType;
    }

    public void setJoinType(String joinType) {
        this.joinType = joinType;
    }

    public List<UnionColumnVo> getUnionColumns() {
        return unionColumns;
    }

    public void setUnionColumns(List<UnionColumnVo> unionColumns) {
        this.unionColumns = unionColumns;
    }
}
