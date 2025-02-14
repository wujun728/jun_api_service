package com.websql.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.druid.DbType;
import com.websql.config.DataSourceFactory;
import com.websql.config.JdbcUtils;
import com.websql.model.AjaxResult;
import com.websql.model.DataSourceModel;
import com.websql.model.Result;
import com.websql.service.DbSourceService;
import com.websql.service.LoginService;
import com.websql.service.TeamSourceService;
import com.websql.util.PasswordUtil;
import com.websql.util.StpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DataSourceController
 * @Description 操作数据源控制
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 17:03
 **/
@Controller
@RequestMapping("/dataSourceManager")
@Slf4j
public class DataSourceManagerController {

    @Resource
    private DbSourceService dbSourceService;

    @Resource
    private LoginService loginService;

    @Resource
    private TeamSourceService teamSourceService;


    @RequestMapping("/page")
    public String dataSourcePage() {
        return "dataSourcePage";
    }

    @RequestMapping("/addSourcePage")
    public String addSourcePage() {
        return "addSourcePage";
    }

    @RequestMapping("/dataSourceList")
    @ResponseBody
    public AjaxResult dataSourceList(DataSourceModel model) {
        Result<DataSourceModel> dataSourceModelResult = dbSourceService.selectDbSourceList(model);
        if (StpUtil.hasRole("demo-admin")) {
            dataSourceModelResult.getList().forEach(s -> s.setDbUrl("****************"));
        }
        return AjaxResult.success(dataSourceModelResult);
    }

    /***
     * 测试数据源
     * @param model
     * @return
     */
    @RequestMapping("/checkUrl")
    @ResponseBody
    public AjaxResult checkUrl(@RequestBody DataSourceModel model) {
        Connection conn = null;
        PreparedStatement pre = null;
        ResultSet rs = null;
        try {
            conn = DriverManager.getConnection(model.getDbUrl().trim(),
                    model.getDbAccount().trim(), model.getDbPassword().trim());
            DbType jdbcType = DataSourceFactory.getDbTypeByJdbcUrl(model.getDbUrl().trim(), model.getDriverClass());
            //odps特殊，提交实例运行
            if (jdbcType.equals(DbType.odps)) {
                if (ObjectUtil.isEmpty(conn.getMetaData().getDatabaseProductName())) {
                    return AjaxResult.error("连接失败,获取odps描述为空!");
                }
                return AjaxResult.success("连接成功!");
            }
            pre = conn.prepareStatement(model.getDbCheckUrl());
            rs = pre.executeQuery();
            if (rs.next()) {
                return AjaxResult.success("连接成功!");
            } else {
                return AjaxResult.error("连接失败,返回结果集为空!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return AjaxResult.error("连接失败,error:" + e.getMessage());
        } finally {
            JdbcUtils.releaseConn(rs, pre, conn);
        }
    }

    /***
     * 新增数据源
     * @param model
     * @return
     */
    @RequestMapping("/addDataSource")
    @ResponseBody
    public AjaxResult addDataSource(@RequestBody DataSourceModel model) {
        try {
            if (ObjectUtil.isEmpty(model.getDbName())) {
                return AjaxResult.error("连接名称不能为空！");
            }
            if (ObjectUtil.isEmpty(model.getDriverClass())) {
                return AjaxResult.error("驱动名称不能为空！");
            }
            if (ObjectUtil.isEmpty(model.getDbUrl())) {
                return AjaxResult.error("连接地址不能为空！");
            }
            log.info("Successful Add  DbSources ");
            dbSourceService.addDbSource(model, null);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /***
     * 删除数据源
     * @param id 主键ID
     * @return
     */
    @RequestMapping("/deleteDataSource/{id}")
    @ResponseBody
    public AjaxResult deleteDataSource(@PathVariable String id) {
        try {
            dbSourceService.deleteDataBaseSource(Long.valueOf(id));
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    /***
     * 加载数据源
     * @param model
     * @return
     */
    @RequestMapping("/findDataSourceList")
    @ResponseBody
    public Map findDataSourceList(DataSourceModel model) {
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", dbSourceService.dbsourceSqlList(model));
        return result;
    }

    @RequestMapping("/findDriverConfigListSelect")
    @ResponseBody
    public Map findDriverConfigListSelect(@RequestParam(required = false) String id) {
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", loginService.findDriverConfigListSelect(id));
        return result;
    }

    @RequestMapping("/updateDataSourceName")
    @ResponseBody
    public AjaxResult updateDataSourceName(@RequestParam Long id, @RequestParam String name) {
        try {
            if (ObjectUtil.isEmpty(name)) {
                return AjaxResult.error("连接名称不能为空！");
            }
            if (ObjectUtil.isEmpty(id)) {
                return AjaxResult.error("必填参数为空！");
            }
            Integer count = dbSourceService.selectDbByName(name);
            if (count > 0) {
                return AjaxResult.error("连接名称已经存在,请换一个！");
            }
            dbSourceService.updateDataSourceName(id, name);
            log.info("Successful update  DbSources ");
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/showDataSourcePassword")
    @ResponseBody
    public AjaxResult showDataSourcePassword(Long id) {
        if (!StpUtils.currentSuperAdmin()) {
            return AjaxResult.error("抱歉，您不是超级管理员无法查看!");
        }
        DataSourceModel dataSourceModelResult = dbSourceService.selectDbById(id);
        if (ObjectUtil.isNull(dataSourceModelResult)) {
            return AjaxResult.error("请重新刷新页面,没有找到此数据源配置信息!");
        }
        if (ObjectUtil.isNotEmpty(dataSourceModelResult.getDbPassword())) {
            String encrypt = PasswordUtil.decrypt(dataSourceModelResult.getDbPassword());
            dataSourceModelResult.setDbPassword(encrypt);
        }
        if (ObjectUtil.isNotEmpty(dataSourceModelResult.getDbAccount())) {
            String encrypt = PasswordUtil.decrypt(dataSourceModelResult.getDbAccount());
            dataSourceModelResult.setDbAccount(encrypt);
        }
        return AjaxResult.success(dataSourceModelResult);
    }

}
