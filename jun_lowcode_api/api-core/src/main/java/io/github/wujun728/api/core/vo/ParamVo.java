package io.github.wujun728.api.core.vo;

public class ParamVo {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 注释
     */
    private String columnComment;

    /**
     * 是否主键
     */
    private Boolean isPrimaryKey;

    /**
     * 属性
     */
    private String attrName;

    /**
     * 表字段类型 int varchar json datetime
     */
    private String dataType;

    /**
     * 属性类型
     */
    private String attrType;

    /**
     * 大于
     */
    private Boolean greater;

    /**
     * 大于等于
     */
    private Boolean greaterEquals;

    /**
     * 小于等于
     */
    private Boolean lessEquals;

    /**
     * 小于
     */
    private Boolean less;

    /**
     * 不等于
     */
    private Boolean notEquals;

    /**
     * in
     */
    private Boolean inEquals;

    /**
     * 模糊查询
     */
    private Boolean likeEquals;

    /**
     * 默认值
     */
    private String columnDefault;

    private Boolean sorted;

    private Boolean order;

    /**
     * 分页条件 开始
     */
    private Boolean limitStart;

    /**
     * 分页条件 每页条数
     */
    private Boolean limitSize;

    /**
     * 新增或修改时是否校验唯一
     */
    private Boolean uniqueVerify;

    public Boolean getUniqueVerify() {
        return uniqueVerify;
    }

    public void setUniqueVerify(Boolean uniqueVerify) {
        this.uniqueVerify = uniqueVerify;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Boolean getOrder() {
        return order;
    }

    public void setOrder(Boolean order) {
        this.order = order;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
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

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public Boolean getGreater() {
        return greater;
    }

    public void setGreater(Boolean greater) {
        this.greater = greater;
    }

    public Boolean getGreaterEquals() {
        return greaterEquals;
    }

    public void setGreaterEquals(Boolean greaterEquals) {
        this.greaterEquals = greaterEquals;
    }

    public Boolean getLessEquals() {
        return lessEquals;
    }

    public void setLessEquals(Boolean lessEquals) {
        this.lessEquals = lessEquals;
    }

    public Boolean getLess() {
        return less;
    }

    public void setLess(Boolean less) {
        this.less = less;
    }

    public Boolean getNotEquals() {
        return notEquals;
    }

    public void setNotEquals(Boolean notEquals) {
        this.notEquals = notEquals;
    }

    public Boolean getInEquals() {
        return inEquals;
    }

    public void setInEquals(Boolean inEquals) {
        this.inEquals = inEquals;
    }

    public Boolean getLikeEquals() {
        return likeEquals;
    }

    public void setLikeEquals(Boolean likeEquals) {
        this.likeEquals = likeEquals;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public Boolean getSorted() {
        return sorted;
    }

    public void setSorted(Boolean sorted) {
        this.sorted = sorted;
    }

    public Boolean getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(Boolean limitStart) {
        this.limitStart = limitStart;
    }

    public Boolean getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(Boolean limitSize) {
        this.limitSize = limitSize;
    }
}
