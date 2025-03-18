package io.github.wujun728.api.core.service.impl;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityAttrInfo;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.factory.EntityServiceFactory;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.MessageUtils;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.manager.SQLInitManager;
import io.github.wujun728.api.core.mapper.DataSourceMapper;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.service.DataSourceService;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SQLUpdateParamServiceImpl implements ApiParamService {

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private SQLInitManager sqlInitManager;

    @Resource
    private DataSourceService dataSourceService;

    @Override
    public void dearApiParam(ApiInfo apiInfo) throws Exception {
        if(StrUtil.isNotBlank(apiInfo.getSql())){
            //SQL参数
            List<String> list = ReUtil.findAllGroup0(SqlUtl.REGEX_KUOHAO, apiInfo.getSql());
            if(ObjectUtil.isNotEmpty(list)){
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
        }else {
            if (StrUtil.isBlank(apiInfo.getTableName())) {
                //操作表不能为空
                throw new BusinessException(EnumCode.PARAMS_ERROR, "操作表不能为空");
            }
            DataSource dataSource = null;
            if(apiInfo.getSourceId()==null){
                dataSource = sqlInitManager.getDefaultDatasource();
                apiInfo.setDatabaseName(dataSource.getSchema());
            }else{
                dataSource = dataSourceMapper.selectById(apiInfo.getSourceId());
            }
            if(dataSource==null){
                throw new BusinessException(EnumCode.NO_DATA_FOUND,"未查到数据源");
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
                } else {
                    paramVo.setPrimaryKey(false);
                }

                paramVo.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                paramVo.setColumnComment(entityAttrInfo.getColumnComment());
                paramVo.setAttrName(entityAttrInfo.getAttrName());
                paramVo.setAttrType(entityAttrInfo.getAttrType());
                paramVo.setDataType(entityAttrInfo.getDataType());
                paramVo.setColumnDefault(entityAttrInfo.getColumnDefault());
                if(ObjectUtil.isNotEmpty(apiInfo.getVerifyColumns())){
                    for (String column: apiInfo.getVerifyColumns()){
                        if(column.equals(entityAttrInfo.getColumnName())){
                            paramVo.setUniqueVerify(true);
                            break;
                        }
                    }
                }
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
    public void dearApiSQL(ApiInfo apiInfo, Map<String,Object> params) throws Exception {

        if(ObjectUtil.isNotEmpty(params)) {
            Iterator<Map.Entry<String, Object>> it = params.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                if (ObjectUtil.isEmpty(entry.getValue())) {
                    it.remove();
                }
            }
        }

        if(StrUtil.isNotBlank(apiInfo.getSql())){
            SQLParamUtil.dearSqlParams(apiInfo,params);
            return;
        }

        List<Object> sqlParams = new ArrayList<>();
        StringBuffer columns = new StringBuffer();
        StringBuffer values = new StringBuffer();

        if(ObjectUtil.isNotEmpty(params) && ObjectUtil.isNotEmpty(apiInfo.getParams())){
            List<SQLParamVo> sqlParamVos = JSON.parseArray(apiInfo.getParams(), SQLParamVo.class);
            List<ParamVo> paramVos = sqlParamVos.get(0).getParamVos();
            String tableName = sqlParamVos.get(0).getTableName();
            ParamVo primaryParamVo = paramVos.stream().filter(e -> e.getPrimaryKey()).collect(Collectors.toList()).get(0);
            List<Map.Entry<String, Object>> primaryKeyList = params.entrySet().stream().filter(e -> e.getKey().equals(primaryParamVo.getAttrName())).collect(Collectors.toList());
            String primaryKeyVal = "";
            if(ObjectUtil.isNotEmpty(primaryKeyList)){
                primaryKeyVal= StringUtil.getValidNullStr(primaryKeyList.get(0).getValue());
            }
            if(ObjectUtil.isEmpty(primaryKeyVal)){
                throw new BusinessException(EnumCode.PARAMS_ERROR, primaryParamVo.getColumnName()+ MessageUtils.get(EnumCode.PARAMS_NOT_EMPTY));
            }

            //校验判断唯一性
            List<ParamVo> paramVoList = paramVos.stream().filter(e -> e.getUniqueVerify() != null && e.getUniqueVerify() == true).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(paramVoList)){
                for (ParamVo paramVo:paramVoList){
                    List<Map.Entry<String, Object>> list = params.entrySet().stream().filter(e -> e.getKey().equals(paramVo.getAttrName())).collect(Collectors.toList());
                    if(ObjectUtil.isNotEmpty(list)){
                        Object value = list.get(0).getValue();
                        if(value!=null && StrUtil.isNotBlank(StringUtil.getValidNullStr(value))){
                            String val = StringUtil.getValidNullStr(value);
                            String sql = "select count(1) as total from "+ tableName +" where "+SqlUtl.getSQLColumn(primaryParamVo.getColumnName())+
                                    " != '"+primaryKeyVal+"' and " + SqlUtl.getSQLColumn(paramVo.getColumnName()) +" = '"+val+"' ";
                            List<String> totals = dataSourceService.executeUniqueSQL(apiInfo.getDataSource(), apiInfo.getDatabaseName(), sql, null);
                            if(ObjectUtil.isNotEmpty(totals) && Long.parseLong(totals.get(0))>0){
                                throw new BusinessException(EnumCode.DATA_EXIST,"该"+paramVo.getColumnComment()+"已存在！");
                            }
                        }
                    }
                }
            }

            for (Map.Entry<String,Object> entry:params.entrySet()) {
                List<ParamVo> paramList = paramVos.stream().filter(e -> e.getAttrName().equals(entry.getKey())).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(paramList)) {
                    ParamVo paramVo = paramList.get(0);
                    if(paramVo.getPrimaryKey() !=null && paramVo.getPrimaryKey()){
                        values.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = '"+ StringUtil.getValidNullStr(entry.getValue()) + "' and ");
                    }else{
                        String value = StringUtil.getValidNullStr(entry.getValue());
                        if(StrUtil.isBlank(value)
                                && ("date".equalsIgnoreCase(paramVo.getDataType())
                                || "datetime".equalsIgnoreCase(paramVo.getDataType())
                                || "json".equalsIgnoreCase(paramVo.getDataType())
                                || "int".equalsIgnoreCase(paramVo.getDataType())
                                || "bigint".equalsIgnoreCase(paramVo.getDataType())
                                || "tinyint".equalsIgnoreCase(paramVo.getDataType())
                                || "integer".equalsIgnoreCase(paramVo.getDataType())
                                || "double".equalsIgnoreCase(paramVo.getDataType())
                                || "money".equalsIgnoreCase(paramVo.getDataType())
                                || "decimal".equalsIgnoreCase(paramVo.getDataType())
                                || "float".equalsIgnoreCase(paramVo.getDataType())
                                || "numeric".equalsIgnoreCase(paramVo.getDataType())
                        )){
                            columns.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ?, ");
                            sqlParams.add(null);
                        }else{
                            columns.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + " = ?, ");
                            sqlParams.add(value);
                        }
                    }
                }
            }
        }

        if(StrUtil.isBlank(columns.toString())) {
            throw new BusinessException(EnumCode.REQUEST_PARAMS_ERROR);
        }

        String columnsSQL = columns.toString().substring(0, columns.toString().lastIndexOf(","));
        String valueSQL = values.toString().substring(0, values.toString().lastIndexOf("and"));
        String sql = "update "+apiInfo.getTableName()+" set "+columnsSQL+" where "+valueSQL;
        apiInfo.setSql(sql);
        apiInfo.setSqlParams(sqlParams);

    }
}
