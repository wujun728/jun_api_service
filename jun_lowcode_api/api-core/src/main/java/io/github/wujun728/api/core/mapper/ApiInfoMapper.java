package io.github.wujun728.api.core.mapper;

import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.vo.ApiBaseVo;
import io.github.wujun728.api.core.vo.ApiInfoVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * API信息 mapper接口
 * 
 * @version 1.0
 * @date 2024-05-08
 */
public interface ApiInfoMapper extends BaseMapper<ApiInfo> {

    /**
     * 查询单个
     * @param apiId
     * @return
     */
    public ApiInfoVo selectById(@Param("apiId") Integer apiId);

    /**
     * 分页查询
     * @param map
     * @return
     */
    public List<ApiInfoVo> selectListByMap(Map<String,Object> map);

    /**
     * 查询
     * @param map
     * @return
     */
    public List<ApiBaseVo> selectBaseListByMap(Map<String,Object> map);

    /**
     * 查询
     * @param map
     * @return
     */
    public List<String> selectApiGroups(Map<String,Object> map);



    /**
     * 查询总数
     * @param map
     * @return
     */
    public long selectCountByMap(Map<String,Object> map);

    /**
     * 修改
     * @param map
     * @return
     */
    public int updateByMap(Map<String,Object> map);

    /**
     * 删除
     * @param map
     * @return
     */
    @Override
    public int deleteByMap(Map<String,Object> map);
}
