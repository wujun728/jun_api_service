package com.websql.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.websql.config.DataSourceFactory;
import com.websql.dao.*;
import com.websql.model.*;
import com.websql.service.DbSourceService;
import com.websql.service.LoginService;
import com.websql.service.TeamSourceService;
import com.websql.util.CacheUtils;
import com.websql.util.PasswordUtil;
import com.websql.util.StpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName LoginServiceImpl
 * @Description 登录实现类
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/26 0026 16:45
 **/
@Service
@Log4j2
public class LoginServiceImpl implements LoginService {


    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserLogRepository sysUserLogRepository;

    @Autowired
    private SysSetUpRepository sysSetUpRepository;

    @Autowired
    private SysRoleRepository sysRoleRepository;

    @Autowired
    private SysUserRoleRepository sysUserRoleRepository;

    @Resource
    private DbSourceService dbSourceService;

    @Resource
    private SysDriverConfigRepository sysDriverConfigRepository;

    @Resource
    private TeamSourceRepository teamSourceRepository;

    @Resource
    private TeamResourceRepository teamResourceRepository;

    @Resource
    private TeamSourceService teamSourceService;


    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    @Override
    public SysUser findByUserName(String userName) {
        return sysUserRepository.findByUserName(userName);
    }

    @Override
    public AjaxResult login(String userName, String password, String ip) {
        SysUserLog log = new SysUserLog().setUserName(userName).setLogName("系统登录日志").setLogIp(ip).setLoginFlag("登录成功!").setLogDate(DateUtil.now());
        try {
            SysUser user = this.findByUserName(userName);
            if (ObjectUtil.isNull(user)) {
                log.setLoginFlag("账号或密码错误!");
                return AjaxResult.error("账号或密码错误!");
            }
            if (user.getState() != 0) {
                log.setLoginFlag("账号已被禁用!");
                return AjaxResult.error("账号已被禁用!");
            }
            String loginPass = PasswordUtil.createPassword(password, user.getUserName());
            if (!user.getPassword().equals(loginPass)) {
                log.setLoginFlag("账号或密码错误!");
                return AjaxResult.error("账号或密码错误!");
            }
            log.setUserId(user.getUserId());
            String name = StpUtils.login(user);
            List<TeamSourceModel> teamList;
            if (StpUtils.currentSuperAdmin()) {
                teamList = teamSourceService.queryValidTeamList();
            } else {
                List<TeamResourceModel> resourceModels = teamSourceService.queryTeamResourceById(Collections.singletonList(user.getUserId()), "USER");
                teamList = teamSourceService.queryTeamByIds(resourceModels.stream().map(TeamResourceModel::getTeamId).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList()));
            }
            if (ObjectUtil.isNotNull(teamList) && !teamList.isEmpty()) {
                StpUtil.getSession().set(StpUtils.SESSION_TEAM_KEY, teamList);
            }
            return AjaxResult.success("欢迎您:" + name);
        } catch (Exception e) {
            e.printStackTrace();
            log.setLoginFlag(e.getMessage());
            return AjaxResult.error(e.getMessage());
        } finally {
            if (!log.getLoginFlag().contains("登录成功")) {
                ThreadUtil.execAsync(() -> checkLoginFail(log));
            }
            sysUserLogRepository.save(log);
        }
    }

    @Override
    public Result<SysUserLog> getLogList(SysUserLog model) {
        Result<SysUserLog> result = new Result<>();
        Specification<SysUserLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(1);
            if (ObjectUtil.isNotEmpty(model.getUserName())) {
                predicates.add(cb.like(root.get("userName"), "%" + model.getUserName() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysUserLog> all = sysUserLogRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public Boolean updateUsers(SysUser sysUser) {
        SysUser sysUserById = sysUserRepository.findById(sysUser.getUserId()).get();
        if (ObjectUtil.isNotEmpty(sysUser.getName())) {
            sysUserById.setName(sysUser.getName());
        }
        if (ObjectUtil.isNotEmpty(sysUser.getEmail())) {
            sysUserById.setEmail(sysUser.getEmail());
        }
        if (ObjectUtil.isNotEmpty(sysUser.getState())) {
            sysUserById.setState(sysUser.getState());
        }
        if (ObjectUtil.isNotEmpty(sysUser.getPassword())) {
            String passNew = PasswordUtil.createPassword(sysUser.getPassword(), sysUserById.getUserName());
            sysUserById.setPassword(passNew);
        }
        sysUserRepository.save(sysUserById);
        return true;
    }

    @Override
    public Boolean updateSysSetUp(SysSetup sys) {
        SysSetup sysSetup = sysSetUpRepository.findById(sys.getId()).get();
        if (ObjectUtil.isNotEmpty(sys.getInitDataSource())) {
            sysSetup.setInitDataSource(sys.getInitDataSource());
        }
        if (ObjectUtil.isNotEmpty(sys.getEnabledSqlLog())) {
            sysSetup.setEnabledSqlLog(sys.getEnabledSqlLog());
        }
        if (ObjectUtil.isNotEmpty(sys.getEnabledHelp())) {
            sysSetup.setEnabledHelp(sys.getEnabledHelp());
        }
        if (ObjectUtil.isNotEmpty(sys.getEnabledLockView())) {
            sysSetup.setEnabledLockView(sys.getEnabledLockView());
        }
        if (ObjectUtil.isNotEmpty(sys.getPageLimitMax())) {
            sysSetup.setPageLimitMax(sys.getPageLimitMax());
        }
        if (ObjectUtil.isNotEmpty(sys.getRiskText())) {
            sysSetup.setRiskText(sys.getRiskText());
        }
        if (ObjectUtil.isNotEmpty(sys.getFailLogin())) {
            sysSetup.setFailLogin(sys.getFailLogin());
        }
        if (ObjectUtil.isNotEmpty(sys.getEnabledHint())) {
            sysSetup.setEnabledHint(sys.getEnabledHint());
        }
        if (ObjectUtil.isNotEmpty(sys.getEnabledNotification())) {
            sysSetup.setEnabledNotification(sys.getEnabledNotification());
        }
        CacheUtils.remove("sys_setup");
        sysSetUpRepository.save(sysSetup);
        CacheUtils.putNoDue("sys_setup", sysSetup);
        return true;
    }


    @Override
    public Result<SysUser> selectUserRoleList(SysUser model) {
        Result<SysUser> result = new Result<>();
        Specification<SysUser> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(3);
            if (ObjectUtil.isNotEmpty(model.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getUserName())) {
                predicates.add(cb.like(root.get("userName"), "%" + model.getUserName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getUserId())) {
                predicates.add(cb.equal(root.get("userId"), model.getUserId()));
            }
            if (ObjectUtil.isNotEmpty(model.getState())) {
                predicates.add(cb.equal(root.get("state"), model.getState()));
            }
            if (ObjectUtil.isNotEmpty(model.getEmail())) {
                predicates.add(cb.equal(root.get("email"), model.getEmail()));
            }
            query.orderBy(cb.desc(root.get("createTime")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysUser> all = sysUserRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        Map<Long, List<SysUserRole>> userRoleMap = sysUserRoleRepository.findAll().stream().collect(Collectors.groupingBy(SysUserRole::getUserId));
        for (SysUser sysUser : all) {
            if (sysUser.getState() == 0) {
                sysUser.setStateName("有效");
            } else {
                sysUser.setStateName("无效");
            }
            List<SysUserRole> sysUserRoles = userRoleMap.get(sysUser.getUserId());
            if (ObjectUtil.isNotNull(sysUserRoles)) {
                sysUser.setSysRoles(sysUserRoles);
                sysUser.setSysRoleName(sysUserRoles.stream().map(SysUserRole::getRole).collect(Collectors.joining(",")));
            }
        }
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public Boolean deleteUserRole(Long id) {
        sysUserRepository.deleteById(id);
        sysUserRoleRepository.delete(new SysUserRole().setUserId(id));
        teamResourceRepository.deleteResourceByResId(Collections.singletonList(id),"USER");
        CacheUtils.remove("user_roles_model");
        CacheUtils.remove("super_user_roles_model");
        return true;
    }

    @Override
    public Boolean updateResetPassword(Long userId, String password) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setPassword(password);
        return updateUsers(sysUser);
    }

    @Override
    public List<SysRole> queryRolesSelect() {
        List<SysRole> resultList = sysRoleRepository.findAll();
        for (SysRole sysRole : resultList) {
            sysRole.setCode(sysRole.getRoleId().toString());
            sysRole.setValue(sysRole.getRole().toString());
        }
        return resultList;
    }

    /**
     * 修改用户信息
     *
     * @param param
     * @return
     */

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateUserRole(SysUser param) {
        LoginServiceImpl loginService = (LoginServiceImpl) AopContext.currentProxy();
        loginService.updateUsers(param);
        loginService.updateUserRoles(param);
        List<String> teams = Arrays.stream(param.getSysTeamName().split(",")).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        if (!teams.isEmpty()) {
            teamSourceService.updateTeamResources(teams, Collections.singletonList(param.getUserId()), "USER");
        }
        return true;
    }

    /**
     * 保存角色信息
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateUserRoles(SysUser param) {
        if (ObjectUtil.isNotEmpty(param.getSysRoleName())) {
            CacheUtils.remove("user_roles_model");
            CacheUtils.remove("super_user_roles_model");
            List<String> roles = Arrays.asList(param.getSysRoleName().split(","));
            sysUserRoleRepository.deleteByUserId(param.getUserId());
            Map<String, SysRole> roleMap = sysRoleRepository.findAll().stream().collect(Collectors.toMap(SysRole::getRole, s -> s, (k1, k2) -> k1));
            for (String role : roles) {
                if (ObjectUtil.isNotEmpty(role)) {
                    SysUserRole sysUserRole = new SysUserRole();
                    sysUserRole.setUserId(param.getUserId());
                    sysUserRole.setRole(role);
                    sysUserRole.setRoleId(roleMap.get(role).getRoleId());
                    sysUserRoleRepository.save(sysUserRole);
                }
            }
        }
        return true;
    }


    /**
     * 新增用户角色信息
     *
     * @param param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public AjaxResult addUserRoleSource(SysUser param) {
        if (ObjectUtil.isNotNull(sysUserRepository.findByUserName(param.getUserName()))) {
            return AjaxResult.error("登录账号已经存在!");
        }
        param.setCreateTime(DateUtil.date());
        String passNew = PasswordUtil.createPassword(param.getPassword(), param.getUserName());
        param.setPassword(passNew);
        sysUserRepository.save(param);
        LoginServiceImpl loginService = (LoginServiceImpl) AopContext.currentProxy();
        loginService.updateUserRoles(param);
        List<String> teams = Arrays.stream(param.getSysTeamName().split(",")).filter(ObjectUtil::isNotEmpty).collect(Collectors.toList());
        if (!teams.isEmpty()) {
            teamSourceService.updateTeamResources(teams, Collections.singletonList(param.getUserId()), "USER");
        }
        return AjaxResult.success();
    }

    /**
     * 锁定屏幕解锁
     *
     * @param pass
     * @return
     */
    @Override
    public AjaxResult unlock(String pass) {
        SysUser currentUser = StpUtils.getCurrentUser();
        SysUser user = this.findByUserName(currentUser.getUserName());
        String loginPass = PasswordUtil.createPassword(pass, user.getUserName());
        if (!user.getPassword().equals(loginPass)) {
            return AjaxResult.error("密码错误!");
        }
        return AjaxResult.success();
    }

    /**
     * 检查账号是否超过失败错误，修改为无效状态
     *
     * @param userLog
     */
    public void checkLoginFail(SysUserLog userLog) {
        Integer count = sysUserLogRepository.findUserLoginFail(userLog.getUserName());
        SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
        Integer failLogin = 3;
        if (ObjectUtil.isNotNull(sysSetup) && ObjectUtil.isNotNull(sysSetup.getFailLogin())) {
            failLogin = sysSetup.getFailLogin();
        }
        if (count >= failLogin && failLogin > -1) {
            sysUserRepository.updateStateByUserName(userLog.getUserName(), 1);
        }
    }

    /**
     * 解锁超级管理源账号并重置密码
     *
     * @param code
     * @return
     */
    @Override
    public AjaxResult unlockLoginUser(String code) {
        sysUserRepository.updateStateByUserName("admin", 0);
        updateResetPassword(1L, code);
        sysUserLogRepository.deleteFailUser("admin");
        return AjaxResult.success();
    }

    @Override
    public Result<SysDriverConfig> selectDriverConfigList(SysDriverConfig model) {
        Result<SysDriverConfig> result = new Result<>();
        Specification<SysDriverConfig> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(3);
            if (ObjectUtil.isNotEmpty(model.getName())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getTypeName())) {
                predicates.add(cb.equal(root.get("typeName"), model.getTypeName()));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysDriverConfig> all = sysDriverConfigRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public AjaxResult deleteDriverConfig(Long id) {
        SysDriverConfig sysDriverConfig = sysDriverConfigRepository.selectById(id);
        if (ObjectUtil.isNull(sysDriverConfig)) {
            return AjaxResult.error("没有找到此驱动配置信息，请刷新页面再试!");
        }
        if (ObjectUtil.equal(sysDriverConfig.getTypeName(), "内置")) {
            return AjaxResult.error("内置驱动配置不允许删除!");
        }
        sysDriverConfigRepository.deleteById(id);
        CacheUtils.remove("driver_config_list");
        CacheUtils.remove("driver_config_data" + id);
        return AjaxResult.success("删除成功");
    }

    @Override
    public List<Map<String, String>> findDriverConfigListSelect(String id) {
        //根据ID查询单条数据
        if (ObjectUtil.isNotNull(id)) {
            SysDriverConfig sysDriverConfig = CacheUtils.get("driver_config_data" + id, SysDriverConfig.class);
            if (ObjectUtil.isNull(sysDriverConfig)) {
                sysDriverConfig = sysDriverConfigRepository.findById(Long.valueOf(id)).get();
                CacheUtils.put("driver_config_data" + id, sysDriverConfig);
            }
            return Collections.singletonList(JSON.parseObject(JSON.toJSONString(sysDriverConfig), Map.class));
        }
        List<Map<String, String>> driverList = CacheUtils.get("driver_config_list", List.class);
        if (ObjectUtil.isNull(driverList)) {
            driverList = new ArrayList<>(0);
            List<SysDriverConfig> list = sysDriverConfigRepository.findAll();
            for (SysDriverConfig dataSourceModel : list) {
                Map<String, String> item = new HashMap<>(4);
                item.put("code", dataSourceModel.getId().toString());
                item.put("value", dataSourceModel.getName());
                item.put("select", "false");
                driverList.add(item);
            }
            CacheUtils.put("driver_config_list", driverList);
        }
        return driverList;
    }

    @Override
    public Boolean saveOrUpdateDriverConfig(SysDriverConfig sysDriverConfig) {
        if (ObjectUtil.isNotNull(sysDriverConfig.getId())) {
            CacheUtils.remove("driver_config_data" + sysDriverConfig.getId());
        }
        CacheUtils.remove("driver_config_list");
        sysDriverConfigRepository.save(sysDriverConfig);
        //todo 待实现自定义加载Class驱动，增加验证驱动是否正常.
        return true;
    }


    @Override
    public List<Map<String, String>> queryUsersAllBySelect() {
        SysUser param = new SysUser();
        param.setState(0);
        List<SysUser> userList = sysUserRepository.findAll(Example.of(param));
        List<Map<String, String>> resultList = new ArrayList<>(userList.size());
        for (SysUser user : userList) {
            Map<String, String> item = new HashMap<>(3);
            item.put("code", user.getUserId().toString());
            item.put("value", user.getName());
            item.put("select", "false");
            resultList.add(item);
        }
        return resultList;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initSystem() {
        try {
            //初始化超管账号
            SysUser user = new SysUser();
            user.setPassword(PasswordUtil.createPassword("admin", "admin"));
            user.setUserId(1L);
            user.setName("超级管理员");
            user.setUserName("admin");
            user.setState(0);
            user.setCreateTime(DateUtil.date());
            sysUserRepository.save(user);

            //初始化预制角色
            SysRole role1 = new SysRole();
            role1.setRole("super-admin");
            role1.setDescription("超级管理员");
            role1.setRoleId(1L);
            role1.setRemark("至高无上的权利");
            sysRoleRepository.save(role1);

            SysRole role2 = new SysRole();
            role2.setRole("database-admin");
            role2.setDescription("数据源管理角色");
            role2.setRoleId(2L);
            role2.setRemark("所属数据源管理菜单");
            sysRoleRepository.save(role2);

            SysRole role3 = new SysRole();
            role3.setRole("sql-admin");
            role3.setDescription("SQL管理角色");
            role3.setRoleId(3L);
            role3.setRemark("所属SQL管理菜单");
            sysRoleRepository.save(role3);

            SysRole role4 = new SysRole();
            role4.setRole("log-admin");
            role4.setDescription("日志管理角色");
            role4.setRoleId(4L);
            role4.setRemark("所属日志管理菜单");
            sysRoleRepository.save(role4);

            SysRole role5 = new SysRole();
            role5.setRole("timing-admin");
            role5.setDescription("作业管理角色");
            role5.setRoleId(5L);
            role5.setRemark("所属作业管理菜单");
            sysRoleRepository.save(role5);

            SysRole role6 = new SysRole();
            role6.setRole("demo-admin");
            role6.setDescription("系统演示角色");
            role6.setRoleId(6L);
            role6.setRemark("系统演示功能");
            sysRoleRepository.save(role6);

            //初始化人员角色
            SysUserRole sysUserRole1 = new SysUserRole();
            sysUserRole1.setUserId(user.getUserId());
            sysUserRole1.setRole(role1.getRole());
            sysUserRole1.setRoleId(role1.getRoleId());
            sysUserRoleRepository.save(sysUserRole1);

            //初始化团队信息
            TeamSourceModel teamSourceModel = new TeamSourceModel();
            teamSourceModel.setId(1L);
            teamSourceModel.setCreateTime(DateUtil.date());
            teamSourceModel.setDescription("Default");
            teamSourceModel.setTeamName("Default");
            teamSourceModel.setUserId(1L);
            teamSourceModel.setState(0);
            teamSourceRepository.save(teamSourceModel);

            //初始化团队资源
            TeamResourceModel resourceModel = new TeamResourceModel();
            resourceModel.setId(1L);
            resourceModel.setTeamId(1L);
            resourceModel.setRoleType("负责人");
            resourceModel.setResourceId(1L);
            resourceModel.setResourceType("USER");
            resourceModel.setCreateTime(DateUtil.date());
            teamResourceRepository.save(resourceModel);

            //初始化系统设置
            SysSetup sysSetup = SysSetup.getInstance();
            sysSetup.setId(1L);
            sysSetup.setEnabledSqlLog(1);
            sysSetup.setInitDataSource(1);
            sysSetup.setEnabledHelp(0);
            sysSetup.setEnabledLockView(0);
            sysSetup.setPageLimitMax(1000);
            sysSetup.setFailLogin(3);
            sysSetup.setRiskText("drop,truncate,delete,create");
            sysSetup.setEnabledHint(0);
            sysSetup.setEnabledNotification(0);
            sysSetUpRepository.save(sysSetup);
            //初始化内置的驱动
            List<SysDriverConfig> sysDriverConfig = JSON.parseArray(ResourceUtil.readUtf8Str("dataSourceTemplate.json"), SysDriverConfig.class);
            sysDriverConfigRepository.saveAll(sysDriverConfig);
            //初始化默认数据源H2
            DataSourceModel model = new DataSourceModel()
                    .setDbName("DEFAULT-H2")
                    .setDriverClass(driverClassName)
                    .setDbUrl(url)
                    .setDbCheckUrl("select 1")
                    .setDbAccount(username)
                    .setDbPassword(password)
                    .setInitialSize(1)
                    .setMaxActive(5)
                    .setMaxIdle(5)
                    .setMaxWait(20)
                    .setDbState("有效");
            DataSourceFactory.saveDataSource(model);
            dbSourceService.addDbSource(model,teamSourceModel.getId());
        } catch (Exception e) {
            log.error("初始化系统报错" + e.getMessage());
            e.printStackTrace();
        }
    }


}
