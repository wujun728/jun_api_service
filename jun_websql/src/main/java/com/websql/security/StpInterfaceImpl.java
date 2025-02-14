package com.websql.security;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.util.ObjectUtil;
import com.websql.dao.SysUserRoleRepository;
import com.websql.model.SysRole;
import com.websql.model.SysUserRole;
import com.websql.service.LoginService;
import com.websql.util.CacheUtils;
import com.websql.util.StpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : StpInterfaceImpl
 * @Description : 获取用户权限信息， 暂时用角色控制，未启用资源控制
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 23:02
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;


    @Autowired
    private LoginService loginService;

    @Override
    public List<String> getPermissionList(Object loginId, String s) {
        // 暂不实现按钮权限
        List<String> list = new ArrayList<String>();
        list.add("*");
        return list;
    }

    @Override
    public List<String> getRoleList(Object loginId, String s) {
        List<String> superRoles = CacheUtils.get("super_user_roles_model", List.class);
        if (StpUtils.currentSuperAdmin()) {
            if (ObjectUtil.isNull(superRoles)) {
                superRoles = loginService.queryRolesSelect().stream().map(SysRole::getRole).filter(role -> !"demo-admin".equals(role)).collect(Collectors.toList());
                CacheUtils.put("super_user_roles_model", superRoles);
            }
            return superRoles;
        }
        List<String> roles = CacheUtils.get("user_roles_model", List.class);
        if (ObjectUtil.isNull(roles)) {
            List<SysUserRole> userRole = sysUserRoleRepository.findUserRole(Long.valueOf(loginId.toString()));
            roles = userRole.stream().map(SysUserRole::getRole).collect(Collectors.toList());
            CacheUtils.put("user_roles_model", roles);
        }
        return roles;
    }
}
