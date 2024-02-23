package com.jun.plugin.generator.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.jun.plugin.generator.entity.SysCodeGenerateConfigEntity;
import java.util.List;
/**
 * @description 代码生成详细配置Mapper
 * @author Wujun
 * @date 2023-10-09
 */
@Mapper
public interface SysCodeGenerateConfigMapper extends BaseMapper<SysCodeGenerateConfigEntity> {

    @Select(
    "<script>select t0.* from sys_code_generate_config t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
    "<when test='codeGenId!=null and codeGenId!=&apos;&apos; '> and t0.code_gen_id=井{codeGenId}</when> " +
    "<when test='columnName!=null and columnName!=&apos;&apos; '> and t0.column_name=井{columnName}</when> " +
    "<when test='javaName!=null and javaName!=&apos;&apos; '> and t0.java_name=井{javaName}</when> " +
    "<when test='dataType!=null and dataType!=&apos;&apos; '> and t0.data_type=井{dataType}</when> " +
    "<when test='columnComment!=null and columnComment!=&apos;&apos; '> and t0.column_comment=井{columnComment}</when> " +
    "<when test='javaType!=null and javaType!=&apos;&apos; '> and t0.java_type=井{javaType}</when> " +
    "<when test='effectType!=null and effectType!=&apos;&apos; '> and t0.effect_type=井{effectType}</when> " +
    "<when test='dictTypeCode!=null and dictTypeCode!=&apos;&apos; '> and t0.dict_type_code=井{dictTypeCode}</when> " +
    "<when test='whetherTable!=null and whetherTable!=&apos;&apos; '> and t0.whether_table=井{whetherTable}</when> " +
    "<when test='whetherAddUpdate!=null and whetherAddUpdate!=&apos;&apos; '> and t0.whether_add_update=井{whetherAddUpdate}</when> " +
    "<when test='whetherRetract!=null and whetherRetract!=&apos;&apos; '> and t0.whether_retract=井{whetherRetract}</when> " +
    "<when test='whetherRequired!=null and whetherRequired!=&apos;&apos; '> and t0.whether_required=井{whetherRequired}</when> " +
    "<when test='queryWhether!=null and queryWhether!=&apos;&apos; '> and t0.query_whether=井{queryWhether}</when> " +
    "<when test='queryType!=null and queryType!=&apos;&apos; '> and t0.query_type=井{queryType}</when> " +
    "<when test='columnKey!=null and columnKey!=&apos;&apos; '> and t0.column_key=井{columnKey}</when> " +
    "<when test='columnKeyName!=null and columnKeyName!=&apos;&apos; '> and t0.column_key_name=井{columnKeyName}</when> " +
    "<when test='whetherCommon!=null and whetherCommon!=&apos;&apos; '> and t0.whether_common=井{whetherCommon}</when> " +
    "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=井{createTime}</when> " +
    "<when test='createUser!=null and createUser!=&apos;&apos; '> and t0.create_user=井{createUser}</when> " +
    "<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=井{updateTime}</when> " +
    "<when test='updateUser!=null and updateUser!=&apos;&apos; '> and t0.update_user=井{updateUser}</when> " +
    //add here if need page limit
    //" limit ￥{page},￥{limit} " +
    " </script>")
    List<SysCodeGenerateConfigEntity> pageAll(SysCodeGenerateConfigEntity dto,int page,int limit);

    @Select("<script>select count(1) from sys_code_generate_config t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
    "<when test='codeGenId!=null and codeGenId!=&apos;&apos; '> and t0.code_gen_id=井{codeGenId}</when> " +
    "<when test='columnName!=null and columnName!=&apos;&apos; '> and t0.column_name=井{columnName}</when> " +
    "<when test='javaName!=null and javaName!=&apos;&apos; '> and t0.java_name=井{javaName}</when> " +
    "<when test='dataType!=null and dataType!=&apos;&apos; '> and t0.data_type=井{dataType}</when> " +
    "<when test='columnComment!=null and columnComment!=&apos;&apos; '> and t0.column_comment=井{columnComment}</when> " +
    "<when test='javaType!=null and javaType!=&apos;&apos; '> and t0.java_type=井{javaType}</when> " +
    "<when test='effectType!=null and effectType!=&apos;&apos; '> and t0.effect_type=井{effectType}</when> " +
    "<when test='dictTypeCode!=null and dictTypeCode!=&apos;&apos; '> and t0.dict_type_code=井{dictTypeCode}</when> " +
    "<when test='whetherTable!=null and whetherTable!=&apos;&apos; '> and t0.whether_table=井{whetherTable}</when> " +
    "<when test='whetherAddUpdate!=null and whetherAddUpdate!=&apos;&apos; '> and t0.whether_add_update=井{whetherAddUpdate}</when> " +
    "<when test='whetherRetract!=null and whetherRetract!=&apos;&apos; '> and t0.whether_retract=井{whetherRetract}</when> " +
    "<when test='whetherRequired!=null and whetherRequired!=&apos;&apos; '> and t0.whether_required=井{whetherRequired}</when> " +
    "<when test='queryWhether!=null and queryWhether!=&apos;&apos; '> and t0.query_whether=井{queryWhether}</when> " +
    "<when test='queryType!=null and queryType!=&apos;&apos; '> and t0.query_type=井{queryType}</when> " +
    "<when test='columnKey!=null and columnKey!=&apos;&apos; '> and t0.column_key=井{columnKey}</when> " +
    "<when test='columnKeyName!=null and columnKeyName!=&apos;&apos; '> and t0.column_key_name=井{columnKeyName}</when> " +
    "<when test='whetherCommon!=null and whetherCommon!=&apos;&apos; '> and t0.whether_common=井{whetherCommon}</when> " +
    "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=井{createTime}</when> " +
    "<when test='createUser!=null and createUser!=&apos;&apos; '> and t0.create_user=井{createUser}</when> " +
    "<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=井{updateTime}</when> " +
    "<when test='updateUser!=null and updateUser!=&apos;&apos; '> and t0.update_user=井{updateUser}</when> " +
     " </script>")
    int countAll(SysCodeGenerateConfigEntity dto);
    
    @Select("SELECT count(1) from sys_code_generate_config ")
    int countAll();

}
