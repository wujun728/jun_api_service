package io.github.wujun728.api.core.mapper;

import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.vo.DataSourceVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 数据源 mapper接口
 * 
 * @version 1.0
 * @date 2024-05-07
 */
public interface DataSourceMapper extends BaseMapper<DataSource> {

    /**
     * 查询单个
     * @param sourceId
     * @return
     */
    public DataSourceVo selectById(@Param("sourceId") Integer sourceId);

    /**
     * 分页查询
     * @param map
     * @return
     */
    public List<DataSourceVo> selectListByMap(Map<String,Object> map);

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
