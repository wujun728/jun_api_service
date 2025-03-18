package io.github.wujun728.api.core.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.factory.EntityServiceFactory;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.manager.SQLInitManager;
import io.github.wujun728.api.core.mapper.DataSourceMapper;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.util.SQLParamUtil;
import io.github.wujun728.api.core.vo.*;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import io.github.wujun728.api.core.vo.ParamVo;
import io.github.wujun728.api.core.vo.SQLParamVo;
import io.github.wujun728.api.core.vo.UnionColumnVo;
import io.github.wujun728.api.core.vo.UnionParamVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 支持SQL 查询
 */
@Service
public class SQLCountQueryParamServiceImpl implements ApiParamService {

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private SQLInitManager sqlInitManager;

    @Override
    public void dearApiParam(ApiInfo apiInfo) throws Exception {
        if (ObjectUtil.isNotEmpty(apiInfo.getParamVos())) {
            SQLParamUtil.dearSelectParamByParams(apiInfo);
        } else if (StrUtil.isNotBlank(apiInfo.getSql())) {
            //SQL参数
            List<String> list = ReUtil.findAllGroup0(SqlUtl.REGEX_KUOHAO, apiInfo.getSql());
            if (ObjectUtil.isNotEmpty(list)) {
                List<SQLParamVo> sqlParamVos = new ArrayList<>();
                SQLParamVo sqlParamVo = new SQLParamVo();
                List<ParamVo> paramVos = new ArrayList<>();
                for (String attrName : list) {
                    ParamVo paramVo = new ParamVo();
                    paramVo.setAttrName(attrName);
                    paramVo.setAttrType("String");
                    paramVos.add(paramVo);
                }
                sqlParamVo.setParamVos(paramVos);
                sqlParamVos.add(sqlParamVo);
                //参数
                apiInfo.setParamVos(sqlParamVos);
            }
        } else {
            if (StrUtil.isBlank(apiInfo.getTableName())) {
                //操作表不能为空
                throw new BusinessException(EnumCode.PARAMS_ERROR, "操作表不能为空");
            }
            DataSource dataSource = null;
            if (apiInfo.getSourceId() == null) {
                dataSource = sqlInitManager.getDefaultDatasource();
                apiInfo.setDatabaseName(dataSource.getSchema());
            } else {
                dataSource = dataSourceMapper.selectById(apiInfo.getSourceId());
            }
            if (dataSource == null) {
                throw new BusinessException(EnumCode.NO_DATA_FOUND, "未查到数据源");
            }

            Connector connector = new Connector();
            connector.setSchema(dataSource.getSchema());
            connector.setDbType(dataSource.getDbType());
            connector.setDriver(dataSource.getDriver());
            connector.setUrl(dataSource.getRealUrl(apiInfo.getDatabaseName()));
            connector.setUser(dataSource.getUserName());
            connector.setPassword(dataSource.getPassword());

            EntityInfo entityInfo = EntityServiceFactory.getEntityService(connector.getDbType()).getEntityInfo(connector, apiInfo.getTableName());
            if (entityInfo == null || ObjectUtil.isEmpty(entityInfo.getEntityAttrs())) {
                throw new BusinessException(EnumCode.DATA_EXCEPTION, "数据库查询失败");
            }

            if (StrUtil.isBlank(entityInfo.getPrimaryKey())) {
                EntityAttrInfo entityAttrInfo = entityInfo.getEntityAttrs().get(0);
                entityInfo.setTablePrimaryKey(entityAttrInfo.getColumnName());
                entityInfo.setPrimaryKey(entityAttrInfo.getAttrName());
                entityInfo.setPrimaryKeyType(entityAttrInfo.getAttrType());
                entityInfo.setPrimaryKeyDataType(entityAttrInfo.getDataType());
                entityInfo.setPrimaryKeyComment(entityAttrInfo.getColumnComment());
            }
            SQLParamUtil.dearSelectParamBySingle(apiInfo, entityInfo);
        }
    }

    @Override
    public void dearApiSQL(ApiInfo apiInfo, Map<String, Object> params) throws Exception {

        if(ObjectUtil.isNotEmpty(params)) {
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                if (ObjectUtil.isEmpty(entry.getValue())) {
                    it.remove();
                }
            }
        }

        if (StrUtil.isNotBlank(apiInfo.getSql())) {
            SQLParamUtil.dearSelectOnlySql(apiInfo, params);
            return;
        }

        if (ObjectUtil.isEmpty(apiInfo.getParamVos())) {
            return;
        }

