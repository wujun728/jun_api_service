package com.websql.service;

import com.websql.model.*;

import java.util.List;
import java.util.Map;

/**
 * @ClassName LoginService
 * @Description 登录接口
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 10:52
 **/
public interface LoginService {

    SysUser findByUserName(String userName);

    AjaxResult login(String userName, String password, String ip);

    Result<SysUserLog> getLogList(SysUserLog model);

    Boolean updateUsers(SysUser sysUser);

    Boolean updateSysSetUp(SysSetup sys);

    void initSystem();

    /**
     * 查询用户列表
     *
     * @param sysUser
     * @return
     */
    Result<SysUser> selectUserRoleList(SysUser sysUser);

    Boolean deleteUserRole(Long id);

    Boolean updateResetPassword(Long userId, String password);

    List<SysRole> queryRolesSelect();

    Boolean updateUserRole(SysUser sysUser);

    AjaxResult addUserRoleSource(SysUser sysUser);

    AjaxResult unlock(String pass);

    AjaxResult unlockLoginUser(String code);

    Result<SysDriverConfig> selectDriverConfigList(SysDriverConfig sysDriverConfig);

    AjaxResult deleteDriverConfig(Long id);

    List<Map<String, String>> findDriverConfigListSelect(String id);

    Boolean saveOrUpdateDriverConfig(SysDriverConfig sysDriverConfig);

    List<Map<String, String>> queryUsersAllBySelect();




}
