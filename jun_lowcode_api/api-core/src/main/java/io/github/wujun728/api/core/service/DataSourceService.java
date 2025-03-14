package io.github.wujun728.api.core.service;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.jdbc.util.DaoException;
import io.github.wujun728.api.core.dto.DataSourceDto;
import io.github.wujun728.api.core.vo.DataSourceVo;
import io.github.wujun728.api.core.dto.list.DataSourceListDto;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.common.bean.Page;

import java.util.List;
import java.util.Map;

/**
 * 数据源 service接口
 * 
 * @version 1.0
 * @date 2024-05-07
 */
public interface DataSourceService {

	/**
	 * 新增
	 *
	 * @param dataSource 新增的对象
	 * @return 新增条数
	 */
	public int insert(DataSource dataSource);

	/**
	 * 修改
	 * 
	 * @param dataSource 修改的对象
	 * @return 修改条数
	 */
	public int update(DataSource dataSource);

	/**
	 * 修改
	 *
	 * @param map
	 * @return 修改条数
	 */
	public int updateByMap(Map<String,Object> map);

	/**
	 * 删除
	 *
	 * @param sourceIds 数据源ID数组
	 * @return 删除条数
	 */
	public int delete(Integer ... sourceIds);

	/**
	 * 删除
	 *
	 * @param map
	 * @return 删除条数
	 */
	public int deleteByMap(Map<String,Object> map);

	/**
	 * 查询单个
	 * 
	 * @param sourceId 数据源ID
	 * @return 查询的对象
	 */
	public DataSourceVo selectById(Integer sourceId);

	/**
	 * 分页查询
	 * @param dataSourceListDto 查询条件
	 * @return 分页对象
	 */
	public Page<DataSourceVo> selectPage(DataSourceListDto dataSourceListDto);

	/**
	 * 查询集合
	 *
	 * @param dataSourceDto 查询条件对象
	 * @return 查询的集合
	 */
	public List<DataSourceVo> selectList(DataSourceDto dataSourceDto);


	/**
	 * 查询总数
	 *
	 * @param dataSourceDto 查询条件对象
	 * @return 查询的总数
	 */
	public long selectCount(DataSourceDto dataSourceDto);

	/**
	 * 执行SQL
	 * @param sourceId
	 * @param database
	 * @param sql    sql语句，里面可以加？
	 * @param params 参数列表，用来替换sql中的?（占位符）
	 * @return
	 * @throws DaoException
	 */
	public List<String> executeUniqueSQL(Integer sourceId, String database, String sql, List<Object> params) throws DaoException;

	/**
	 * 执行SQL
	 * @param dataSource
	 * @param database
	 * @param sql    sql语句，里面可以加？
	 * @param params 参数列表，用来替换sql中的?（占位符）
	 * @return
	 * @throws DaoException
	 */
	public List<String> executeUniqueSQL(DataSource dataSource, String database, String sql, List<Object> params) throws DaoException;

	/**
	 * 执行SQL
	 * @param sourceId
	 * @param database
	 * @param sql    sql语句，里面可以加？
	 * @param params 参数列表，用来替换sql中的?（占位符）
	 * @return
	 * @throws DaoException
	 */
	public ApiResult executeSQL(Integer sourceId, String database, String sql, List<Object> params, boolean isCamelCase, String opType);

	/**
	 * 执行SQL
	 * @param dataSource
	 * @param database
	 * @param sql    sql语句，里面可以加？
	 * @param params 参数列表，用来替换sql中的?（占位符）
	 * @return
	 * @throws DaoException
	 */
	public ApiResult executeSQL(DataSource dataSource, String database, String sql, List<Object> params, boolean isCamelCase, String opType);

}
