package com.websql.dao;

import com.websql.model.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SysUserRepository extends JpaSpecificationExecutor<SysUser>, JpaRepository<SysUser, Long> {
    SysUser findByUserName(String userName);

    @Transactional(rollbackFor = Exception.class)
    @Query("update  SysUser set  state = ?2 WHERE userName= ?1")
    @Modifying
    public void updateStateByUserName(String userName,Integer state);
}
