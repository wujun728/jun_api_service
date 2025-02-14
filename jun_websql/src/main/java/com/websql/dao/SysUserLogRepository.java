package com.websql.dao;

import com.websql.model.SysUserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

/**
 * @ClassName DbSqlTextRepository
 * @Description sql文本持久层
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/22 0022 2:08
 **/
public interface SysUserLogRepository extends JpaSpecificationExecutor<SysUserLog>, JpaRepository<SysUserLog, Long> {


    @Query(value = "select  count(*) FROM SYS_USERLOG WHERE USER_NAME= ?1 and login_flag  not like '%登录成功%' ", nativeQuery = true)
    Integer findUserLoginFail(String userName);

    @Transactional(rollbackFor = Exception.class)
    @Query("delete  SysUserLog WHERE userName= ?1 and loginFlag not like '%登录成功%' ")
    @Modifying
    void deleteFailUser(String userName);
}
