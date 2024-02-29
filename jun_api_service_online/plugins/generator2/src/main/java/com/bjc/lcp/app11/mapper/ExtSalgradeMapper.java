package com.bjc.lcp.app11.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.bjc.lcp.app11.entity.ExtSalgradeEntity;
import java.util.List;
/**
 * @description Mapper
 * @author Wujun
 * @date 2024-02-28
 */
@Mapper
public interface ExtSalgradeMapper extends BaseMapper<ExtSalgradeEntity> {

    @Select(
    "<script>select t0.* from ext_salgrade t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='grade!=null and grade!=&apos;&apos; '> and t0.grade=井{grade}</when> " +
    "<when test='losal!=null and losal!=&apos;&apos; '> and t0.losal=井{losal}</when> " +
    "<when test='hisal!=null and hisal!=&apos;&apos; '> and t0.hisal=井{hisal}</when> " +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
    //add here if need page limit
    //" limit ￥{page},￥{limit} " +
    " </script>")
    List<ExtSalgradeEntity> pageAll(ExtSalgradeEntity dto,int page,int limit);

    @Select("<script>select count(1) from ext_salgrade t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='grade!=null and grade!=&apos;&apos; '> and t0.grade=井{grade}</when> " +
    "<when test='losal!=null and losal!=&apos;&apos; '> and t0.losal=井{losal}</when> " +
    "<when test='hisal!=null and hisal!=&apos;&apos; '> and t0.hisal=井{hisal}</when> " +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
     " </script>")
    int countAll(ExtSalgradeEntity dto);
    
    @Select("SELECT count(1) from ext_salgrade ")
    int countAll();

}
