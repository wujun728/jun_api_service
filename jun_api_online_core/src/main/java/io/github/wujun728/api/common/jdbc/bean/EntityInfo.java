package io.github.wujun728.api.common.jdbc.bean;

import io.github.wujun728.api.common.jdbc.util.StringUtil;

import java.util.List;

/**
 * @version 1.0
 * @date 2019-07-06
 **/
public class EntityInfo {

    /**
     * 实体名
     */
    private String entityName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 表注释
     */
    private String tableComment;

    /**
     * 实体中的主键名
     */
    private String primaryKey;

    /**
     * 表中的主键注释
     */
    private String primaryKeyComment;

    /**
     * 数据库表主键名
     */
    private String tablePrimaryKey;

    /**
     * 实体中的主键类型
     */
    private String primaryKeyType;

    /**
     * 表中的主键类型
     */
    private String primaryKeyDataType;

    /**
     * 实体中的主键是否自增 auto_increment
     */
    private boolean autoIncrement;

    /**
     * 属性集合
     */
    private List<EntityAttrInfo> entityAttrs;

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getPrimaryKeyDataType() {
        return primaryKeyDataType;
    }

    public void setPrimaryKeyDataType(String primaryKeyDataType) {
        this.primaryKeyDataType = primaryKeyDataType;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public List<EntityAttrInfo> getEntityAttrs() {
        return entityAttrs;
    }

    public void setEntityAttrs(List<EntityAttrInfo> entityAttrs) {
        this.entityAttrs = entityAttrs;
    }

    public String getTablePrimaryKey() {
        return tablePrimaryKey;
    }

    public void setTablePrimaryKey(String tablePrimaryKey) {
        this.tablePrimaryKey = tablePrimaryKey;
    }

    public String getEntityName() {
        entityName =  StringUtil.toUpperCaseFirstOne(StringUtil.toUnderscoreToCamelCase(tableName));
        return entityName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getPrimaryKeyType() {
        return primaryKeyType;
    }

    public void setPrimaryKeyType(String primaryKeyType) {
        this.primaryKeyType = primaryKeyType;
    }

    public String getPrimaryKeyComment() {
        return primaryKeyComment;
    }

    public void setPrimaryKeyComment(String primaryKeyComment) {
        this.primaryKeyComment = primaryKeyComment;
    }
}
