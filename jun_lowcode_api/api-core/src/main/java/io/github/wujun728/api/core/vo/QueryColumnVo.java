package io.github.wujun728.api.core.vo;

public class QueryColumnVo {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 注释
     */
    private String columnComment;

    /**
     * 别名
     */
    private String aliasName;

    /**
     * 属性
     */
    private String attrName;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }
}
