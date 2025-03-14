package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.factory.TableInfoFactory;
import io.github.wujun728.api.common.jdbc.service.EntityService;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import cn.hutool.core.util.ObjectUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @date 2019-07-06
 **/
public class MySQLEntityServiceImpl implements EntityService {


    /**
     * 获取entityInfo
     * @param connector
     * @param tableName
     * @return
     * @throws Exception
     */
    @Override
    public EntityInfo getEntityInfo(Connector connector, String tableName) throws Exception{

        BaseDao baseDao = new BaseDao(connector);
        Connection con = baseDao.getConnection();
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            if (con == null) {
                System.out.println("数据库连接失败");
                return null;
            }
            String schema = con.getCatalog();

            String tableComment = TableInfoFactory.getTableService(connector.getDbType()).getTableComment(con,schema,tableName);

            String sql = "select * from information_schema.columns where " +
                    " table_schema = '" + schema + "' and table_name = '" + tableName + "' ORDER BY ORDINAL_POSITION ";
            pstmt = con.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();

            return getEntityInfo(result,tableName,tableComment);
        } finally {
            baseDao.closeAll(con, pstmt, rs);
        }
    }

    /**
     * 获取 EntityInfo
     * @param resultSet
     * @param tableName
     * @param tableComment
     * @return
     * @throws Exception
     */
    private EntityInfo getEntityInfo(ResultSet resultSet, String tableName, String tableComment) throws Exception{
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setTableName(tableName);

        if (ObjectUtil.isNotEmpty(tableComment)) {
            String first = tableComment.substring(tableComment.length() - 1);
            if (first.equals("表")) {
                tableComment = tableComment.substring(0, tableComment.length() - 1);
            }
        }
        entityInfo.setTableComment(tableComment);

        String primaryKey = "";
        String tablePrimaryKey = "";
        String primaryKeyType = "";
        String primaryKeyDataType = "";
        String primaryKeyComment = "";
        boolean autoIncrement = false;
        List<EntityAttrInfo> entityAttrs = new ArrayList<>();

        boolean flag = false;
        while (resultSet.next()) {
            flag = true;
            EntityAttrInfo entityAttrInfo = new EntityAttrInfo();

            String columnType = resultSet.getString("COLUMN_TYPE");
            if(!"sysname".equals(columnType)){
                String column_key = resultSet.getString("COLUMN_KEY");
                String colname = resultSet.getString("COLUMN_NAME");
                String lowerColname = StringUtil.toUnderscoreToCamelCase(colname);
                String dataType = resultSet.getString("DATA_TYPE");
                String comment = resultSet.getString("COLUMN_COMMENT");
                String extra = resultSet.getString("EXTRA");
                comment = comment==null?"":comment.replaceAll("\r\n"," ").replaceAll("\"","\\\"");
                String attrType = getAttrType(dataType.toLowerCase());

                String defaults = resultSet.getString("COLUMN_DEFAULT");
                String isnull = resultSet.getString("IS_NULLABLE");

                if ("PRI".equals(column_key)) {
                    entityAttrInfo.setPrimaryKey(true);
                    primaryKey = lowerColname;
                    tablePrimaryKey = colname;
                    primaryKeyType = attrType;
                    primaryKeyDataType = dataType;
                    primaryKeyComment = comment;
                }

                if("auto_increment".equals(extra)){
                    autoIncrement = true;
                }

                entityAttrInfo.setExtra(extra);
                entityAttrInfo.setAttrType(attrType);
                entityAttrInfo.setAttrName(lowerColname);
                entityAttrInfo.setColumnName(colname);
                entityAttrInfo.setColumnType(columnType);
                entityAttrInfo.setDataType(dataType);
                entityAttrInfo.setColumnComment(comment);
                entityAttrInfo.setColumnDefault(defaults);
                entityAttrInfo.setIsNullable(isnull);
                entityAttrs.add(entityAttrInfo);
            }
        }

        if (flag == false) {
            throw new Exception("表" + tableName + "不存在");
        }

        entityInfo.setAutoIncrement(autoIncrement);
        entityInfo.setPrimaryKey(primaryKey);
        entityInfo.setPrimaryKeyType(primaryKeyType);
        entityInfo.setPrimaryKeyDataType(primaryKeyDataType);
        entityInfo.setTablePrimaryKey(tablePrimaryKey);
        entityInfo.setPrimaryKeyComment(primaryKeyComment);
        entityInfo.setEntityAttrs(entityAttrs);
        return entityInfo;
    }

    /**
     * 获取属性类型
     * @param dataType
     * @return
     */
    private String getAttrType(String dataType){
        String attrType = "";
        if ("int".equals(dataType)
                || "tinyint".equals(dataType) || "integer".equals(dataType)) {
            attrType = "Integer";
        }else if ("bigint".equals(dataType)) {
            attrType = "Long";
        } else if ("varchar".equals(dataType) || "nvarchar".equals(dataType)
                || "datetime".equals(dataType) || "date".equals(dataType)
                || "char".equals(dataType) || "time".equals(dataType)
                || "text".equals(dataType)) {
            attrType = "String";
        } else if ("double".equals(dataType)
                || "money".equals(dataType)
                || "decimal".equals(dataType)
                || "float".equals(dataType)
                || "numeric".equals(dataType)) {
            attrType = "Double";
        } else if ("image".equals(dataType)
                || "blob".equals(dataType)
                || "longblob".equals(dataType)
                || "binary".equals(dataType)
                || "varbinary".equals(dataType)) {
            attrType = "byte[]";
        } else {
            if (!"sysname".equals(dataType)) {
                attrType = "String";
            }
        }
        return attrType;
    }

}
