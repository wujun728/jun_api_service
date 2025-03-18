package io.github.wujun728.api.core.vo;

public class UnionColumnVo {

    /**
     * 第一张列名
     */
    private String columnName;

    /**
     *  = !=  > <  默认 =
     */
    private String equalsType;

    /**
     * 第二张表列表
     */
    private String secondColumnName;

    /**
     * 连接符 and or
     */
    private String linkType;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getEqualsType() {
        return equalsType;
    }

    public void setEqualsType(String equalsType) {
        this.equalsType = equalsType;
    }

    public String getSecondColumnName() {
        return secondColumnName;
    }

    public void setSecondColumnName(String secondColumnName) {
        this.secondColumnName = secondColumnName;
    }

    public String getLinkType() {
        return linkType;
    }

    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }
}
