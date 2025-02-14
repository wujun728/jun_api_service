package com.ibeetl.olexec.dao;

import com.ibeetl.olexec.entity.DepartmentEntity;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.*;

import java.util.List;

@SqlResource("core.coreUser")
public interface CoreUserDao extends BaseMapper<DepartmentEntity> {

    /**
     * 根据角色编码查询用户集合
     * @param roleCode 角色编码
     * @return
     */
    List<DepartmentEntity> getUserByRole(String roleCode);


}
