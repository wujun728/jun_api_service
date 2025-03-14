package io.github.wujun728.api.core.util;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.vo.*;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.github.wujun728.api.core.vo.ParamVo;
import io.github.wujun728.api.core.vo.QueryColumnVo;
import io.github.wujun728.api.core.vo.SQLParamVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLParamUtil {


    public final static String [] SORT_NAMES = new String[]{"create_time","update_time","data_time"};

    /**
     * 处理查询语句参数
     *
     * @param apiInfo
     * @param params
     */
    public static void dearSqlParams(ApiInfo apiInfo, Map<String, Object> params) {
        if (StrUtil.isBlank(apiInfo.getSql())) {
            throw new BusinessException(EnumCode.PARAMS_ERROR, "参数错误");
        }

        //去除空格、制表符、换行符
        String sql = apiInfo.getSql().replaceAll("\\s+", " ");
        List<Object> sqlParams = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(apiInfo.getParamVos())) {
            List<ParamVo> paramVos = apiInfo.getParamVos().get(0).getParamVos();
            for (ParamVo paramVo : paramVos) {
                sql = sql.replaceAll("#\\{" + paramVo.getAttrName() + "}", "?")
                        .replaceAll("\\$\\{" + paramVo.getAttrName() + "}", "?");
                Object value = params.get(paramVo.getAttrName());
                sqlParams.add("".equals(value) ? null : value);
            }
        }
        apiInfo.setSql(sql);
        apiInfo.setSqlParams(sqlParams);
    }

    /**
     * 处理查询语句参数
     *
     * @param apiInfo
     * @param params
     */
    public static void dearSelectOnlySql(ApiInfo apiInfo, Map<String, Object> params) {

        if (StrUtil.isBlank(apiInfo.getSql())) {
            throw new BusinessException(EnumCode.PARAMS_ERROR, "参数错误");
        }

        List<Object> sqlParams = new ArrayList<>();
        //去除空格、制表符、换行符
        String sql = apiInfo.getSql().replaceAll("\\s+", " ");

        String limit = "";
        String where = "";
        String cond = "";
        String[] conds = null;
        if (sql.indexOf("#") != -1 || sql.indexOf("$") != -1) {
            if (sql.lastIndexOf("where") != -1) {
                where = "where";
                if (sql.lastIndexOf("limit") != -1) {
                    limit = sql.substring(sql.lastIndexOf("limit"));
                    cond = sql.substring(sql.lastIndexOf("where") + 5, sql.lastIndexOf("limit"));
                } else {
                    cond = sql.substring(sql.lastIndexOf("where") + 5);
                }
                conds = cond.split("and");
                sql = sql.substring(0, sql.lastIndexOf("where"));
            } else {
                if (sql.lastIndexOf("limit") != -1) {
                    limit = sql.substring(sql.lastIndexOf("limit"));
                    sql = sql.substring(0, sql.lastIndexOf("limit"));
                }
            }
            String condSql = "";
            if (conds != null && conds.length > 0) {
                for (String con : conds) {
                    if (ObjectUtil.isNotEmpty(apiInfo.getParamVos())) {
                        List<ParamVo> paramVos = apiInfo.getParamVos().get(0).getParamVos();
                        for (ParamVo paramVo : paramVos) {
                            if (con.indexOf("#{" + paramVo.getAttrName() + "}") != -1 || con.indexOf("${" + paramVo.getAttrName() + "}") != -1) {
                                Object value = params.get(paramVo.getAttrName());
                                if (ObjectUtil.isNotEmpty(value)) {
                                    condSql += con.replaceAll("#\\{" + paramVo.getAttrName() + "}", "?")
                                            .replaceAll("\\$\\{" + paramVo.getAttrName() + "}", "?") + " and ";
                                    sqlParams.add(value);
                                }
                            }
                        }
                    }
                }
                if (condSql.indexOf("and") != -1) {
                    condSql = condSql.substring(0, condSql.lastIndexOf("and"));
                }
            }
            if (StrUtil.isNotBlank(limit)) {
                if (ObjectUtil.isNotEmpty(apiInfo.getParamVos())) {
                    List<ParamVo> paramVos = apiInfo.getParamVos().get(0).getParamVos();
                    for (ParamVo paramVo : paramVos) {
                        if (limit.indexOf("#{" + paramVo.getAttrName() + "}") != -1 || limit.indexOf("${" + paramVo.getAttrName() + "}") != -1) {
                            Object value = params.get(paramVo.getAttrName());
                            if (ObjectUtil.isNotEmpty(value)) {
                                limit = limit.replaceAll("#\\{" + paramVo.getAttrName() + "}", "?")
                                        .replaceAll("\\$\\{" + paramVo.getAttrName() + "}", "?");
                                sqlParams.add(value);
                            }
                        }
                    }
                }
            }
            if (StrUtil.isNotBlank(condSql)) {
                sql += where + condSql + limit;
            } else {
                sql += limit;
            }
        }

        apiInfo.setSql(sql);
        apiInfo.setSqlParams(sqlParams);
    }

    /**
     * 处理查询语句参数
     *
     * @param apiInfo
     */
    public static void dearSelectParamBySingle(ApiInfo apiInfo, EntityInfo entityInfo) {
        List<SQLParamVo> sqlParamVos = new ArrayList<>();
        SQLParamVo sqlParamVo = new SQLParamVo();
        sqlParamVo.setTableComment(entityInfo.getTableComment());
        sqlParamVo.setTableName(entityInfo.getTableName());
        sqlParamVo.setMaster(true);
        sqlParamVo.setTableAlias("t");
        List<QueryColumnVo> queryColumns = new ArrayList<>();

        List<ParamVo> paramVos = new ArrayList<>();
        List<ParamVo> selectOneParamVos = new ArrayList<>();
        String sortName = "";
        for (EntityAttrInfo entityAttrInfo : entityInfo.getEntityAttrs()) {
            if(ListUtil.toList(SORT_NAMES).contains(entityAttrInfo.getColumnName().toLowerCase())){
                if(StrUtil.isBlank(sortName)){
                    sortName = entityAttrInfo.getColumnName().toLowerCase();
                }
            }
            boolean isPrimary = false;
            ParamVo paramVo = new ParamVo();
            if (entityAttrInfo.getColumnName().equals(entityInfo.getTablePrimaryKey())) {
                isPrimary = true;
                //主键 自增
                paramVo.setPrimaryKey(true);
                //批量参数
                ParamVo paramBatch = new ParamVo();
                paramBatch.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramBatch.setColumnComment(entityAttrInfo.getColumnComment() + "多个");
                paramBatch.setAttrName(entityAttrInfo.getAttrName() + "s");
                paramBatch.setAttrType("String");
                paramBatch.setPrimaryKey(true);
                paramBatch.setInEquals(true);
                paramBatch.setDataType("varchar");
                paramVos.add(paramBatch);

                //不等于
                ParamVo paramNotEquals = new ParamVo();
                paramNotEquals.setDataType(entityAttrInfo.getDataType());
                paramNotEquals.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramNotEquals.setColumnComment(entityAttrInfo.getColumnComment() + "不等于");
                paramNotEquals.setAttrName("no" + StringUtil.toUpperCaseFirstOne(entityAttrInfo.getAttrName()));
                paramNotEquals.setAttrType(entityAttrInfo.getAttrType());
                paramNotEquals.setPrimaryKey(true);
                paramNotEquals.setNotEquals(true);
                paramVos.add(paramNotEquals);

            } else {
                paramVo.setPrimaryKey(false);
            }

            paramVo.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
            paramVo.setColumnComment(entityAttrInfo.getColumnComment());
            paramVo.setDataType(entityAttrInfo.getDataType());
            paramVo.setAttrName(entityAttrInfo.getAttrName());
            paramVo.setAttrType(entityAttrInfo.getAttrType());
            paramVo.setColumnDefault(entityAttrInfo.getColumnDefault());
            paramVos.add(paramVo);

            if(paramVo.getPrimaryKey()!=null && paramVo.getPrimaryKey()==true){
                selectOneParamVos.add(paramVo);
            }

            //模糊查询
            for (String key : SqlUtl.SQL_LIKE_NAME_ARRAY) {
                if (entityAttrInfo.getColumnName().toLowerCase().indexOf(key) != -1) {
                    //LIKE
                    ParamVo paramLike = new ParamVo();
                    paramLike.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                    paramLike.setColumnComment(entityAttrInfo.getColumnComment() + "(模糊查询)");
                    paramLike.setAttrName(entityAttrInfo.getAttrName() + "Like");
                    paramLike.setAttrType("String");
                    paramLike.setDataType(entityAttrInfo.getDataType());
                    paramLike.setLikeEquals(true);
                    paramVos.add(paramLike);
                }
            }

            //大于小于
            if ("date".equalsIgnoreCase(entityAttrInfo.getDataType())) {
                ParamVo paramGreaterEquals = new ParamVo();
                paramGreaterEquals.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramGreaterEquals.setColumnComment(entityAttrInfo.getColumnComment() + "-开始（yyyy-MM-dd）");
                paramGreaterEquals.setAttrName(entityAttrInfo.getAttrName() + "Start");
                paramGreaterEquals.setAttrType("String");
                paramGreaterEquals.setDataType(entityAttrInfo.getDataType());
                paramGreaterEquals.setGreaterEquals(true);
                paramVos.add(paramGreaterEquals);

                ParamVo paramLessEquals = new ParamVo();
                paramLessEquals.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramLessEquals.setColumnComment(entityAttrInfo.getColumnComment() + "-截止（yyyy-MM-dd）");
                paramLessEquals.setAttrName(entityAttrInfo.getAttrName() + "End");
                paramLessEquals.setAttrType("String");
                paramLessEquals.setDataType(entityAttrInfo.getDataType());
                paramLessEquals.setLessEquals(true);
                paramVos.add(paramLessEquals);
            } else if ("datetime".equalsIgnoreCase(entityAttrInfo.getDataType())) {
                ParamVo paramGreaterEquals = new ParamVo();
                paramGreaterEquals.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramGreaterEquals.setColumnComment(entityAttrInfo.getColumnComment() + "-开始（yyyy-MM-dd HH:mm:ss）");
                paramGreaterEquals.setAttrName(entityAttrInfo.getAttrName() + "Start");
                paramGreaterEquals.setAttrType("String");
                paramGreaterEquals.setDataType(entityAttrInfo.getDataType());
                paramGreaterEquals.setGreaterEquals(true);
                paramVos.add(paramGreaterEquals);

                ParamVo paramLessEquals = new ParamVo();
                paramLessEquals.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramLessEquals.setColumnComment(entityAttrInfo.getColumnComment() + "-截止（yyyy-MM-dd HH:mm:ss）");
                paramLessEquals.setAttrName(entityAttrInfo.getAttrName() + "End");
                paramLessEquals.setAttrType("String");
                paramLessEquals.setDataType(entityAttrInfo.getDataType());
                paramLessEquals.setLessEquals(true);
                paramVos.add(paramLessEquals);
            }

            QueryColumnVo queryColumnVo = new QueryColumnVo();
            queryColumnVo.setColumnName(entityAttrInfo.getColumnName());
            queryColumnVo.setColumnComment(entityAttrInfo.getColumnComment());
            queryColumnVo.setAliasName(null);
            queryColumnVo.setAttrName(entityAttrInfo.getAttrName());
            queryColumnVo.setAttrType(entityAttrInfo.getAttrType());
            queryColumnVo.setPrimaryKey(isPrimary);
            queryColumns.add(queryColumnVo);
        }

        if (SqlUtl.OP_TYPE_SELECT.equals(apiInfo.getOpType())) {
            //加上 排序分页条件
            ParamVo sorted = new ParamVo();
            sorted.setColumnName(SqlUtl.SQL_KEY_ORDER);
            sorted.setColumnComment("排序列名");
            sorted.setAttrName("sortName");
            sorted.setAttrType("String");
            sorted.setSorted(true);
            sorted.setColumnDefault(sortName);
            paramVos.add(sorted);

            ParamVo order = new ParamVo();
            order.setColumnName(" ");
            order.setColumnComment("排序顺序（DESC/ASC）");
            order.setAttrName("sortOrder");
            order.setAttrType("String");
            order.setOrder(true);
            order.setColumnDefault("DESC");
            paramVos.add(order);

            ParamVo limitStart = new ParamVo();
            limitStart.setColumnName(SqlUtl.SQL_KEY_LIMIT);
            limitStart.setColumnComment("分页条件 第几页");
            limitStart.setAttrName("pageIndex");
            limitStart.setAttrType("Integer");
            limitStart.setLimitStart(true);
            limitStart.setColumnDefault("1");
            paramVos.add(limitStart);

            ParamVo limit = new ParamVo();
            limit.setColumnName(", ");
            limit.setColumnComment("分页条件 每页条数");
            limit.setAttrName("pageSize");
            limitStart.setAttrType("Integer");
            limitStart.setColumnDefault("10");
            limit.setLimitSize(true);
            paramVos.add(limit);
        }

        sqlParamVo.setQueryColumns(queryColumns);
        if (SqlUtl.OP_TYPE_SELECT_ONE.equals(apiInfo.getOpType())) {
            sqlParamVo.setParamVos(selectOneParamVos);
        }else{
            sqlParamVo.setParamVos(paramVos);
        }
        sqlParamVos.add(sqlParamVo);
        //参数
        apiInfo.setParamVos(sqlParamVos);
        apiInfo.setSql(null);
    }

    /**
     * 处理查询语句参数
     *
     * @param apiInfo
     */
    public static void dearSelectParamByParams(ApiInfo apiInfo) {
        if (ObjectUtil.isEmpty(apiInfo.getParamVos())) {
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }

        List<SQLParamVo> sqlParamVos = apiInfo.getParamVos().stream().filter(e -> e.getMaster() != null && e.getMaster() == true).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(sqlParamVos)) {
            List<ParamVo> paramVos = sqlParamVos.get(0).getParamVos();
            if (ObjectUtil.isEmpty(paramVos)) {
                paramVos = new ArrayList<>();
            }

            //加上 排序分页条件
            ParamVo sorted = new ParamVo();
            sorted.setColumnName(SqlUtl.SQL_KEY_ORDER);
            sorted.setColumnComment("排序列名");
            sorted.setAttrName("sortName");
            sorted.setAttrType("String");
            sorted.setSorted(true);
            paramVos.add(sorted);

            ParamVo order = new ParamVo();
            order.setColumnName(" ");
            order.setColumnComment("排序顺序（DESC/ASC）");
            order.setAttrName("sortOrder");
            order.setAttrType("String");
            order.setOrder(true);
            paramVos.add(order);

            if (ConstantUtil.IS_VALID_Y.equals(apiInfo.getPageFlag())) {
                ParamVo limitStart = new ParamVo();
                limitStart.setColumnName(SqlUtl.SQL_KEY_LIMIT);
                limitStart.setColumnComment("分页条件 第几页");
                limitStart.setAttrName("pageIndex");
                limitStart.setAttrType("Integer");
                limitStart.setLimitStart(true);
                paramVos.add(limitStart);

                ParamVo limit = new ParamVo();
                limit.setColumnName(", ");
                limit.setColumnComment("分页条件 每页条数");
                limit.setAttrName("pageSize");
                limitStart.setAttrType("Integer");
                limit.setLimitSize(true);
                paramVos.add(limit);
            }
            sqlParamVos.get(0).setParamVos(paramVos);
            apiInfo.setParamVos(sqlParamVos);
        }
    }
}