        List<Object> sqlParams = new ArrayList<>();
        StringBuffer values = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        sql.append("select count(1) as total ");
        //查询主表
        List<SQLParamVo> sqlParamVos = apiInfo.getParamVos().stream().filter(e -> e.getMaster() != null && e.getMaster() == true).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(sqlParamVos)) {
            String alias = StrUtil.isNotBlank(sqlParamVos.get(0).getTableAlias()) ? sqlParamVos.get(0).getTableAlias() : "t0";
            sql.append(" from " + sqlParamVos.get(0).getTableName() + " " + alias + " ");
        } else {
            SQLParamVo sqlParamVo = apiInfo.getParamVos().get(0);
            String alias = StrUtil.isNotBlank(sqlParamVo.getTableAlias()) ? sqlParamVo.getTableAlias() : "t0";
            sql.append(" from " + sqlParamVo.getTableName() + " " + alias + " ");
        }

        //关联关系
        for (int i = 0; i < apiInfo.getParamVos().size(); i++) {
            SQLParamVo sqlParamVo = apiInfo.getParamVos().get(i);
            String alias = StrUtil.isNotBlank(sqlParamVo.getTableAlias()) ? sqlParamVo.getTableAlias() : "t" + i;
            if (ObjectUtil.isNotEmpty(sqlParamVo.getUnionParamVos())) {
                List<UnionParamVo> unionParamVos = sqlParamVo.getUnionParamVos();
                for (UnionParamVo unionParamVo : unionParamVos) {
                    sql.append(" " + unionParamVo.getJoinType() + " " + unionParamVo.getLinkTableName() + " " + unionParamVo.getLinkTableAlias() + " on ");
                    String joins = "";
                    String linkType = "";
                    for (UnionColumnVo unionColumnVo : unionParamVo.getUnionColumns()) {
                        if (StrUtil.isBlank(unionColumnVo.getColumnName()) && StrUtil.isBlank(unionColumnVo.getSecondColumnName())) {
                            linkType = "and";
                            joins += " 1 = 1 " + linkType;
                        } else {
                            linkType = unionColumnVo.getLinkType();
                            joins += alias + "." + SqlUtl.getSQLColumn(unionColumnVo.getColumnName()) + " " + unionColumnVo.getEqualsType() + " " + unionParamVo.getLinkTableAlias() + "." + SqlUtl.getSQLColumn(unionColumnVo.getSecondColumnName()) + " " + linkType;
                        }
                    }
                    if (StrUtil.isNotBlank(joins) && StrUtil.isNotBlank(linkType)) {
                        joins = joins.substring(0, joins.lastIndexOf(linkType));
                    }
                    sql.append(" " + joins + " ");
                }
            }
        }

        //处理参数
        if (ObjectUtil.isNotEmpty(params) && ObjectUtil.isNotEmpty(apiInfo.getParamVos())) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                for (int i = 0; i < apiInfo.getParamVos().size(); i++) {
                    SQLParamVo sqlParamVo = apiInfo.getParamVos().get(i);
                    String alias = StrUtil.isNotBlank(sqlParamVo.getTableAlias()) ? sqlParamVo.getTableAlias() : "t" + i;
                    if (ObjectUtil.isNotEmpty(sqlParamVo.getParamVos())) {
                        List<ParamVo> paramList = sqlParamVo.getParamVos().stream().filter(e -> e.getAttrName().equals(entry.getKey())).collect(Collectors.toList());
                        if (ObjectUtil.isNotEmpty(paramList)) {
                            ParamVo paramVo = paramList.get(0);
                            Object value = entry.getValue();
                            if (paramVo.getInEquals() != null && paramVo.getInEquals()) {
                                if (entry.getValue() instanceof List) {
                                    List list = (List) entry.getValue();
                                    value = StringUtil.array2Char(list);
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                                } else if (entry.getValue() instanceof String[]) {
                                    String[] array = (String[]) entry.getValue();
                                    value = StringUtil.array2CharStr(array);
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                                } else {
                                    value = StringUtil.getValidNullStr(value);
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ? and ");
                                    sqlParams.add(value);
                                }
                            } else if (paramVo.getLikeEquals() != null && paramVo.getLikeEquals()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " like ? and ");
                                sqlParams.add("%" + StringUtil.getValidNullStr(value) + "%");
                            } else if (paramVo.getLess() != null && paramVo.getLess()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " < ? and ");
                                sqlParams.add(StringUtil.getValidNullStr(value));
                            } else if (paramVo.getGreater() != null && paramVo.getGreater()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " > ? and ");
                                sqlParams.add(StringUtil.getValidNullStr(value));
                            } else if (paramVo.getGreaterEquals() != null && paramVo.getGreaterEquals()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " >= ? and ");
                                sqlParams.add(StringUtil.getValidNullStr(value));
                            } else if (paramVo.getLessEquals() != null && paramVo.getLessEquals()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " <= ? and ");
                                sqlParams.add(StringUtil.getValidNullStr(value));
                            } else if (paramVo.getNotEquals() != null && paramVo.getNotEquals()) {
                                values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " != ? and ");
                                sqlParams.add(StringUtil.getValidNullStr(value));
                            } else if (paramVo.getSorted() != null && paramVo.getSorted() && ObjectUtil.isNotEmpty(value)) {

                            } else if (paramVo.getOrder() != null && paramVo.getOrder() && ObjectUtil.isNotEmpty(value)) {

                            } else if (paramVo.getLimitStart() != null && paramVo.getLimitStart() && ObjectUtil.isNotEmpty(value)) {

                            } else if (paramVo.getLimitSize() != null && paramVo.getLimitSize() && ObjectUtil.isNotEmpty(value)) {

                            } else {
                                if (entry.getValue() instanceof List) {
                                    List list = (List) entry.getValue();
                                    value = StringUtil.array2Char(list);
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                                } else if (entry.getValue() instanceof String[]) {
                                    String[] array = (String[]) entry.getValue();
                                    value = StringUtil.array2CharStr(array);
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                                } else {
                                    values.append(alias + "." + SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ? and ");
                                    sqlParams.add(StringUtil.getValidNullStr(value));
                                }
                            }
                        }
                    }
                }
            }
        }

        String valueSQL = values.toString();
        if (StrUtil.isNotBlank(valueSQL)) {
            valueSQL = valueSQL.substring(0, valueSQL.lastIndexOf("and"));
            sql.append(" where " + valueSQL);
        }
        if (StrUtil.isBlank(sql.toString())) {
            throw new BusinessException(EnumCode.PARAMS_ERROR);
        }
        apiInfo.setSql(sql.toString());
        apiInfo.setSqlParams(sqlParams);
    }
}
