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
import io.github.wujun728.api.core.vo.ParamVo;
import io.github.wujun728.api.core.vo.SQLParamVo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SQLDeleteParamServiceImpl implements ApiParamService {

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private SQLInitManager sqlInitManager;

    @Override
    public void dearApiParam(ApiInfo apiInfo) throws Exception {
        if (StrUtil.isNotBlank(apiInfo.getSql())) {
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
            //根据表处理
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
            List<ParamVo> paramVos = new ArrayList<>();
            for (EntityAttrInfo entityAttrInfo : entityInfo.getEntityAttrs()) {
                ParamVo paramVo = new ParamVo();
                if (entityAttrInfo.getColumnName().equals(entityInfo.getTablePrimaryKey())) {
                    //主键 自增
                    paramVo.setPrimaryKey(true);

                    //批量删除参数
                    ParamVo paramBatch = new ParamVo();
                    paramBatch.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                    paramBatch.setColumnComment(entityAttrInfo.getColumnComment() + "多个");
                    paramBatch.setAttrName(entityAttrInfo.getAttrName() + "s");
                    paramBatch.setAttrType("String");
                    paramBatch.setDataType("varchar");
                    paramBatch.setPrimaryKey(true);
                    paramBatch.setInEquals(true);
                    paramVos.add(paramBatch);
                } else {
                    paramVo.setPrimaryKey(false);
                }

                paramVo.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramVo.setColumnComment(entityAttrInfo.getColumnComment());
                paramVo.setAttrName(entityAttrInfo.getAttrName());
                paramVo.setAttrType(entityAttrInfo.getAttrType());
                paramVo.setDataType(entityAttrInfo.getDataType());
                paramVo.setColumnDefault(entityAttrInfo.getColumnDefault());
                paramVos.add(paramVo);
            }

            //参数
            List<SQLParamVo> paramVoList = new ArrayList<>();
            SQLParamVo sqlParamVo = new SQLParamVo();
            sqlParamVo.setParamVos(paramVos);
            sqlParamVo.setTableComment(entityInfo.getTableComment());
            sqlParamVo.setTableName(entityInfo.getTableName());
            sqlParamVo.setMaster(true);
            paramVoList.add(sqlParamVo);
            apiInfo.setParamVos(paramVoList);
            apiInfo.setSql(null);
        }
    }

    @Override
    public void dearApiSQL(ApiInfo apiInfo, Map<String, Object> params) throws Exception {

        if (StrUtil.isNotBlank(apiInfo.getSql())) {
            SQLParamUtil.dearSqlParams(apiInfo, params);
            return;
        }

        List<Object> sqlParams = new ArrayList<>();
        StringBuffer values = new StringBuffer();
        if (ObjectUtil.isNotEmpty(params) && ObjectUtil.isNotEmpty(apiInfo.getParams())) {
            List<SQLParamVo> sqlParamVos = JSON.parseArray(apiInfo.getParams(), SQLParamVo.class);
            List<ParamVo> paramVos = sqlParamVos.get(0).getParamVos();
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                List<ParamVo> paramList = paramVos.stream().filter(e -> e.getAttrName().equals(entry.getKey())).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(paramList)) {
                    ParamVo paramVo = paramList.get(0);
                    Object value = entry.getValue();
                    if (ObjectUtil.isEmpty(value)) {
                        continue;
                    }
                    if (paramVo.getInEquals() != null && paramVo.getInEquals()) {
                        if (entry.getValue() instanceof List) {
                            List list = (List) entry.getValue();
                            value = StringUtil.array2Char(list);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                        } else if (entry.getValue() instanceof String[]) {
                            String[] array = (String[]) entry.getValue();
                            value = StringUtil.array2CharStr(array);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                        } else {
                            value = StringUtil.getValidNullStr(value);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ? and ");
                            sqlParams.add(value);
                        }
                    } else if (paramVo.getLikeEquals() != null && paramVo.getLikeEquals()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " like ? and ");
                        sqlParams.add("%" + StringUtil.getValidNullStr(value) + "%");
                    } else if (paramVo.getLess() != null && paramVo.getLess()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " < ? and ");
                        sqlParams.add(StringUtil.getValidNullStr(value));
                    } else if (paramVo.getGreater() != null && paramVo.getGreater()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " > ? and ");
                        sqlParams.add(StringUtil.getValidNullStr(value));
                    } else if (paramVo.getGreaterEquals() != null && paramVo.getGreaterEquals()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " >= ? and ");
                        sqlParams.add(StringUtil.getValidNullStr(value));
                    } else if (paramVo.getLessEquals() != null && paramVo.getLessEquals()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " <= ? and ");
                        sqlParams.add(StringUtil.getValidNullStr(value));
                    } else if (paramVo.getNotEquals() != null && paramVo.getNotEquals()) {
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " != ? and ");
                        sqlParams.add(StringUtil.getValidNullStr(value));
                    } else {
                        if (entry.getValue() instanceof List) {
                            List list = (List) entry.getValue();
                            value = StringUtil.array2Char(list);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                        } else if (entry.getValue() instanceof String[]) {
                            String[] array = (String[]) entry.getValue();
                            value = StringUtil.array2CharStr(array);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " in (" + value + ") and ");
                        } else {
                            value = StringUtil.getValidNullStr(value);
                            values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ? and ");
                            sqlParams.add(value);
                        }
                    }
                }
            }
        }

        if (StrUtil.isBlank(values.toString())) {
            throw new BusinessException(EnumCode.REQUEST_PARAMS_ERROR);
        }

        String valueSQL = values.toString().substring(0, values.toString().lastIndexOf("and"));
        String sql = "delete from " + apiInfo.getTableName() + " where " + valueSQL;
        apiInfo.setSql(sql);
        apiInfo.setSqlParams(sqlParams);
    }
}
