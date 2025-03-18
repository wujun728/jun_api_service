package io.github.wujun728.api.core.service.impl;

import io.github.wujun728.api.common.bean.Page;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.TableInfo;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.jdbc.util.StringUtil;
import io.github.wujun728.api.common.utils.BeanUtil;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.common.validate.EntityValidation;
import io.github.wujun728.api.common.validate.ValidationType;
import io.github.wujun728.api.core.dto.ApiInfoDto;
import io.github.wujun728.api.core.dto.add.ApiBatchAddDto;
import io.github.wujun728.api.core.dto.list.ApiInfoListDto;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.factory.SQLParamServiceFactory;
import io.github.wujun728.api.core.manager.SQLInitManager;
import io.github.wujun728.api.core.mapper.ApiInfoMapper;
import io.github.wujun728.api.core.mapper.DataSourceMapper;
import io.github.wujun728.api.core.service.ApiInfoService;
import io.github.wujun728.api.core.template.service.PageTemplateService;
import io.github.wujun728.api.core.vo.*;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSON;
import io.github.wujun728.api.core.vo.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  API信息 service接口实现类
 *
 * @version 1.0
 * @date 2024-05-08
 */
@Service("lowCodeApiInfoServiceImpl")
public class ApiInfoServiceImpl implements ApiInfoService, EntityValidation<ApiInfo> {

	@Resource
	private ApiInfoMapper apiInfoMapper;

	@Resource
	private DataSourceMapper dataSourceMapper;

	@Resource
	private PageTemplateService pageTemplateService;

	@Resource
	private SQLInitManager sqlInitManager;

	@Value("${lowcode.page}")
	private String lowcodePage;

	/**
	 * 新增
	 *
	 * @param apiInfo 新增的对象
	 * @return 新增条数
	 */
	@Override
	public int insert(ApiInfo apiInfo){
		int result = 0;

		//校验
		this.validate(apiInfo, ValidationType.ADD);

		try {
			apiInfo.setApiId(null);
			apiInfo.setCreateTime(DateUtil.now());
			apiInfo.setUpdateTime(DateUtil.now());
			if(ObjectUtil.isEmpty(apiInfo.getState())){
				apiInfo.setState(ConstantUtil.IS_VALID_Y);
			}
			if(ObjectUtil.isNotEmpty(apiInfo.getParamVos())){
				apiInfo.setParams(JSON.toJSONString(apiInfo.getParamVos()));
			}
			result = apiInfoMapper.insert(apiInfo);
		} catch (Exception e){
			throw new BusinessException(EnumCode.INSERT_FAIL, e);
		}
		return result;
	}

	/**
	 * 新增
	 *
	 * @param apiInfo 新增的对象
	 * @return 新增条数
	 */
	@Override
	public int insert(ApiInfo apiInfo, boolean batch){
		int result = 0;

		//校验apiPath
		if(StrUtil.isNotBlank(apiInfo.getApiPath())){
			ApiInfoDto apiInfoDto = new ApiInfoDto();
			if(SqlUtl.OP_TYPE_SELECT.equals(apiInfo.getOpType())){
				apiInfoDto.setApiPathLike(apiInfo.getApiPath());
			}else{
				apiInfoDto.setApiPath(apiInfo.getApiPath());
			}
			apiInfoDto.setApiType(apiInfo.getApiType());
			long total = this.selectCount(apiInfoDto);
			if(total>0){
				apiInfo.setApiPath(apiInfo.getApiPath()+(total+1));
			}
		}

		try {
			apiInfo.setApiId(null);
			apiInfo.setCreateTime(DateUtil.now());
			apiInfo.setUpdateTime(DateUtil.now());
			if(ObjectUtil.isEmpty(apiInfo.getState())){
				apiInfo.setState(ConstantUtil.IS_VALID_Y);
			}
			if(ObjectUtil.isNotEmpty(apiInfo.getParamVos())){
				apiInfo.setParams(JSON.toJSONString(apiInfo.getParamVos()));
			}
			result = apiInfoMapper.insert(apiInfo);
		} catch (Exception e){
			throw new BusinessException(EnumCode.INSERT_FAIL, e);
		}
		return result;
	}

