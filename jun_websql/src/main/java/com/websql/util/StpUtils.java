package com.websql.util;

import cn.dev33.satoken.exception.ApiDisabledException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.model.SysUser;
import com.websql.model.TeamSourceModel;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName : StpUtils
 * @Description : 权限工具类
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 22:40
 */
public class StpUtils {

    private final static String SESSION_USER_KEY = "login_user";

    public final static String SESSION_TEAM_KEY = "login_user_team";

    public final static String SESSION_TEAM_ACTIVE_KEY = "login_user_team_active";


    public static SysUser getCurrentUser() {
        if (!getCurrentLogin()) {
            throw new RuntimeException("当前用户暂未登录!");
        }
        return (SysUser) StpUtil.getSession().get(SESSION_USER_KEY);
    }


    public static String getCurrentUserName() {
        return getCurrentUser().getName();
    }

    public static Boolean getCurrentLogin() {
        return StpUtil.isLogin();
    }

    public static Boolean currentSuperAdmin() {
        return Long.parseLong(StpUtil.getLoginId().toString()) == 1L;
    }


    public static void currentLogout() {
        StpUtil.logout();
    }

    public static String login(SysUser user) {
        StpUtil.login(user.getUserId());
        StpUtil.getSession().set(SESSION_USER_KEY, user);
        return user.getName();
    }


    /**
     * 检查开放平台权限
     */
    public static void checkOpenAuth() {
        if (!EnvBeanUtil.getBoolean("open-api-enabled")) {
            throw new ApiDisabledException("openApi服务未开启!");
        }
        if (!ObjectUtil.equal(getRequest().getHeader("auth"), EnvBeanUtil.getString("open-api-password"))) {
            throw new ApiDisabledException("openApi密码错误!");
        }
        SysUser sysUser = new SysUser();
        sysUser.setName("openApi");
        sysUser.setUserId(999L);
        sysUser.setUserName("openApi");
        login(sysUser);
    }

    private static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
    }

    public static String getUserExtName() {
        SysUser user = getCurrentUser();
        return user.getUserId() + ":" + user.getUserName();
    }


    /**
     * 当前用户拥有的所有团队
     *
     * @return
     */
    public static List<TeamSourceModel> getTeamList() {
        Object object = StpUtil.getSession().get(SESSION_TEAM_KEY);
        if (ObjectUtil.isNotEmpty(object)) {
            return (List<TeamSourceModel>) object;
        }
        return new ArrayList<>(0);
    }

    /**
     * 当前使用的团队
     *
     * @return
     */
    public static TeamSourceModel getCurrentActiveTeam() {
        Object object = StpUtil.getSession().get(SESSION_TEAM_ACTIVE_KEY);
        if (ObjectUtil.isNotEmpty(object)) {
            return (TeamSourceModel) object;
        } else {
            List<TeamSourceModel> teamList = getTeamList();
            if (!teamList.isEmpty()) {
                Long teamId = CacheUtils.get("login_team_" + getCurrentUserName(), Long.class);
                if (ObjectUtil.isNotNull(teamId)) {
                    List<TeamSourceModel> item = teamList.stream().filter(s -> s.getId().equals(teamId)).collect(Collectors.toList());
                    if (!item.isEmpty()) {
                        StpUtil.getSession().set(SESSION_TEAM_ACTIVE_KEY, item.get(0));
                        return item.get(0);
                    }
                }
                StpUtil.getSession().set(SESSION_TEAM_ACTIVE_KEY, teamList.get(0));
                return teamList.get(0);
            }
        }
        return null;
    }


}
