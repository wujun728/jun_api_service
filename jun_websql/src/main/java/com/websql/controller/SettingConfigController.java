package com.websql.controller;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.websql.config.DbSourceFactory;
import com.websql.model.*;
import com.websql.service.DbSourceService;
import com.websql.service.LoginService;
import com.websql.service.MessageTemplateService;
import com.websql.service.TeamSourceService;
import com.websql.task.ExamineVersionFactory;
import com.websql.util.EnvBeanUtil;
import com.websql.util.PasswordUtil;
import com.websql.util.StpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName : SettingConfigController
 * @Description : 系统管理
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/29 17:24
 */
@Controller
@RequestMapping("/settingManager")
@Slf4j
public class SettingConfigController {


    @Autowired
    private DbSourceFactory dbSourceFactory;


    @Autowired
    private LoginService loginService;

    @Autowired
    private ExamineVersionFactory examineVersionFactory;

    @Autowired
    private TeamSourceService teamSourceService;

    @Autowired
    private DbSourceService dbSourceService;

    @Autowired
    private MessageTemplateService messageTemplateService;


    @RequestMapping("/userRolePage")
    public String userRolePage() {
        return "userRolePage";
    }


    @RequestMapping("/addUserPage")
    public String addUserPage() {
        return "addUserPage";
    }

    @RequestMapping("/addTeamPage")
    public String addTeamPage() {
        return "addTeamPage";
    }


    @RequestMapping("/teamManagerPage")
    public String teamManagerPage() {
        return "teamListPage";
    }

    @RequestMapping("/exportLogPage")
    public String timingAddPage() {
        return "exportLogPage";
    }

    @RequestMapping("/addMessageTemplatePage")
    public String addMessageTemplatePage() {
        return "addMessageTemplatePage";
    }


    @RequestMapping("/showTeamResourcePage/{id}")
    public ModelAndView showTeamResourcePage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("showTeamResourcePage");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    @RequestMapping("/addDriverConfigPage")
    public ModelAndView addDriverConfigPage(@RequestParam(required = false) Long id) {
        ModelAndView modelAndView = new ModelAndView("addDriverConfigPage");
        modelAndView.addObject("object", new SysDriverConfig());
        if (ObjectUtil.isNotNull(id)) {
            List<Map<String, String>> driverConfigListSelect = loginService.findDriverConfigListSelect(String.valueOf(id));
            if (driverConfigListSelect.size() > 0) {
                modelAndView.addObject("object", driverConfigListSelect.get(0));
            }
        }
        return modelAndView;
    }


    @RequestMapping("/dataBaseConsolePage")
    public ModelAndView dataBaseConsolePage() {
        ModelAndView modelAndView = new ModelAndView("redirect:/h2-console");
        if (!EnvBeanUtil.getBoolean("spring.h2.console.enabled")) {
            modelAndView.setViewName("main");
            modelAndView.addObject("errorMsg", "已关闭数据库控制台，请查看帮助手册修改开启!");
        }
        return modelAndView;
    }

    @RequestMapping("/druidConsolePage")
    public ModelAndView druidConsolePage() {
        ModelAndView modelAndView = new ModelAndView("redirect:/druid");
        if (!EnvBeanUtil.getBoolean("druid.login.enabled")) {
            modelAndView.setViewName("main");
            modelAndView.addObject("errorMsg", "已关闭连接池控制台，请查看帮助手册修改开启!");
        }
        return modelAndView;
    }

    @RequestMapping("/driverConfigPage")
    public String driverConfigPage() {
        return "sysDriverConfigPage";
    }