	/**
	 * 修改
	 *
	 * @param apiInfo 修改的对象
	 * @return 修改条数
	 */
	@Override
	public int update(ApiInfo apiInfo) {
		int result = 0;

		//校验
		validate(apiInfo, ValidationType.UPDATE);

		try {

			if(ObjectUtil.isNotEmpty(apiInfo.getParamVos())){
				apiInfo.setParams(JSON.toJSONString(apiInfo.getParamVos()));
			}

			apiInfo.setUpdateTime(DateUtil.now());
			result = apiInfoMapper.updateById(apiInfo);
		} catch (Exception e){
			throw new BusinessException(EnumCode.UPDATE_FAIL, e);
		}

		return result;
	}

	/**
	 * 校验ApiInfo
	 * @param apiInfo
	 */
	@Override
	public void validate(ApiInfo apiInfo, ValidationType validationType) {
		//校验ApiInfo
		if(StrUtil.isNotBlank(apiInfo.getApiPath())){
			ApiInfoDto apiInfoDto = new ApiInfoDto();
			apiInfoDto.setApiPath(apiInfo.getApiPath());
			apiInfoDto.setApiType(apiInfo.getApiType());
			if(ValidationType.UPDATE.equals(validationType)){
				apiInfoDto.setNoApiId(apiInfo.getApiId());
			}
			long total = this.selectCount(apiInfoDto);
			if(total>0){
				throw new BusinessException(EnumCode.VALID_FAIL,"该接口地址已存在！");
			}
		}
	}

	/**
	 * 修改
	 *
	 * @param map
	 * @return 修改条数
	 */
	@Override
	public int updateByMap(Map<String,Object> map) {
		int result = 0;

		try {
			result = apiInfoMapper.updateByMap(map);
		} catch (Exception e){
			throw new BusinessException(EnumCode.UPDATE_FAIL, e);
		}

		return result;
	}

	/**
	 * 删除
	 *
	 * @param apiIds 接口ID数组
	 * @return 删除的条数
	 */
	@Override
	public int delete(Integer ... apiIds) {
		int result = 0;

		if(ObjectUtil.isEmpty(apiIds)){
		 	throw BusinessException.paramsEmpty("apiIds");
		}

		try {
			for (Integer apiId:apiIds){
				ApiInfo apiInfo = new ApiInfo();
				apiInfo.setApiId(apiId);
				apiInfo.setState(ConstantUtil.IS_VALID_N);
				result += this.update(apiInfo);
			}
		} catch (Exception e){
			throw new BusinessException(EnumCode.DELETE_FAIL, e);
		}

		return result;
	}

	/**
	 * 删除
	 *
	 * @param map
	 * @return 删除条数
	 */
	@Override
	public int deleteByMap(Map<String,Object> map) {
		int result = 0;

		if(map==null || map.size()==0){
			throw BusinessException.paramsEmpty("map");
		}

		try {
			result = apiInfoMapper.deleteByMap(map);
		} catch (Exception e){
			throw new BusinessException(EnumCode.DELETE_FAIL, e);
		}

		return result;
	}

	/**
	 * 查询单个
	 *
	 * @param apiId 接口ID
	 * @return 查询对象
	 */
	@Override
	public ApiInfoVo selectById(Integer apiId) {
		ApiInfoVo apiInfoVo = null;

		if(ObjectUtil.isEmpty(apiId)){
			throw BusinessException.paramsEmpty("apiId");
		}

		try {
			apiInfoVo = apiInfoMapper.selectById(apiId);
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}

		return apiInfoVo;
	}

