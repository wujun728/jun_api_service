package io.github.wujun728.api.core.service.impl;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.bean.Page;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.util.DaoException;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.BeanUtil;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.common.validate.EntityValidation;
import io.github.wujun728.api.common.validate.ValidationType;
import io.github.wujun728.api.core.dto.DataSourceDto;
import io.github.wujun728.api.core.dto.list.DataSourceListDto;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.manager.SQLInitManager;
import io.github.wujun728.api.core.mapper.ApiInfoMapper;
import io.github.wujun728.api.core.mapper.DataSourceMapper;
import io.github.wujun728.api.core.service.DataSourceService;
import io.github.wujun728.api.core.vo.DataSourceVo;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 *  数据源 service接口实现类
 *
 * @version 1.0
 * @date 2024-05-07
 */
@Service("lowCodeDataSourceServiceImpl")
public class DataSourceServiceImpl implements DataSourceService, EntityValidation<DataSource> {

	@Resource
	private DataSourceMapper dataSourceMapper;

	@Resource
	private ApiInfoMapper apiInfoMapper;

	private Logger logger = LoggerFactory.getLogger(DataSourceServiceImpl.class);

	@Resource
	private SQLInitManager sqlInitManager;

	/**
	 * 新增
	 *
	 * @param dataSource 新增的对象
	 * @return 新增条数
	 */
	@Override
	public int insert(DataSource dataSource){
		int result = 0;

		//校验
		this.validate(dataSource, ValidationType.ADD);

		try {
			dataSource.setCreateTime(DateUtil.now());
			dataSource.setUpdateTime(DateUtil.now());
			if(ObjectUtil.isEmpty(dataSource.getState())){
				dataSource.setState(ConstantUtil.IS_VALID_Y);
			}
			result = dataSourceMapper.insert(dataSource);
		} catch (Exception e){
			throw new BusinessException(EnumCode.INSERT_FAIL, e);
		}
		return result;
	}

	/**
	 * 修改
	 *
	 * @param dataSource 修改的对象
	 * @return 修改条数
	 */
	@Override
	public int update(DataSource dataSource) {
		int result = 0;

		//校验
		validate(dataSource, ValidationType.UPDATE);

		try {
			dataSource.setUpdateTime(DateUtil.now());
			result = dataSourceMapper.updateById(dataSource);
		} catch (Exception e){
			throw new BusinessException(EnumCode.UPDATE_FAIL, e);
		}

		return result;
	}

