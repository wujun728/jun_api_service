package com.jun.plugin.generator.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.jun.plugin.generator.entity.SysCodeGenerateEntity;
import java.util.List;
/**
 * @description 代码生成基础配置Mapper
 * @author Wujun
 * @date 2023-10-09
 */
@Mapper
public interface SysCodeGenerateMapper extends BaseMapper<SysCodeGenerateEntity> {

    @Select(
    "<script>select t0.* from sys_code_generate t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
    "<when test='authorName!=null and authorName!=&apos;&apos; '> and t0.author_name=井{authorName}</when> " +
    "<when test='clasname!=null and clasname!=&apos;&apos; '> and t0.class_name=井{clasname}</when> " +
    "<when test='tablePrefix!=null and tablePrefix!=&apos;&apos; '> and t0.table_prefix=井{tablePrefix}</when> " +
    "<when test='generateType!=null and generateType!=&apos;&apos; '> and t0.generate_type=井{generateType}</when> " +
    "<when test='tableName!=null and tableName!=&apos;&apos; '> and t0.table_name=井{tableName}</when> " +
    "<when test='packageName!=null and packageName!=&apos;&apos; '> and t0.package_name=井{packageName}</when> " +
    "<when test='buname!=null and buname!=&apos;&apos; '> and t0.bus_name=井{buname}</when> " +
    "<when test='tableComment!=null and tableComment!=&apos;&apos; '> and t0.table_comment=井{tableComment}</when> " +
    "<when test='appCode!=null and appCode!=&apos;&apos; '> and t0.app_code=井{appCode}</when> " +
    "<when test='menuPid!=null and menuPid!=&apos;&apos; '> and t0.menu_pid=井{menuPid}</when> " +
    "<when test='createUser!=null and createUser!=&apos;&apos; '> and t0.create_user=井{createUser}</when> " +
    "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=井{createTime}</when> " +
    "<when test='updateUser!=null and updateUser!=&apos;&apos; '> and t0.update_user=井{updateUser}</when> " +
    "<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=井{updateTime}</when> " +
    //add here if need page limit
    //" limit ￥{page},￥{limit} " +
    " </script>")
    List<SysCodeGenerateEntity> pageAll(SysCodeGenerateEntity dto,int page,int limit);

    @Select("<script>select count(1) from sys_code_generate t0 " +
    //add here if need left join
    "where 1=1" +
    "<when test='id!=null and id!=&apos;&apos; '> and t0.id=井{id}</when> " +
    "<when test='authorName!=null and authorName!=&apos;&apos; '> and t0.author_name=井{authorName}</when> " +
    "<when test='clasname!=null and clasname!=&apos;&apos; '> and t0.class_name=井{clasname}</when> " +
    "<when test='tablePrefix!=null and tablePrefix!=&apos;&apos; '> and t0.table_prefix=井{tablePrefix}</when> " +
    "<when test='generateType!=null and generateType!=&apos;&apos; '> and t0.generate_type=井{generateType}</when> " +
    "<when test='tableName!=null and tableName!=&apos;&apos; '> and t0.table_name=井{tableName}</when> " +
    "<when test='packageName!=null and packageName!=&apos;&apos; '> and t0.package_name=井{packageName}</when> " +
    "<when test='buname!=null and buname!=&apos;&apos; '> and t0.bus_name=井{buname}</when> " +
    "<when test='tableComment!=null and tableComment!=&apos;&apos; '> and t0.table_comment=井{tableComment}</when> " +
    "<when test='appCode!=null and appCode!=&apos;&apos; '> and t0.app_code=井{appCode}</when> " +
    "<when test='menuPid!=null and menuPid!=&apos;&apos; '> and t0.menu_pid=井{menuPid}</when> " +
    "<when test='createUser!=null and createUser!=&apos;&apos; '> and t0.create_user=井{createUser}</when> " +
    "<when test='createTime!=null and createTime!=&apos;&apos; '> and t0.create_time=井{createTime}</when> " +
    "<when test='updateUser!=null and updateUser!=&apos;&apos; '> and t0.update_user=井{updateUser}</when> " +
    "<when test='updateTime!=null and updateTime!=&apos;&apos; '> and t0.update_time=井{updateTime}</when> " +
     " </script>")
    int countAll(SysCodeGenerateEntity dto);
    
    @Select("SELECT count(1) from sys_code_generate ")
    int countAll();

}
