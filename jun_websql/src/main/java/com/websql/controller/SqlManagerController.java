package com.websql.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.websql.model.*;
import com.websql.service.DbSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : SqlManagerController
 * @Description : SQL管理控制器
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 23:53
 */
@Controller
@RequestMapping("/sqlManager")
@Slf4j
public class SqlManagerController {


    @Resource
    private DbSourceService dbSourceService;

    @RequestMapping("/sqlPage")
    public String sqlPage() {
        return "sqlPageNew";
    }

    @RequestMapping("/sqlTextPage")
    public String sqlTextPage() {
        return "sqlTextPage";
    }


    @RequestMapping("/metaTablePage")
    public String tablePage() {
        return "metaTablePage";
    }

    /**
     * 查询sql文本下拉框
     *
     * @param model
     * @return
     */
    @RequestMapping("/querySqlTextSelect")
    @ResponseBody
    public Map sqlTextList(DataSourceModel model) {
        List<Map<String, String>> resultData = dbSourceService.sqlTextList(model);
        Map result = new HashMap(2);
        result.put("code", 0);
        result.put("data", resultData);
        return result;
    }

    /**
     * sql文本列表
     *
     * @param model
     * @return
     */
    @RequestMapping("/querySqlTextList")
    @ResponseBody
    public AjaxResult getSqlTextList(DbSqlText model) {
        return AjaxResult.success(dbSourceService.getDbSqlText(model));
    }


    /**
     * 保存sql文本
     *
     * @param model
     * @return
     */
    @RequestMapping("/saveSqlText")
    @ResponseBody
    public AjaxResult saveSqlText(@RequestBody DbSqlText model) {
        try {
            dbSourceService.saveSqlText(model);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
        return AjaxResult.success();
    }


    /**
     * 删除sql
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteSqlText/{id}")
    @ResponseBody
    public AjaxResult deleteSqlText(@PathVariable String id) {
        try {
            dbSourceService.deleteSqlText(id);
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }

    @RequestMapping("/sqlTextDeleteAll")
    @ResponseBody
    public AjaxResult sqlTextDeleteAll() {
        try {
            dbSourceService.sqlTextDeleteAll();
            return AjaxResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(e.getMessage());
        }
    }


    /**
     * sql执行
     *
     * @param sql
     * @return
     */
    @RequestMapping("/executeSqlNew")
    @ResponseBody
    public AjaxResult executeSqlNew(@RequestBody ExecuteSql sql) {
        if (ObjectUtil.isEmpty(sql.getDataBaseName()) || ObjectUtil.isEmpty(sql.getSqlText())) {
            return AjaxResult.error("请选择数据源或编写SQL!");
        }
        sql.setExport(false);
        return dbSourceService.executeSqlNew(sql);
    }

    /***
     * 查询数据库所有表及字段
     * @param database 数据源名称
     * @return
     */
    @RequestMapping("/findTableField")
    @ResponseBody
    public AjaxResult findTableField(@RequestParam String database) {
        return dbSourceService.findTableField(database);
    }


    /***
     * 查询数据库表元数据
     * @param database 数据源名称
     * @param table 表名
     * @return
     */
    @RequestMapping("/findMetaTable")
    @ResponseBody
    public AjaxResult findMetaTable(@RequestParam String database, @RequestParam String table) {
        if (ObjectUtil.isEmpty(database) || ObjectUtil.isEmpty(table)) {
            return AjaxResult.error("必填参数为空!");
        }
        return dbSourceService.findMetaTable(database, table);
    }


    /**
     * 建表语句
     *
     * @param database 数据源名称
     * @param table    表名
     * @return
     */
    @RequestMapping("/showTableSql")
    @ResponseBody
    public AjaxResult showTableSql(@RequestParam String database, @RequestParam String table) {
        if (ObjectUtil.isEmpty(database) || ObjectUtil.isEmpty(table)) {
            return AjaxResult.error("必填参数为空!");
        }
        return dbSourceService.showTableSql(database, table);
    }

    /**
     * 创建异步导出excel文件
     *
     * @param sql 查询对象
     * @return
     */
    @RequestMapping("/createAsyncExport")
    @ResponseBody
    public AjaxResult createAsyncExport(@RequestBody ExecuteSql sql, HttpServletResponse response) {
        if (ObjectUtil.isEmpty(sql.getDataBaseName()) || ObjectUtil.isEmpty(sql.getSqlText())) {
            return AjaxResult.error("请选择数据源或编写SQL!");
        }
        return AjaxResult.success(dbSourceService.createAsyncExport(sql));
    }

    /**
     * 下载excel文件到浏览器
     *
     * @param id 查询对象
     * @return
     */
    @GetMapping("/downloadExcelFile")
    public void downloadExcelFile(@RequestParam Long id, HttpServletResponse response) throws IOException {
        if (ObjectUtil.isEmpty(id)) {
            sendMessage("必填参数为空!", response);
            return;
        }
        AjaxResult result = queryExportData(id);
        if (ObjectUtil.notEqual(result.getCode(), 200)) {
            sendMessage(result.getMsg(), response);
            return;
        }
        SysExportModel model = (SysExportModel) result.getData();
        try (InputStream inputStream = Files.newInputStream(Paths.get(model.getFiles()))) {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.reset();
            response.setContentType("application/octet-stream");
            String filename = new File(model.getFiles()).getName();
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
        } catch (IOException e) {
            sendMessage("文件下载失败请重试:" + e.getMessage(), response);
        }
    }

    /**
     * 查询导出进度
     *
     * @param id 主键ID
     * @return
     */
    @PostMapping("/queryExportData")
    @ResponseBody
    private AjaxResult queryExportData(@RequestParam Long id) {
        if (ObjectUtil.isEmpty(id)) {
            return AjaxResult.error("必填参数为空!");
        }
        SysExportModel model = dbSourceService.exportAsyncData(id);
        if (ObjectUtil.isNull(model)) {
            return AjaxResult.error("没有找到要下载的文件，请重新生成!");
        }
        if (ObjectUtil.equal(model.getState(), "导出中")) {
            return AjaxResult.error("导出中");
        }
        if (ObjectUtil.equal(model.getState(), "失败")) {
            return AjaxResult.error("导出失败，请重试!" + model.getMessage());
        }
        if (ObjectUtil.isEmpty(model.getFiles())) {
            return AjaxResult.error("没有找到文件存放路径地址，请重新生成!");
        }
        if (!FileUtil.isFile(model.getFiles())) {
            return AjaxResult.error("磁盘中已不存在，可能过期自动删除或文件丢失;请重新生成!");
        }
        return AjaxResult.success(model);
    }

    /**
     * 提示json格式异常信息
     *
     * @param message 提示信息
     */
    private void sendMessage(String message, HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(JSON.toJSONString(AjaxResult.error(message)));
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 元数据树呈现
     *
     * @return
     */
    @RequestMapping("/metaTreeTableList")
    @ResponseBody
    public AjaxResult metaTreeTableList() {
        return AjaxResult.success(dbSourceService.metaTreeTableList());
    }


}

