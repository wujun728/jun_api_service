package io.github.wujun728.api.common.jdbc.bean;

/**
 * 数据库每列属性
 */
public class ColumnInfo {

    /**
     * 列名
     */
    private String columnName;

    /**
     * 是否主键
     */
    private boolean isPrimaryKey;

    /**
     * 类型 varchar(32)
     */
    private String columnType;

    /**
     * 类型 varchar int
     */
    private String dataType;

    /**
     * 注释
     */
    private String columnComment;

    /**
     * 默认值
     */
    private String columnDefault;

    /**
     * 是否为空 NO 否 YES 是
     */
    private String isNullable;

    /**
     * 是否自增 mysql auto_increment
     */
    private String extra;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }
}
