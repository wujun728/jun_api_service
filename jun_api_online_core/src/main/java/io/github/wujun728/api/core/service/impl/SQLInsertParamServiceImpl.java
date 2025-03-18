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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SQLInsertParamServiceImpl implements ApiParamService {

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
            //单表
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
                entityInfo.setPrimaryKeyComment(entityAttrInfo.getColumnComment());
                entityInfo.setPrimaryKeyDataType(entityAttrInfo.getDataType());
            }
            List<ParamVo> paramVos = new ArrayList<>();
            for (EntityAttrInfo entityAttrInfo : entityInfo.getEntityAttrs()) {
                if (entityInfo.isAutoIncrement() && entityAttrInfo.getColumnName().equals(entityInfo.getTablePrimaryKey())) {
                    //主键 自增
                } else {
                    ParamVo paramVo = new ParamVo();
                    paramVo.setDataType(entityAttrInfo.getDataType());
                    paramVo.setColumnName(SqlUtl.getSQLColumn(entityAttrInfo.getColumnName()));
                    paramVo.setColumnComment(entityAttrInfo.getColumnComment());
                    paramVo.setAttrName(entityAttrInfo.getAttrName());
                    paramVo.setAttrType(entityAttrInfo.getAttrType());
                    paramVo.setColumnDefault(entityAttrInfo.getColumnDefault());
                    paramVo.setPrimaryKey(false);
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

            //校验判断唯一性
            List<ParamVo> paramVoList = paramVos.stream().filter(e -> e.getUniqueVerify() != null && e.getUniqueVerify() == true).collect(Collectors.toList());
            if(ObjectUtil.isNotEmpty(paramVoList)){
                for (ParamVo paramVo:paramVoList){
                    List<Map.Entry<String, Object>> list = params.entrySet().stream().filter(e -> e.getKey().equals(paramVo.getAttrName())).collect(Collectors.toList());
                    if(ObjectUtil.isNotEmpty(list)){
                        Object value = list.get(0).getValue();
                        if(value!=null && StrUtil.isNotBlank(StringUtil.getValidNullStr(value))){
                            String val = StringUtil.getValidNullStr(value);
                            String sql = "select count(1) as total from "+ tableName +" where " + SqlUtl.getSQLColumn(paramVo.getColumnName()) +" = '"+val+"'";
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
                    columns.append(SqlUtl.getSQLColumn(paramVo.getColumnName()) + ", ");
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
                        values.append("?, ");
                        sqlParams.add(null);
                    }else{
                        values.append("?, ");
                        sqlParams.add(value);
                    }
                }
            }
        }

        String columnsSQL = "";
        String valueSQL = "";
        //如果参数没有 采用默认数据
        if(StrUtil.isBlank(columns.toString())) {
            if(ObjectUtil.isNotEmpty(apiInfo.getParams())){
                List<ParamVo> paramVos = JSON.parseArray(apiInfo.getParams(), ParamVo.class);
                List<ParamVo> paramList = paramVos.stream().filter(e -> StrUtil.isNotBlank(e.getColumnDefault())).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(paramList)) {
                    columns.append(SqlUtl.getSQLColumn(paramList.get(0).getColumnName()) + ", ");
                    values.append("?, ");
                    sqlParams.add(paramList.get(0).getColumnDefault());
                }
            }
        }

        if(StrUtil.isBlank(columns.toString())) {
            throw new BusinessException(EnumCode.REQUEST_PARAMS_ERROR);
        }
        columnsSQL = columns.toString().substring(0, columns.toString().lastIndexOf(","));
        valueSQL = values.toString().substring(0, values.toString().lastIndexOf(","));

        String sql = "insert "+apiInfo.getTableName()+"( "+columnsSQL+" ) values ( "+valueSQL+" )";
        apiInfo.setSql(sql);
        apiInfo.setSqlParams(sqlParams);
    }
}
