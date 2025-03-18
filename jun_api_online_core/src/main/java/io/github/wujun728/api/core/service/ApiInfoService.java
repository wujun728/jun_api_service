package io.github.wujun728.api.core.service;

import io.github.wujun728.api.core.dto.ApiInfoDto;
import io.github.wujun728.api.core.dto.add.ApiBatchAddDto;
import io.github.wujun728.api.core.dto.list.ApiInfoListDto;
import io.github.wujun728.api.core.vo.ApiInfoTreeVo;
import io.github.wujun728.api.core.vo.ApiInfoVo;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.common.bean.Page;

import java.util.List;
import java.util.Map;

/**
 * API信息 service接口
 * 
 * @version 1.0
 * @date 2024-05-08
 */
public interface ApiInfoService {

	/**
	 * 新增
	 *
	 * @param apiInfo 新增的对象
	 * @return 新增条数
	 */
	public int insert(ApiInfo apiInfo);

	/**
	 * 新增
	 *
	 * @param apiInfo 新增的对象
	 * @return 新增条数
	 */
	public int insert(ApiInfo apiInfo, boolean batch);


	/**
	 * 新增
	 *
	 * @param apiBatchAddDto 新增的对象
	 * @return 新增条数
	 */
	public void batchInsert(ApiBatchAddDto apiBatchAddDto) throws Exception;


	/**
	 * 修改
	 * 
	 * @param apiInfo 修改的对象
	 * @return 修改条数
	 */
	public int update(ApiInfo apiInfo);

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
	 * @param apiIds 接口ID数组
	 * @return 删除条数
	 */
	public int delete(Integer ... apiIds);

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
	 * @param apiId 接口ID
	 * @return 查询的对象
	 */
	public ApiInfoVo selectById(Integer apiId);

	/**
	 * 分页查询
	 * @param apiInfoListDto 查询条件
	 * @return 分页对象
	 */
	public Page<ApiInfoVo> selectPage(ApiInfoListDto apiInfoListDto);

	/**
	 * 查询集合
	 *
	 * @param apiInfoListDto 查询条件对象
	 * @return 查询的集合
	 */
	public List<ApiInfoTreeVo> selectApiTrees(ApiInfoListDto apiInfoListDto);

	/**
	 * 查询集合
	 *
	 * @return 查询的集合
	 */
	public List<String> selectApiGroups(Integer sourceId, String databaseName);

	/**
	 * 查询集合
	 *
	 * @param apiInfoDto 查询条件对象
	 * @return 查询的集合
	 */
	public List<ApiInfoVo> selectList(ApiInfoDto apiInfoDto);


	/**
	 * 查询总数
	 *
	 * @param apiInfoDto 查询条件对象
	 * @return 查询的总数
	 */
	public long selectCount(ApiInfoDto apiInfoDto);

}
