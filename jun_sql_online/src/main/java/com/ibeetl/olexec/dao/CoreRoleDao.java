package com.ibeetl.olexec.dao;

import com.ibeetl.olexec.entity.UserEntity;
import org.beetl.sql.mapper.BaseMapper;
import org.beetl.sql.mapper.annotation.*;

@SqlResource("core.coreRole")
public interface CoreRoleDao extends BaseMapper<UserEntity> {



}