	/**
	 * 校验DataSource
	 * @param dataSource
	 */
	@Override
	public void validate(DataSource dataSource, ValidationType validationType) {
		//校验DataSource
		if(StrUtil.isNotBlank(dataSource.getSourceName())){
			DataSourceDto dataSourceDto = new DataSourceDto();
			dataSourceDto.setSourceName(dataSource.getSourceName());
			if(ValidationType.UPDATE == validationType){
				dataSourceDto.setNoSourceId(dataSource.getSourceId());
			}
			long total = this.selectCount(dataSourceDto);
			if(total>0){
				//throw new BusinessException(EnumCode.VALID_FAIL,"该数据源名称已存在！");
			}
		}

		if (ObjectUtil.isNotEmpty(dataSource.getDbType())
			&& ObjectUtil.isNotEmpty(dataSource.getHost())
			&& ObjectUtil.isNotEmpty(dataSource.getPort())
			&& ObjectUtil.isNotEmpty(dataSource.getUserName())
		) {
			DataSourceDto dataSourceDto = new DataSourceDto();
			dataSourceDto.setDbType(dataSource.getDbType());
			dataSourceDto.setHost(dataSource.getHost());
			dataSourceDto.setPort(dataSource.getPort());
			dataSourceDto.setUserName(dataSource.getUserName());
			if(ValidationType.UPDATE == validationType){
				dataSourceDto.setNoSourceId(dataSource.getSourceId());
			}
			long total = this.selectCount(dataSourceDto);
			if (total>0) {
				throw new BusinessException(EnumCode.VALID_FAIL,"该数据源信息已存在！");
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
			result = dataSourceMapper.updateByMap(map);
		} catch (Exception e){
			throw new BusinessException(EnumCode.UPDATE_FAIL, e);
		}

		return result;
	}

	/**
	 * 删除
	 *
	 * @param sourceIds 数据源ID数组
	 * @return 删除的条数
	 */
	@Override
	public int delete(Integer ... sourceIds) {
		int result = 0;

		if(ObjectUtil.isEmpty(sourceIds)){
		 	throw BusinessException.paramsEmpty("sourceIds");
		}

		//校验是否有API接口
		for (Integer sourceId:sourceIds) {
			DataSourceVo dataSourceVo = dataSourceMapper.selectById(sourceId);
			if (dataSourceVo != null) {
				Map<String, Object> map = new HashMap<>();
				map.put("sourceId", sourceId);
				long total = apiInfoMapper.selectCountByMap(map);
				if (total > 0) {
					throw new BusinessException(EnumCode.VALID_FAIL,"数据源【"+dataSourceVo.getSourceName()+"】下有API接口，暂时无法删除！");
				}
			}
		}

		try {
			for (Integer sourceId:sourceIds){
				DataSource dataSource = new DataSource();
				dataSource.setSourceId(sourceId);
				dataSource.setState(ConstantUtil.IS_VALID_N);
				result+=this.update(dataSource);
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
			result = dataSourceMapper.deleteByMap(map);
		} catch (Exception e){
			throw new BusinessException(EnumCode.DELETE_FAIL, e);
		}

		return result;
	}

	/**
	 * 查询单个
	 *
	 * @param sourceId 数据源ID
	 * @return 查询对象
	 */
	@Override
	public DataSourceVo selectById(Integer sourceId) {
		DataSourceVo dataSourceVo = null;

		if(ObjectUtil.isEmpty(sourceId)){
			throw BusinessException.paramsEmpty("sourceId");
		}

		try {
			dataSourceVo = dataSourceMapper.selectById(sourceId);
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}

		return dataSourceVo;
	}

	/**
	 * 分页查询
	 *
	 * @param dataSourceListDto 查询条件
	 * @return 分页对象
	 */
	@Override
	public Page<DataSourceVo> selectPage(DataSourceListDto dataSourceListDto) {
		Page<DataSourceVo> page = new Page<DataSourceVo>();
		try {
			List<DataSourceVo> dataSourceVos = dataSourceMapper.selectListByMap(BeanUtil.beanToMap(dataSourceListDto));
			dataSourceListDto.clearWrapper();
			long total = dataSourceMapper.selectCountByMap(BeanUtil.beanToMap(dataSourceListDto));
			page = new Page<DataSourceVo>(dataSourceVos, total);
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return page;
	}

	/**
	 * 查询集合
	 *
	 * @param dataSourceDto 查询条件对象
	 * @return 查询的集合
	 */
	@Override
	public List<DataSourceVo> selectList(DataSourceDto dataSourceDto) {
		List<DataSourceVo> list = null;
		try {
			list = dataSourceMapper.selectListByMap(BeanUtil.beanToMap(dataSourceDto));
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return list;
	}

	/**
	 * 查询总数
	 *
	 * @param dataSourceDto 查询条件对象
	 * @return 查询的总数
	 */
	@Override
	public long selectCount(DataSourceDto dataSourceDto) {
		long total = 0;
		try {
			total = dataSourceMapper.selectCountByMap(BeanUtil.beanToMap(dataSourceDto));
		} catch (Exception e){
			throw new BusinessException(EnumCode.QUERY_FAIL, e);
		}
		return total;
	}

	/**
	 * 执行SQL
	 * @param dataSource
	 * @param database
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	public List<String> executeUniqueSQL(DataSource dataSource, String database, String sql, List<Object> params) throws DaoException{
		Connector connector = new Connector();
		connector.setSchema(dataSource.getSchema());
		connector.setDbType(dataSource.getDbType());
		connector.setDriver(dataSource.getDriver());
		connector.setUrl(dataSource.getRealUrl(database));
		connector.setUser(dataSource.getUserName());
		connector.setPassword(dataSource.getPassword());

		if(StrUtil.isBlank(sql)){
			logger.info("executeUniqueSQL sql = {}", sql);
			throw new BusinessException(EnumCode.PARAMS_ERROR,"参数错误");
		}

		if (sql.indexOf("#")!=-1 || sql.indexOf("$")!=-1){
			logger.info("executeUniqueSQL sql = {}", sql);
			throw new BusinessException(EnumCode.REQUEST_PARAMS_ERROR);
		}

		BaseDao baseDao = new BaseDao(connector);
		List<String> result = baseDao.uniqueResult(sql, params);
		return result;
	}

	/**
	 * 执行SQL
	 * @param sourceId
	 * @param database
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	@Override
	public List<String> executeUniqueSQL(Integer sourceId, String database, String sql, List<Object> params) throws DaoException{
		//处理参数
		DataSourceVo dataSourceVo = this.selectById(sourceId);
		if(dataSourceVo == null){
			throw new BusinessException(EnumCode.NO_DATA_FOUND);
		}

		return executeUniqueSQL(dataSourceVo,database,sql, params);
	}

	/**
	 * 执行SQL
	 * @param dataSource
	 * @param database
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	@Override
	public ApiResult executeSQL(DataSource dataSource, String database, String sql, List<Object> params, boolean isCamelCase, String opType) {
		Connector connector = new Connector();
		connector.setSchema(dataSource.getSchema());
		connector.setDbType(dataSource.getDbType());
		connector.setDriver(dataSource.getDriver());
		connector.setUrl(dataSource.getRealUrl(database));
		connector.setUser(dataSource.getUserName());
		connector.setPassword(dataSource.getPassword());

		if(StrUtil.isBlank(sql)){
			logger.info("executeSQL sql = {}", sql);
			throw new BusinessException(EnumCode.PARAMS_ERROR,"参数错误");
		}

		if (sql.indexOf("#")!=-1 || sql.indexOf("$")!=-1){
			logger.info("executeSQL sql = {}", sql);
			throw new BusinessException(EnumCode.REQUEST_PARAMS_ERROR);
		}

		BaseDao baseDao = new BaseDao(connector);

		if(ObjectUtil.isEmpty(opType)){
			opType = getSQLOpType(sql);
		}

		try {
			if(SqlUtl.OP_TYPE_SELECT.equals(opType)){
				//查询
				List<LinkedHashMap<String, Object>> result = baseDao.findResult(sql, params, isCamelCase);
				return ApiResult.data(result);
			}else if(SqlUtl.OP_TYPE_SELECT_ONE.equals(opType)){
				//查询
				List<LinkedHashMap<String, Object>> result = baseDao.findResult(sql, params, isCamelCase);
				if(result!=null && result.size()>0){
					return ApiResult.data(result.get(0));
				}
				return ApiResult.data(null);
			}else if(SqlUtl.OP_TYPE_INSERT.equals(opType)){
				int i = baseDao.singleUpdate(sql, params);
				return ApiResult.successData(EnumCode.OP_SUCCESS, i);
			}else if(SqlUtl.OP_TYPE_UPDATE.equals(opType)){
				int i = baseDao.singleUpdate(sql, params);
				return ApiResult.successData(EnumCode.OP_SUCCESS, i);
			}else if(SqlUtl.OP_TYPE_DELETE.equals(opType)){
				int i = baseDao.singleUpdate(sql, params);
				return ApiResult.successData(EnumCode.OP_SUCCESS, i);
			}else{
				//查询
				List<LinkedHashMap<String, Object>> result = baseDao.findResult(sql, params, isCamelCase);
				return ApiResult.data(result);
			}
		}catch (Exception e){
			e.printStackTrace();
			return ApiResult.failure(EnumCode.OP_FAIL, "SQL执行异常");
		}
	}

	private String getSQLOpType(String sql){
		String first = sql.split(" ")[0].trim().toUpperCase();
		if(Arrays.stream(SqlUtl.OP_TYPES).filter(i->i.equals(first)).collect(Collectors.toList()).size()>0){
			return first;
		}
		return SqlUtl.OP_TYPE_SELECT;
	}

	/**
	 * 执行SQL
	 * @param sourceId
	 * @param database
	 * @param sql
	 * @return
	 * @throws DaoException
	 */
	@Override
	public ApiResult executeSQL(Integer sourceId, String database, String sql, List<Object> params , boolean isCamelCase, String opType) {

		DataSource dataSource = null;
		if(sourceId == null){
			dataSource = sqlInitManager.getDefaultDatasource();
			database = dataSource.getSchema();
		}else{
			dataSource = this.selectById(sourceId);
		}
		if(dataSource == null){
			throw new BusinessException(EnumCode.NO_DATA_FOUND);
		}
		return executeSQL(dataSource, database,sql,params, isCamelCase, opType);
	}
}
