package io.github.wujun728.api.common.jdbc.service.impl;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
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
public class DB2EntityServiceImpl implements EntityService {


    @Override
    public EntityInfo getEntityInfo(Connector connector, String tableName) throws Exception {
        BaseDao baseDao = new BaseDao(connector);
        Connection con = baseDao.getConnection();

        ResultSet rs = null;
        PreparedStatement pstmt = null;
        try {
            if (con == null) {
                System.out.println("数据库连接失败");
                return null;
            }
            String sql = "select * from sysibm.systables where type='T' and creator='" + connector.getSchema() + "'";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            boolean flag = false;
            while (rs.next()) {
                if (tableName.equals(rs.getString("name"))) {
                    flag = true;
                    String comment = rs.getString("remarks");
                    String tableComment = comment==null?"":comment;

                    sql = "select * from syscat.columns where tabschema = '" + connector.getSchema() + "' and tabname = upper('" + tableName + "') order by colno ";
                    pstmt = con.prepareStatement(sql);
                    ResultSet result = pstmt.executeQuery();
                    return getEntityInfo(result,tableName,tableComment);
                }
            }
            if (flag == false) {
                throw new Exception("表" + tableName + "不存在");
            }

        } finally {
            baseDao.closeAll(con, pstmt, rs);
        }
        return null;
    }

    /**
     * 获取entityInfo
     *
     * @param resultSet
     * @param tableName
     * @param tableComment
     * @return
     * @throws Exception
     */
    private EntityInfo getEntityInfo(ResultSet resultSet, String tableName, String tableComment) throws Exception{
        EntityInfo entityInfo = new EntityInfo();
        entityInfo.setTableName(tableName);

        if (tableComment != null && !"".equals(tableComment)) {
            if (tableComment.substring(tableComment.length() - 1).equals("表")) {
                tableComment = tableComment.substring(0, tableComment.length() - 1);
            }
        }
        entityInfo.setTableComment(tableComment);

        String primaryKey = "";
        String primaryKeyType = "";
        String primaryKeyDataType = "";
        String tablePrimaryKey = "";
        String primaryKeyComment = "";
        List<EntityAttrInfo> entityAttrs = new ArrayList<>();

        boolean flag = false;
        int count = 1;
        while (resultSet.next()) {
            flag =true;
            EntityAttrInfo entityAttrInfo = new EntityAttrInfo();
            String colname = resultSet.getString("COLNAME");
            String attrName = StringUtil.toUnderscoreToCamelCase(colname);
            String type_name = resultSet.getString("TYPENAME");
            String dataType=type_name.toLowerCase();
            String remark= resultSet.getString("REMARKS");
            remark = remark==null?"":remark.replaceAll("\n"," ").replaceAll("\"","\\\"");
            String length = resultSet.getString("LENGTH");
            String scale = resultSet.getString("SCALE");
            String defaults = resultSet.getString("DEFAULT");
            if(ObjectUtil.isEmpty(defaults)){
                defaults="";
            }
            String isnull = resultSet.getString("NULLS");
            String columnType=dataType+"("+length+")   ";
            if("integer".equals(dataType)
                    || "bigint".equals(dataType)){
                columnType=dataType;
            } else if ("double".equals(dataType) || "money".equals(dataType)
                    || "decimal".equals(dataType) || "float".equals(dataType)
                    || "numeric".equals(dataType)) {
                columnType=dataType+"("+length+","+scale+")   ";
            }

            String attrType = getAttrType(dataType.toLowerCase());
            if (count == 1) {
                primaryKey = attrName;
                tablePrimaryKey = colname;
                primaryKeyType = attrType;
                primaryKeyDataType = dataType;
                primaryKeyComment = remark;
                /*String identity = resultSet.getString("IDENTITY");
                if (!"Y".equals(identity)) {
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
        entityInfo.setTablePrimaryKey(tablePrimaryKey);
        entityInfo.setPrimaryKeyDataType(primaryKeyDataType);
        entityInfo.setPrimaryKeyComment(primaryKeyComment);
        entityInfo.setEntityAttrs(entityAttrs);

        return entityInfo;
    }

    private String getAttrType(String dataType){
        String attrType = "";
        if ("integer".equals(dataType)) {
            attrType = "Integer";
        }else if ("bigint".equals(dataType)) {
            attrType = "Long";
        } else if ("varchar".equals(dataType)
                || "nvarchar".equals(dataType)
                || "datetime".equals(dataType) || "date".equals(dataType)
                || "char".equals(dataType) || "time".equals(dataType)) {
            attrType = "String";
        } else if ("double".equals(dataType) || "money".equals(dataType)
                || "decimal".equals(dataType) || "float".equals(dataType)
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
