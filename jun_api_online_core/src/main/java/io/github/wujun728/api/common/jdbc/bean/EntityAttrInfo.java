package io.github.wujun728.api.common.jdbc.bean;

/**
 * 实体属性对象
 */
public class EntityAttrInfo extends ColumnInfo{

    /**
     * 属性名
     */
    private String attrName;

    /**
     * 属性类型
     */
    private String attrType;

    public EntityAttrInfo(String attrName, String attrType) {
        this.attrName = attrName;
        this.attrType = attrType;
    }

    public EntityAttrInfo() {
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