	/**
	 * 分页查询
	 *
	 * @param apiInfoListDto 查询条件
	 * @return 分页对象
	 */
	@Override
	public Page<ApiInfoVo> selectPage(ApiInfoListDto apiInfoListDto) {
		Page<ApiInfoVo> page = new Page<ApiInfoVo>();
		try {
			List<ApiInfoVo> apiInfoVos = apiInfoMapper.selectListByMap(BeanUtil.beanToMap(apiInfoListDto));
			apiInfoListDto.clearWrapper();
			long total = apiInfoMapper.selectCountByMap(BeanUtil.beanToMap(apiInfoListDto));
			page = new Page<ApiInfoVo>(apiInfoVos, total);
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return page;
	}

	/**
	 * 查询集合
	 *
	 * @param apiInfoListDto 查询条件对象
	 * @return 查询的集合
	 */
	@Override
	public List<ApiInfoTreeVo> selectApiTrees(ApiInfoListDto apiInfoListDto){
		List<ApiInfoTreeVo> apiInfoTreeVos = new ArrayList<>();
		List<ApiBaseVo> apiBaseVos = apiInfoMapper.selectBaseListByMap(BeanUtil.beanToMap(apiInfoListDto));
		if(ObjectUtil.isEmpty(apiBaseVos)){
			return apiInfoTreeVos;
		}
		Map<Integer, List<ApiBaseVo>> sourceMap = apiBaseVos.stream().collect(Collectors.groupingBy(ApiBaseVo::getSourceId));
		for (Map.Entry<Integer, List<ApiBaseVo>> entry: sourceMap.entrySet()){
			Integer sourceId = entry.getKey();
			List<ApiBaseVo> apiInfoVoList = entry.getValue();
			String sourceName = apiInfoVoList.stream().filter(e -> e.getSourceId().equals(sourceId)).collect(Collectors.toList()).get(0).getSourceName();
			//分组
			ApiInfoTreeVo apiInfoTreeVo = new ApiInfoTreeVo();
			apiInfoTreeVo.setSourceId(sourceId);
			apiInfoTreeVo.setSourceName(sourceName);

			List<ApiDatabaseTreeVo> apiDatabaseTreeVos = new ArrayList<>();
			Map<String, List<ApiBaseVo>> databaseNameMap = apiInfoVoList.stream().collect(Collectors.groupingBy(ApiBaseVo::getDatabaseName));
			for (Map.Entry<String, List<ApiBaseVo>> databaseEntry: databaseNameMap.entrySet()){
				ApiDatabaseTreeVo apiDatabaseTreeVo = new ApiDatabaseTreeVo();
				apiDatabaseTreeVo.setDatabaseName(databaseEntry.getKey());

				List<ApiGroupTreeVo> apiGroupTreeVos = new ArrayList<>();
				Map<String, List<ApiBaseVo>> groupNameMap = databaseEntry.getValue().stream().collect(Collectors.groupingBy(ApiBaseVo::getGroupName));
				for (Map.Entry<String, List<ApiBaseVo>> groupEntry: groupNameMap.entrySet()){
					ApiGroupTreeVo apiGroupTreeVo = new ApiGroupTreeVo();
					apiGroupTreeVo.setGroupName(groupEntry.getKey());
					apiGroupTreeVo.setApis(groupEntry.getValue());
					apiGroupTreeVos.add(apiGroupTreeVo);
				}
				apiDatabaseTreeVo.setGroups(apiGroupTreeVos);
				apiDatabaseTreeVos.add(apiDatabaseTreeVo);
			}
			apiInfoTreeVo.setDatabases(apiDatabaseTreeVos);
			apiInfoTreeVos.add(apiInfoTreeVo);
		}
		return apiInfoTreeVos;
	}

	/**
	 * 查询集合
	 *
	 * @return 查询的集合
	 */
	@Override
	public List<String> selectApiGroups(Integer sourceId, String databaseName){
		List<String> list = null;
		try {
			Map<String,Object> map =  new HashMap<>();
			if(sourceId==null){
				DataSource dataSource = sqlInitManager.getDefaultDatasource();
				databaseName = dataSource.getSchema();
			}else{
				map.put("sourceId",sourceId);
			}
			if(StrUtil.isNotBlank(databaseName)){
				map.put("databaseName",databaseName);
			}
			map.put("state", ConstantUtil.IS_VALID_Y);
			list = apiInfoMapper.selectApiGroups(map);
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return list;
	}
	/**
	 * 查询集合
	 *
	 * @param apiInfoDto 查询条件对象
	 * @return 查询的集合
	 */
	@Override
	public List<ApiInfoVo> selectList(ApiInfoDto apiInfoDto) {
		List<ApiInfoVo> list = null;
		try {
			list = apiInfoMapper.selectListByMap(BeanUtil.beanToMap(apiInfoDto));
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return list;
	}

	/**
	 * 查询总数
	 *
	 * @param apiInfoDto 查询条件对象
	 * @return 查询的总数
	 */
	@Override
	public long selectCount(ApiInfoDto apiInfoDto) {
		long total = 0;
		try {
			total = apiInfoMapper.selectCountByMap(BeanUtil.beanToMap(apiInfoDto));
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return total;
	}

	/**
	 * 新增
	 *
	 * @param apiBatchAddDto 新增的对象
	 * @return 新增条数
	 */
	@Transactional(rollbackFor = {RuntimeException.class, Error.class, Exception.class})
	@Override
	public void batchInsert(ApiBatchAddDto apiBatchAddDto) throws Exception {

		if(ObjectUtil.isEmpty(apiBatchAddDto.getTableInfos())){
			//操作表不能为空
			throw new BusinessException(EnumCode.PARAMS_ERROR, "操作表不能为空");
		}

		for (TableInfo tableInfo: apiBatchAddDto.getTableInfos()){
			List<ApiInfo> apiInfos = new ArrayList<>();
			String tableComment = tableInfo.getTableComment();
			if(StrUtil.isNotBlank(tableComment)){
				String first = tableComment.substring(tableComment.length() - 1);
				if (first.equals("表")) {
					tableComment = tableComment.substring(0, tableComment.length() - 1);
				}
			}
			ApiInfo apiInfo = new ApiInfo();
			apiInfo.setTableName(tableInfo.getTableName());
			apiInfo.setGroupName(tableComment);
			apiInfo.setSourceId(apiBatchAddDto.getSourceId());
			apiInfo.setDatabaseName(apiBatchAddDto.getDatabaseName());
			apiInfo.setParamType(ApiInfo.PARAM_TYPE_AUTO);

			//新增
			cn.hutool.core.bean.BeanUtil.copyProperties(getInsertApiBaseByTableVo(tableInfo), apiInfo);
			apiInfo.setSql(null);
			apiInfo.setParamVos(null);
			apiInfo.setParams(null);
			SQLParamServiceFactory.getApiParamService(apiInfo.getOpType()).dearApiParam(apiInfo);
			this.insert(apiInfo,true);
			apiInfos.add(BeanUtil.copy(apiInfo,ApiInfo.class));

			//修改
			cn.hutool.core.bean.BeanUtil.copyProperties(getUpdateApiBaseByTableVo(tableInfo), apiInfo);
			apiInfo.setSql(null);
			apiInfo.setParamVos(null);
			apiInfo.setParams(null);
			SQLParamServiceFactory.getApiParamService(apiInfo.getOpType()).dearApiParam(apiInfo);
			this.insert(apiInfo,true);
			apiInfos.add(BeanUtil.copy(apiInfo,ApiInfo.class));

			//删除
			ApiBaseByTableVo deleteApiBaseByTableVo = getDeleteApiBaseByTableVo(tableInfo);
			cn.hutool.core.bean.BeanUtil.copyProperties(deleteApiBaseByTableVo, apiInfo);
			apiInfo.setSql(null);
			apiInfo.setParamVos(null);
			apiInfo.setParams(null);
			SQLParamServiceFactory.getApiParamService(apiInfo.getOpType()).dearApiParam(apiInfo);
			this.insert(apiInfo,true);
			String delApiPath = apiInfo.getApiPath();
			apiInfos.add(BeanUtil.copy(apiInfo,ApiInfo.class));

			//查询列表
			cn.hutool.core.bean.BeanUtil.copyProperties(getSelectApiBaseByTableVo(tableInfo), apiInfo);
			apiInfo.setSql(null);
			apiInfo.setDeleteApiPath(delApiPath);
			apiInfo.setParamVos(null);
			apiInfo.setParams(null);
			apiInfo.setPageFlag(ConstantUtil.IS_VALID_Y);
			SQLParamServiceFactory.getApiParamService(apiInfo.getOpType()).dearApiParam(apiInfo);
			this.insert(apiInfo,true);
			apiInfos.add(BeanUtil.copy(apiInfo,ApiInfo.class));

			//查询单个
			cn.hutool.core.bean.BeanUtil.copyProperties(getSelectOneApiBaseByTableVo(tableInfo), apiInfo);
			apiInfo.setSql(null);
			apiInfo.setParamVos(null);
			apiInfo.setParams(null);
			apiInfo.setPageFlag(ConstantUtil.IS_VALID_N);
			SQLParamServiceFactory.getApiParamService(apiInfo.getOpType()).dearApiParam(apiInfo);
			this.insert(apiInfo,true);
			apiInfos.add(BeanUtil.copy(apiInfo,ApiInfo.class));

			if (lowcodePage.equals("true")){
				pageTemplateService.createFiles(apiInfos);
			}
		}
	}

	private static ApiBaseByTableVo getInsertApiBaseByTableVo(TableInfo tableInfo){
		ApiBaseByTableVo apiBaseByTableVo = new ApiBaseByTableVo();
		apiBaseByTableVo.setApiName("新增"+tableInfo.getTableComment());
		apiBaseByTableVo.setApiPath(StringUtil.toUnderscoreToCamelCase(tableInfo.getTableName()));
		apiBaseByTableVo.setApiType(Method.POST.name());
		apiBaseByTableVo.setGroupName(tableInfo.getTableComment());
		apiBaseByTableVo.setOpType(SqlUtl.OP_TYPE_INSERT.toUpperCase());
		return apiBaseByTableVo;
	}

	private static ApiBaseByTableVo getUpdateApiBaseByTableVo(TableInfo tableInfo){
		ApiBaseByTableVo apiBaseByTableVo = new ApiBaseByTableVo();
		apiBaseByTableVo.setApiName("修改"+tableInfo.getTableComment());
		apiBaseByTableVo.setApiPath(StringUtil.toUnderscoreToCamelCase(tableInfo.getTableName()));
		apiBaseByTableVo.setApiType(Method.PUT.name());
		apiBaseByTableVo.setGroupName(tableInfo.getTableComment());
		apiBaseByTableVo.setOpType(SqlUtl.OP_TYPE_UPDATE.toUpperCase());
		return apiBaseByTableVo;
	}

	private static ApiBaseByTableVo getDeleteApiBaseByTableVo(TableInfo tableInfo){
		ApiBaseByTableVo apiBaseByTableVo = new ApiBaseByTableVo();
		apiBaseByTableVo.setApiName("删除"+tableInfo.getTableComment());
		apiBaseByTableVo.setApiPath(StringUtil.toUnderscoreToCamelCase(tableInfo.getTableName()));
		apiBaseByTableVo.setApiType(Method.DELETE.name());
		apiBaseByTableVo.setGroupName(tableInfo.getTableComment());
		apiBaseByTableVo.setOpType(SqlUtl.OP_TYPE_DELETE.toUpperCase());
		return apiBaseByTableVo;
	}

	private static ApiBaseByTableVo getSelectApiBaseByTableVo(TableInfo tableInfo){
		ApiBaseByTableVo apiBaseByTableVo = new ApiBaseByTableVo();
		apiBaseByTableVo.setApiName("查询列表"+tableInfo.getTableComment());
		apiBaseByTableVo.setApiPath(StringUtil.toUnderscoreToCamelCase(tableInfo.getTableName())+"s");
		apiBaseByTableVo.setApiType(Method.GET.name());
		apiBaseByTableVo.setGroupName(tableInfo.getTableComment());
		apiBaseByTableVo.setOpType(SqlUtl.OP_TYPE_SELECT.toUpperCase());
		return apiBaseByTableVo;
	}

	private static ApiBaseByTableVo getSelectOneApiBaseByTableVo(TableInfo tableInfo){
		ApiBaseByTableVo apiBaseByTableVo = new ApiBaseByTableVo();
		apiBaseByTableVo.setApiName("查询单个"+tableInfo.getTableComment());
		apiBaseByTableVo.setApiPath(StringUtil.toUnderscoreToCamelCase(tableInfo.getTableName()));
		apiBaseByTableVo.setApiType(Method.GET.name());
		apiBaseByTableVo.setGroupName(tableInfo.getTableComment());
		apiBaseByTableVo.setOpType(SqlUtl.OP_TYPE_SELECT_ONE.toUpperCase());
		return apiBaseByTableVo;
	}
}