    @RequestMapping("/updateUserRolesPage/{id}")
    public ModelAndView updateUserRolesPage(@PathVariable Long id) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(id);
        Result<SysUser> sysUserResult = loginService.selectUserRoleList(sysUser);
        if (sysUserResult.getList().isEmpty()) {
            throw new RuntimeException("没有找到用户信息，请重试!");
        }
        List<TeamResourceModel> resourceModels = teamSourceService.queryTeamResourceById(Collections.singletonList(id), "USER");
        String teams = resourceModels.stream().filter(s -> ObjectUtil.isNotEmpty(s.getTeamId())).map(s -> s.getTeamId().toString()).collect(Collectors.joining(","));
        return new ModelAndView("updateUserRolesPage").addObject("user", sysUserResult.getList().get(0)).addObject("teams", teams);
    }


    @RequestMapping("/sysSetUpPage")
    public ModelAndView sysSetUpPage() {
        ModelAndView mav = new ModelAndView("sysSetUpPage");
        SysSetup sysSetup = dbSourceFactory.getSysSetUp();
        mav.addObject("obj", sysSetup);
        mav.addObject("version", examineVersionFactory.getVersionModel());
        return mav;
    }

    @RequestMapping("/updateSysSetUp")
    @ResponseBody
    public AjaxResult updateSysSetUp(@RequestBody SysSetup sys) {
        return AjaxResult.success(loginService.updateSysSetUp(sys));
    }


    @RequestMapping("/userRoleList")
    @ResponseBody
    public AjaxResult userRoleList(SysUser sysUser) {
        return AjaxResult.success(loginService.selectUserRoleList(sysUser));
    }

    @RequestMapping("/deleteUserRole/{id}")
    @ResponseBody
    public AjaxResult deleteUserRole(@PathVariable Long id) {
        if (ObjectUtil.isNull(id)) {
            return AjaxResult.error("必填参数不能为空!");
        }
        if (!StpUtils.currentSuperAdmin()) {
            return AjaxResult.error("仅允许超级管理员账号删除用户!");
        }
        if (id == 1L) {
            return AjaxResult.error("超级管理员用户不允许删除!");
        }
        return AjaxResult.success(loginService.deleteUserRole(id));
    }


    @RequestMapping("/updateResetPassword")
    @ResponseBody
    public AjaxResult updateResetPassword(@RequestBody Map<String, String> body) {
        if (ObjectUtil.isNull(body) || ObjectUtil.isEmpty(body) ||
                ObjectUtil.isNull(body.get("userId")) || ObjectUtil.isNull(body.get("password"))) {
            return AjaxResult.error("必填参数不能为空!");
        }
        return AjaxResult.success(loginService.updateResetPassword(Long.valueOf(body.get("userId")), body.get("password")));
    }

    @RequestMapping("/queryRolesSelect")
    @ResponseBody
    public AjaxResult queryRolesSelect() {
        return AjaxResult.success(loginService.queryRolesSelect());
    }

    @RequestMapping("/updateUserRole")
    @ResponseBody
    public AjaxResult updateUserRole(@RequestBody SysUser sysUser) {
        return AjaxResult.success(loginService.updateUserRole(sysUser));
    }

    @RequestMapping("/addUserRoleSource")
    @ResponseBody
    public AjaxResult addUserRoleSource(@RequestBody SysUser sysUser) {
        if (ObjectUtil.isEmpty(sysUser.getName())) {
            return AjaxResult.error("名称不能为空!");
        }
        if (ObjectUtil.isEmpty(sysUser.getUserName())) {
            return AjaxResult.error("登录账号不能为空!");
        }
        if (ObjectUtil.isEmpty(sysUser.getPassword())) {
            return AjaxResult.error("登录密码不能为空!");
        }
        if (ObjectUtil.isEmpty(sysUser.getSysRoleName())) {
            return AjaxResult.error("请选择角色信息！!");
        }
        if (ObjectUtil.isEmpty(sysUser.getSysTeamName())) {
            return AjaxResult.error("请选择团队信息！!");
        }
        return loginService.addUserRoleSource(sysUser);
    }


    @RequestMapping("/driverConfigList")
    @ResponseBody
    public AjaxResult driverConfigList(SysDriverConfig sysDriverConfig) {
        return AjaxResult.success(loginService.selectDriverConfigList(sysDriverConfig));
    }

    @RequestMapping("/deleteDriverConfig/{id}")
    @ResponseBody
    public AjaxResult deleteDriverConfig(@PathVariable Long id) {
        if (ObjectUtil.isNull(id)) {
            return AjaxResult.error("必填参数不能为空!");
        }
        return loginService.deleteDriverConfig(id);
    }

    @RequestMapping("/saveOrUpdateDriverConfig")
    @ResponseBody
    public AjaxResult saveOrUpdateDriverConfig(@RequestBody SysDriverConfig sysDriverConfig) {
        return AjaxResult.success(loginService.saveOrUpdateDriverConfig(sysDriverConfig));
    }


    @RequestMapping("/queryTeamList")
    @ResponseBody
    public AjaxResult queryTeamList(TeamSourceModel teamSourceModel) {
        return AjaxResult.success(teamSourceService.selectTeamList(teamSourceModel));
    }

    @RequestMapping("/addTeamSource")
    @ResponseBody
    public AjaxResult addTeamSource(@RequestBody TeamSourceModel teamSourceModel) {
        if (ObjectUtil.isEmpty(teamSourceModel.getTeamName())) {
            return AjaxResult.error("团队名称不能为空!");
        }
        if (ObjectUtil.isEmpty(teamSourceModel.getUserId())) {
            return AjaxResult.error("团队负责人不能为空!");
        }
        return teamSourceService.addTeamSource(teamSourceModel);
    }

    @RequestMapping("/deleteTeam/{id}")
    @ResponseBody
    public AjaxResult deleteTeam(@PathVariable Long id) {
        if (ObjectUtil.isNull(id)) {
            return AjaxResult.error("必填参数不能为空!");
        }
        return teamSourceService.deleteTeam(id);
    }

    /***
     * 加载人员数据源
     * @return
     */
    @RequestMapping("/queryTeamAllBySelect")
    @ResponseBody
    public Map queryTeamAllBySelect() {
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", teamSourceService.queryTeamAllBySelect());
        return result;
    }


    @RequestMapping("/queryTeamResourceList")
    @ResponseBody
    public AjaxResult queryTeamResourceList(@RequestParam Long id) {
        return AjaxResult.success(teamSourceService.queryTeamResourceList(id));
    }


    @RequestMapping("/reloadDataSourceAll")
    @ResponseBody
    public AjaxResult reloadDataSourceAll() {
        int size = dbSourceFactory.initDataSource();
        return AjaxResult.success(size);
    }

    /**
     * 下载数据源
     *
     * @return
     * @throws
     */
    @GetMapping("/downloadDataSourceJson")
    public ResponseEntity downloadDataSourceJson() throws IOException {
        List<DataSourceModel> dataSourceModels = dbSourceService.reloadDataSourceList();
        if (dataSourceModels.isEmpty()) {
            return ResponseEntity.ok("数据源信息中没有数据，无法导出!");
        }
        //转换对应团队信息
        List<Long> ids = dataSourceModels.stream().map(DataSourceModel::getId).collect(Collectors.toList());
        List<TeamResourceModel> resourceModels = teamSourceService.queryTeamResourceById(ids, "DATASOURCE");
        Map<Long, Long> teamMap = resourceModels.stream().collect(Collectors.toMap(TeamResourceModel::getResourceId, TeamResourceModel::getTeamId));
        List<Map<String, Object>> resultList = new ArrayList<>(dataSourceModels.size());
        for (DataSourceModel dataSourceModel : dataSourceModels) {
            String userName = PasswordUtil.decrypt(dataSourceModel.getDbAccount());
            String password = PasswordUtil.decrypt(dataSourceModel.getDbPassword());
            Map<String, Object> item = new HashMap<>(10);
            item.put("title", Base64Encoder.encode(dataSourceModel.getDbName()));
            item.put("url", Base64Encoder.encode(dataSourceModel.getDbUrl()));
            item.put("userName", Base64Encoder.encode(userName));
            item.put("password", Base64Encoder.encode(password));
            item.put("checkSql", dataSourceModel.getDbCheckUrl());
            item.put("driver", dataSourceModel.getDriverClass());
            item.put("initialSize", dataSourceModel.getInitialSize());
            item.put("maxActive", dataSourceModel.getMaxActive());
            item.put("maxIdle", dataSourceModel.getMaxIdle());
            item.put("maxWait", dataSourceModel.getMaxWait());
            item.put("teamId", teamMap.get(dataSourceModel.getId()));
            item.put("version", examineVersionFactory.getVersionModel().getLocalVersion());
            resultList.add(item);
        }
        String fileName = "webSql_DataSource.json";
        Path tempFile = Files.createTempFile(fileName, "");
        try {
            Files.write(tempFile, JSONUtil.toJsonStr(resultList).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(tempFile))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @GetMapping("/downloadSqlTextJson")
    public ResponseEntity downloadSqlTextJson() throws IOException {
        List<DbSqlText> dataSourceModels = dbSourceService.sqlTextListAll();
        if (dataSourceModels.isEmpty()) {
            return ResponseEntity.ok("sql文本信息没有数据，无法导出!");
        }
        List<Map<String, Object>> resultList = new ArrayList<>(dataSourceModels.size());
        for (DbSqlText sqlText : dataSourceModels) {
            Map<String, Object> item = new HashMap<>(10);
            item.put("title", Base64Encoder.encode(sqlText.getTitle()));
            item.put("content", Base64Encoder.encode(sqlText.getSqlText()));
            item.put("teamId", sqlText.getTeamId());
            item.put("version", examineVersionFactory.getVersionModel().getLocalVersion());
            resultList.add(item);
        }
        String fileName = "webSql_SqlTextSource.json";
        Path tempFile = Files.createTempFile(fileName, "");
        try {
            Files.write(tempFile, JSONUtil.toJsonStr(resultList).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(tempFile))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    @GetMapping("/downloadTeamJson")
    public ResponseEntity downloadTeamJson() throws IOException {
        List<TeamSourceModel> teamSourceModelList = teamSourceService.selectTeamListAll();
        if (teamSourceModelList.isEmpty()) {
            return ResponseEntity.ok("团队信息没有数据，无法导出!");
        }
        if (teamSourceModelList.size() == 1) {
            TeamSourceModel teamSourceModel = teamSourceModelList.get(0);
            if (teamSourceModel.getId() == 1 && "Default".equals(teamSourceModel.getTeamName())) {
                return ResponseEntity.ok("您只有一个默认团队,不需要导出备份!");
            }
        }
        List<Map<String, Object>> resultList = new ArrayList<>(teamSourceModelList.size());
        for (TeamSourceModel team : teamSourceModelList) {
            if (team.getId() == 1 && "Default".equals(team.getTeamName())) {
                continue;
            }
            Map<String, Object> item = new HashMap<>(10);
            item.put("userId", team.getUserId());
            item.put("teamName", Base64Encoder.encode(team.getTeamName()));
            item.put("description", Base64Encoder.encode(team.getDescription()));
            item.put("state", team.getState());
            item.put("id", team.getId());
            item.put("version", examineVersionFactory.getVersionModel().getLocalVersion());
            resultList.add(item);
        }
        String fileName = "webSql_TeamDataSource.json";
        Path tempFile = Files.createTempFile(fileName, "");
        try {
            Files.write(tempFile, JSONUtil.toJsonStr(resultList).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(Files.size(tempFile))
                .contentType(MediaType.APPLICATION_JSON)
                .body(resource);
    }

    /**
     * 上传数据源信息
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadDataSourceJson")
    @ResponseBody
    public AjaxResult uploadDataSourceJson(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.error("没有解析出文件!");
        }
        try {
            JSONArray dataSourceJson = JSONUtil.parseArray(new String(file.getBytes(), StandardCharsets.UTF_8));
            if (dataSourceJson.isEmpty()) {
                return AjaxResult.error("没有解析出json数据，请检查！");
            }
            StringBuilder message = new StringBuilder();
            Boolean flag = true;
            for (Object object : dataSourceJson) {
                Map<String, Object> map = (Map<String, Object>) object;
                DataSourceModel model = new DataSourceModel();
                model.setDbName(Base64Decoder.decodeStr(MapUtil.getStr(map, "title")));
                model.setDbUrl(Base64Decoder.decodeStr(MapUtil.getStr(map, "url")));
                model.setDbAccount(Base64Decoder.decodeStr(MapUtil.getStr(map, "userName")));
                model.setDbPassword(Base64Decoder.decodeStr(MapUtil.getStr(map, "password")));
                model.setDbCheckUrl(MapUtil.getStr(map, "checkSql"));
                model.setDriverClass(MapUtil.getStr(map, "driver"));
                model.setInitialSize(MapUtil.getInt(map, "initialSize"));
                model.setMaxActive(MapUtil.getInt(map, "maxActive"));
                model.setMaxIdle(MapUtil.getInt(map, "maxIdle"));
                model.setMaxWait(MapUtil.getInt(map, "maxWait"));
                model.setDbState("有效");
                try {
                    //导入的历史团队id不存在，将赋值给当前选中的团队。
                    Long teamId = MapUtil.getLong(map, "teamId");
                    List<TeamSourceModel> teamSourceModels = teamSourceService.queryTeamByIds(Collections.singletonList(teamId));
                    if (teamSourceModels.isEmpty()) {
                        teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
                    }
                    dbSourceService.addDbSource(model, teamId);
                    message.append("[").append(model.getDbName()).append("]上传成功!").append("<br>");
                } catch (Exception e) {
                    flag = false;
                    message.append("[").append(model.getDbName()).append("]上传失败,原因：").append(e.getMessage()).append("<br>");
                }
            }
            return flag ? AjaxResult.success("共导入" + dataSourceJson.size() + "条数据!") : AjaxResult.error(message.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败!" + e.getMessage());
        }
    }

    /**
     * 上传sql文本数据
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadSqlTextJson")
    @ResponseBody
    public AjaxResult uploadSqlTextJson(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.error("没有解析出文件!");
        }
        try {
            JSONArray dataSourceJson = JSONUtil.parseArray(new String(file.getBytes(), StandardCharsets.UTF_8));
            if (dataSourceJson.isEmpty()) {
                return AjaxResult.error("没有解析出json数据，请检查！");
            }
            for (Object object : dataSourceJson) {
                Map<String, Object> map = (Map<String, Object>) object;
                DbSqlText dbSqlText = new DbSqlText();
                dbSqlText.setSqlCreateDate(DateUtil.now());
                dbSqlText.setSqlText(Base64Decoder.decodeStr(MapUtil.getStr(map, "content")));
                dbSqlText.setTitle(Base64Decoder.decodeStr(MapUtil.getStr(map, "title")));
                List<TeamSourceModel> teamSourceModels = teamSourceService.queryTeamByIds(Arrays.asList(MapUtil.getLong(map, "taemId")));
                //导入的历史团队id不存在，将赋值给当前选中的团队。
                if (!teamSourceModels.isEmpty()) {
                    dbSqlText.setTeamId(MapUtil.getLong(map, "taemId"));
                }
                dbSourceService.saveSqlText(dbSqlText);
            }
            return AjaxResult.success("共导入" + dataSourceJson.size() + "条数据!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败!" + e.getMessage());
        }
    }

    /**
     * 上传团队信息
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadTeamJson")
    @ResponseBody
    public AjaxResult uploadTeamJson(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return AjaxResult.error("没有解析出文件!");
        }
        try {
            JSONArray dataSourceJson = JSONUtil.parseArray(new String(file.getBytes(), StandardCharsets.UTF_8));
            if (dataSourceJson.isEmpty()) {
                return AjaxResult.error("没有解析出json数据，请检查！");
            }
            for (Object object : dataSourceJson) {
                Map<String, Object> map = (Map<String, Object>) object;
                int compare = VersionComparator.INSTANCE.compare(MapUtil.getStr(map, "version"), "v3.7");
                if (compare < 0) {
                    throw new RuntimeException("您的版本小于V3.7不兼容,无法导入团队信息！");
                }
                TeamSourceModel teamSourceModel = new TeamSourceModel();
                teamSourceModel.setTeamName(Base64Decoder.decodeStr(MapUtil.getStr(map, "teamName")));
                teamSourceModel.setUserId(MapUtil.getLong(map, "userId"));
                teamSourceModel.setDescription(Base64Decoder.decodeStr(MapUtil.getStr(map, "description")));
                teamSourceModel.setState(MapUtil.getInt(map, "state"));
                teamSourceModel.setId(MapUtil.getLong(map, "id"));
                AjaxResult ajaxResult = teamSourceService.addTeamSource(teamSourceModel);
                if (!ajaxResult.getCode().equals(200)) {
                    return ajaxResult;
                }
            }
            return AjaxResult.success("共导入" + dataSourceJson.size() + "条数据!");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error("导入失败!" + e.getMessage());
        }
    }


    /**
     * 删除所有数据源
     *
     * @return
     */
    @PostMapping("/deleteDataSourceAll")
    @ResponseBody
    public AjaxResult deleteDataSourceAll() {
        dbSourceService.deleteDataSourceAll();
        return AjaxResult.success();
    }

    /***
     * 加载导出日志列表
     * @param model
     * @return
     */
    @RequestMapping("/exportFilesLogList")
    @ResponseBody
    public AjaxResult exportFilesLogList(SysExportModel model) {
        return AjaxResult.success(dbSourceService.exportFilesLogList(model));
    }

    /***
     * 查询报警配置
     * @param model
     * @return
     */
    @RequestMapping("/messageTemplateList")
    @ResponseBody
    public AjaxResult messageTemplateList(SysMessageTemplateModel model) {
        return AjaxResult.success(messageTemplateService.list(model));
    }

    /**
     * 新增报警配置
     * @param sysMessageTemplateModel
     * @return
     */
    @RequestMapping("/addMessageTemplate")
    @ResponseBody
    public AjaxResult addMessageTemplate(@RequestBody SysMessageTemplateModel sysMessageTemplateModel) {
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getName())) {
            return AjaxResult.error("名称不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getUrl())) {
            return AjaxResult.error("URL不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getContent())) {
            return AjaxResult.error("消息内容不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getType())) {
            return AjaxResult.error("类型不能为空!");
        }
        return messageTemplateService.addMessageTemplate(sysMessageTemplateModel);
    }

    /**
     * 删除告警配置
     * @param id
     * @return
     */
    @RequestMapping("/deleteMessageTemplate")
    @ResponseBody
    public AjaxResult deleteMessageTemplate(@RequestParam Long id) {
        if (ObjectUtil.isEmpty(id)) {
            return AjaxResult.error("参数不能为空!");
        }
        return messageTemplateService.deleteMessageTemplate(id);
    }

    /**
     * 测试发送
     * @param sysMessageTemplateModel
     * @return
     */
    @RequestMapping("/testMessageTemplate")
    @ResponseBody
    public AjaxResult testMessageTemplate(@RequestBody SysMessageTemplateModel sysMessageTemplateModel) {
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getName())) {
            return AjaxResult.error("名称不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getUrl())) {
            return AjaxResult.error("URL不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getContent())) {
            return AjaxResult.error("消息内容不能为空!");
        }
        if (ObjectUtil.isEmpty(sysMessageTemplateModel.getType())) {
            return AjaxResult.error("类型不能为空!");
        }
        return messageTemplateService.testMessageTemplate(sysMessageTemplateModel);
    }

    /***
     * 加载告警模板数据源
     * @return
     */
    @RequestMapping("/findMessageTemplateList")
    @ResponseBody
    public Map findMessageTemplateList() {
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", messageTemplateService.findMessageTemplateList());
        return result;
    }

}
