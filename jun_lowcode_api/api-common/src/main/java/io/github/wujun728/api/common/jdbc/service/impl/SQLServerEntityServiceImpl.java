package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.service.EntityService;
import io.github.wujun728.api.common.jdbc.factory.TableInfoFactory;
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
public class SQLServerEntityServiceImpl implements EntityService {

    @Override
    public EntityInfo getEntityInfo(Connector connector, String tableName) throws Exception {
        BaseDao baseDao = new BaseDao(connector);
        Connection con = baseDao.getConnection();

        ResultSet rs = null;
        PreparedStatement pstmt = null;

        String tableComment = TableInfoFactory.getTableService(connector.getDbType()).getTableComment(con,null,tableName);

        try {
            String sql = "SELECT a.column_id, a.name as COLUMN_NAME ,typ.name as TYPE_NAME , " +
                    " cast([value] as varchar(500))[value] FROM sys.columns a " +
                    " left join sys.extended_properties g on (a.object_id = g.major_id AND g.minor_id = a.column_id) " +
                    " left join sys.types typ on (a.system_type_id = typ.system_type_id) " +
                    " WHERE object_id = (SELECT object_id FROM sys.tables WHERE name = '" + tableName + "') order by a.column_id ";
            pstmt = con.prepareStatement(sql);
            ResultSet result = pstmt.executeQuery();
            return getEntityInfo(result,tableName,tableComment);
        } finally {
            baseDao.closeAll(con, pstmt, rs);
        }
    }

    /**
     * 获取entityInfo
     * @param resultSet
     * @param tableName
     * @param tableComment
     * @return
     * @throws Exception
     */
    private EntityInfo getEntityInfo(ResultSet resultSet, String tableName,String tableComment) throws Exception{
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
        List<EntityAttrInfo> entityAttrs = new ArrayList<>();

        int count = 1;
        boolean flag = false;
        while (resultSet.next()) {
            flag = true;
            EntityAttrInfo entityAttrInfo = new EntityAttrInfo();

            String colname = resultSet.getString("COLUMN_NAME");
            String dataType = resultSet.getString("TYPE_NAME");
            if (ObjectUtil.isNotEmpty(dataType)) {
                dataType = dataType.split(" ")[0];
            }

            String remark= resultSet.getString("remark");
            remark=remark==null?"":remark.replaceAll("\n"," ").replaceAll("\"","\\\"");

            String length = resultSet.getString("columlength");
            String scale = resultSet.getString("scale");

            String defaults = resultSet.getString("defaults");
            if(ObjectUtil.isEmpty(defaults)){
                defaults="";
            }
            String isnull = resultSet.getString("is_nullable");
            if("0".equals(isnull)){
                isnull="N";
            }else{
                isnull="Y";
            }

            String columnType=dataType+"("+length+")  ";
            if ("double".equals(dataType) || "money".equals(dataType)
                    || "decimal".equals(dataType) || "float".equals(dataType)
                    || "numeric".equals(dataType)) {
                columnType=dataType+"("+length+","+scale+") ";
            }else if("int".equals(dataType)
                    || "bigint".equals(dataType)
                    || "tinyint".equals(dataType)
                    || "smallint".equals(dataType)
                    || "bigint".equals(dataType)){
                columnType=dataType;
            }

            String attrName = StringUtil.toUnderscoreToCamelCase(colname);
            String attrType = getAttrType(dataType);

            if (count == 1) {
                primaryKey = attrName;
                tablePrimaryKey = colname;
                primaryKeyType = attrType;
                primaryKeyDataType = dataType;
                primaryKeyComment = remark;

               /* String is_identity = resultSet.getString("is_identity");
                if (!"1".equals(is_identity)) {
                    primaryKeyType = "String";
                } else {
                    primaryKeyType = "Integer";
                }*/
            }
            count++;

            entityAttrInfo.setAttrType(attrType);
            entityAttrInfo.setAttrName(attrName);
            entityAttrInfo.setColumnName(colname);
            entityAttrInfo.setColumnType(columnType);
            entityAttrInfo.setDataType(dataType);
            entityAttrInfo.setColumnComment(remark);
            entityAttrInfo.setColumnDefault(defaults);
            entityAttrInfo.setIsNullable(isnull);
            entityAttrs.add(entityAttrInfo);
        }

        if (flag == false) {
            throw new Exception("表" + entityInfo.getTableName() + "不存在");
        }

        entityInfo.setPrimaryKey(primaryKey);
        entityInfo.setPrimaryKeyType(primaryKeyType);
        entityInfo.setPrimaryKeyDataType(primaryKeyDataType);
        entityInfo.setTablePrimaryKey(tablePrimaryKey);
        entityInfo.setPrimaryKeyComment(primaryKeyComment);
        entityInfo.setEntityAttrs(entityAttrs);
        return entityInfo;
    }

    private String getAttrType(String dataType){
        String attrType = "";
        if ("int".equals(dataType)) {
            attrType="Integer";
        }else if ("bigint".equals(dataType)) {
            attrType = "Long";
        } else if ("varchar".equals(dataType)
                || "nvarchar".equals(dataType)
                || "datetime".equals(dataType) || "date".equals(dataType)
                || "char".equals(dataType) || "time".equals(dataType)) {
            attrType="String";
        } else if ("double".equals(dataType) || "money".equals(dataType)
                || "decimal".equals(dataType) || "float".equals(dataType)
                || "numeric".equals(dataType)) {
            attrType="Double";
        } else if ("image".equals(dataType)
                || "blob".equals(dataType)
                || "longblob".equals(dataType)
                || "binary".equals(dataType)
                || "varbinary".equals(dataType)) {
            attrType="byte[]";
        } else {
            if (!"sysname".equals(dataType)) {
                attrType="String";
            }
        }
        return attrType;
    }

}
