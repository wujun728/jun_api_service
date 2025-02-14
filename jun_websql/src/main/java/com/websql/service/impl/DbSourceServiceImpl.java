package com.websql.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.text.StrFormatter;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpStatus;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.websql.config.DataSourceFactory;
import com.websql.config.JdbcUtils;
import com.websql.config.SqlParserHandler;
import com.websql.dao.*;
import com.websql.model.*;
import com.websql.service.DbSourceService;
import com.websql.service.TeamSourceService;
import com.websql.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.io.File;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @ClassName DbSourceServiceImpl
 * @Description 业务处理
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 17:34
 **/
@Service
public class DbSourceServiceImpl implements DbSourceService {

    private static final Logger log = LoggerFactory.getLogger(DbSourceServiceImpl.class);

    @Resource
    private DbSourceRepository dbSourceRepository;

    @Resource
    private DbSqlTextRepository dbSqlTextRepository;

    @Resource
    private SysSetUpRepository sysSetUpRepository;

    @Resource
    private SysLogRepository sysLogRepository;

    @Resource
    private SysUserLogRepository sysUserLogRepository;

    @Resource
    private TeamSourceService teamSourceService;

    @Resource
    private SysExportLogRepository sysExportLogRepository;


    @Override
    public List<DataSourceModel> reloadDataSourceList() {
        return dbSourceRepository.reloadDataSourceList();
    }

    @Override
    public Result<DataSourceModel> selectDbSourceList(DataSourceModel result) {
        Result<DataSourceModel> result1 = new Result<DataSourceModel>();
        PageRequest pageRequest = PageRequest.of(result.getPage() - 1, result.getLimit());
        List<TeamResourceModel> resourceModels = teamSourceService.queryTeamResourceByTeamId(Collections.singletonList(Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId()), "DATASOURCE");
        Specification<DataSourceModel> spec = (root, query, cb) -> {
            Path<String> dbName = root.get("dbName");
            Path<String> dbAccount = root.get("dbAccount");
            Path<String> dbUrl = root.get("dbUrl");
            String dbUrl1 = result.getDbUrl() == null ? "" : result.getDbUrl();
            String account1 = result.getDbAccount() == null ? "" : result.getDbAccount();
            String dbname2 = result.getDbName() == null ? "" : result.getDbName();
            Predicate p1 = cb.like(dbName, "%" + dbname2 + "%");
            Predicate p2 = cb.like(dbAccount, "%" + account1 + "%");
            Predicate p3 = cb.like(dbUrl, "%" + dbUrl1 + "%");
            Predicate p = cb.and(p1, p2, p3, root.get("id").in(resourceModels.stream().map(TeamResourceModel::getResourceId).collect(Collectors.toList())));
            query.orderBy(cb.desc(root.get("id")));
            return p;
        };
        Page<DataSourceModel> all = dbSourceRepository.findAll(spec, pageRequest);
        result1.setList(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public Result<DbSqlText> getDbSqlText(DbSqlText model) {
        Result<DbSqlText> result1 = new Result<DbSqlText>();
        PageRequest request = PageRequest.of(model.getPage() - 1, model.getLimit());
        Long teamId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        Specification<DbSqlText> spec = (root, query, cb) -> {
            Path<String> title = root.get("title");
            Path<String> sqlText = root.get("sqlText");
            String sqlText1 = model.getSqlText() == null ? "" : model.getSqlText();
            String title1 = model.getTitle() == null ? "" : model.getTitle();
            Predicate p2 = cb.like(sqlText, "%" + sqlText1 + "%");
            Predicate p3 = cb.like(title, "%" + title1 + "%");
            Predicate p = cb.and(p2, p3, root.get("teamId").in(teamId));
            query.orderBy(cb.desc(root.get("id")));
            return p;
        };
        Page<DbSqlText> all = dbSqlTextRepository.findAll(spec, request);
        result1.setList(all.getContent());
        result1.setCount((int) all.getTotalElements());
        return result1;
    }

    @Override
    public void deleteDataBaseSource(Long id) {
        CacheUtils.remove("data_source_model");
        DataSourceModel dataSourceModel = dbSourceRepository.selectById(id);
        dbSourceRepository.deleteById(id);
        DataSourceFactory.removeDataSource(dataSourceModel.getDbName());
        teamSourceService.deleteResourceByResIds(Collections.singletonList(Long.valueOf(id)), "DATASOURCE");
    }

    @Override
    public SysSetup initSysSetup() {
        List<SysSetup> sysList = sysSetUpRepository.findAll();
        if (sysList.size() > 0) {
            return sysList.get(0);
        } else {
            return SysSetup.getInstance();
        }
    }

    @Override
    public void deleteSqlText(String id) {
        dbSqlTextRepository.delsqlText(Integer.valueOf(id));
    }

    @Override
    public Result<SysLog> getLogList(SysLog model) {
        Result<SysLog> result = new Result<SysLog>();
        Specification<SysLog> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(3);
            if (ObjectUtil.isNotEmpty(model.getLogContent())) {
                predicates.add(cb.like(root.get("name"), "%" + model.getLogContent() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getLogContent())) {
                predicates.add(cb.like(root.get("logName"), "%" + model.getLogName() + "%"));
            }
            if (ObjectUtil.isNotEmpty(model.getLogDbSource())) {
                predicates.add(cb.like(root.get("logDbSource"), "%" + model.getLogDbSource() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysLog> all = sysLogRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public void delSysLog() {
        sysLogRepository.deleteAll();
    }

    @Override
    public void delUserLog() {
        sysUserLogRepository.deleteAll();
    }

    @Override
    public AjaxResult executeSqlNew(ExecuteSql sql) {
        SysLog log = new SysLog("sql执行记录", "1", sql.getDataBaseName(), Base64Encoder.encode(sql.getSqlText()), StpUtils.getUserExtName(), DateUtil.now());
        try {
            List<SqlParserVo> parserVoList = SqlParserHandler.getParserVo(sql.getDataBaseName(), sql.getSqlText());
            if (parserVoList.isEmpty()) {
                return AjaxResult.error("解析SQL失败,返回为空请重试!");
            }
            if (sql.isExport()) {
                long count = parserVoList.stream().map(SqlParserVo::getMethodType).filter(s -> !s.equals("SELECT")).count();
                if (count > 0) {
                    return AjaxResult.error("导出数据时,不支持非查询语句执行!");
                }
            }
            List<SqlExecuteResultVo> resultVos = new ArrayList<>(parserVoList.size());
            for (SqlParserVo sqlParserVo : parserVoList) {
                TimeInterval timer = DateUtil.timer();
                SqlExecuteResultVo vo = new SqlExecuteResultVo();
                vo.setTableNameList(JSON.toJSONString(sqlParserVo.getTableNameList()));
                vo.setExecuteType(sqlParserVo.getMethodType());
                vo.setParserSql(sqlParserVo.getSqlContent());
                vo.setDataBaseKey(sql.getDataBaseName());
                if (SqlParserHandler.SELECT.equals(sqlParserVo.getMethodType())) {
                    Map<String, Object> moreResult = JdbcUtils.findMoreResult(sql.getDataBaseName(), sqlParserVo.getSqlContent(), new ArrayList<>());
                    vo.setData(moreResult.get("data"));
                    vo.setStatus(Integer.parseInt(moreResult.get("code").toString()));
                    vo.setType(0);
                    vo.setErrorMessage(moreResult.get("msg").toString());
                } else {
                    Map<String, Object> moreResult = JdbcUtils.updateByPreparedStatement(sql.getDataBaseName(), sqlParserVo.getSqlContent(), new ArrayList<>());
                    vo.setType(1);
                    vo.setData(moreResult.get("data"));
                    vo.setStatus(Integer.parseInt(moreResult.get("code").toString()));
                    vo.setErrorMessage(moreResult.get("msg").toString());
                }
                vo.setTime(timer.intervalRestart());
                resultVos.add(vo);
            }
            log.setLogResult(JSON.toJSONString(resultVos));
            return AjaxResult.success(resultVos);
        } catch (Exception e) {
            e.printStackTrace();
            log.setLogResult(e.getMessage());
            return AjaxResult.error(e.getMessage());
        } finally {
            SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
            if (sysSetup.getEnabledSqlLog() == 1) {
                sysLogRepository.save(log);
            }
        }
    }


    @Override
    public Integer selectDbByName(String paramName) {
        return dbSourceRepository.findDataSourceByName(paramName);
    }

    @Override
    public void addDbSource(DataSourceModel model, Long teamId) {
        if (selectDbByName(model.getDbName()) > 0) {
            throw new RuntimeException("数据源名称已经存在,请换一个!");
        }
        try {
            DataSourceFactory.saveDataSource(model);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        CacheUtils.remove("data_source_model");
        if (ObjectUtil.isNotEmpty(model.getDbPassword())) {
            String encrypt = PasswordUtil.encrypt(model.getDbPassword());
            model.setDbPassword(encrypt);
        }
        if (ObjectUtil.isNotEmpty(model.getDbAccount())) {
            String encrypt = PasswordUtil.encrypt(model.getDbAccount());
            model.setDbAccount(encrypt);
        }
        DataSourceModel save = dbSourceRepository.save(model);
        //兼容系统初始化时获取不到teamId问题
        Long tid = teamId == null ? StpUtils.getCurrentActiveTeam().getId() : teamId;
        teamSourceService.updateTeamResources(Collections.singletonList(tid.toString()), Collections.singletonList(save.getId()), "DATASOURCE");

    }

    @Override
    public List<Map<String, String>> dbsourceSqlList(DataSourceModel model) {
        List<Map<String, String>> dataSourceList = CacheUtils.get("data_source_model", List.class);
        if (ObjectUtil.isNull(dataSourceList)) {
            dataSourceList = new ArrayList<>(0);
            List<DataSourceModel> list = dbSourceRepository.findAll().stream().filter(s -> s.getDbState().equals("有效")).sorted(Comparator.comparing(DataSourceModel::getId).reversed()).collect(Collectors.toList());
            for (DataSourceModel dataSourceModel : list) {
                Map<String, String> item = new HashMap<>(3);
                item.put("code", dataSourceModel.getDbName());
                item.put("value", dataSourceModel.getDbName());
                item.put("id", dataSourceModel.getId().toString());
                item.put("select", "false");
                dataSourceList.add(item);
            }
            CacheUtils.put("data_source_model", dataSourceList,30000);
        }
        //按照团队过滤
        Long teamId = StpUtils.getCurrentActiveTeam().getId();
        List<String> resourceIds = teamSourceService.queryTeamResourceByTeamId(Collections.singletonList(teamId), "DATASOURCE").stream().map(s -> s.getResourceId().toString()).collect(Collectors.toList());
        dataSourceList = dataSourceList.stream().filter(s -> resourceIds.contains(s.get("id"))).collect(Collectors.toList());
        //按最近使用的数据源推荐并选中此数据源
        String source = sysLogRepository.querySysLogDataSource();
        dataSourceList.stream().filter(s -> s.get("code").equals(source)).forEach(s -> s.put("short", "1"));
        dataSourceList.stream().filter(s -> !s.get("code").equals(source)).forEach(s -> s.put("short", "2"));
        dataSourceList.sort(Comparator.comparingInt((Map o) -> Integer.valueOf(o.get("short").toString())));
        if (!dataSourceList.isEmpty()) {
            dataSourceList.stream().skip(1).forEach(s -> s.put("select", "false"));
            dataSourceList.get(0).put("select", "true");
        }
        return dataSourceList;
    }

    @Override
    public void saveSqlText(DbSqlText model) {
        model.setSqlCreateUser(StpUtils.getCurrentUserName());
        if (ObjectUtil.isNull(model.getTeamId())) {
            model.setTeamId(Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId());
        }
        dbSqlTextRepository.save(model);
    }

    @Override
    public void sqlTextDeleteAll() {
        dbSqlTextRepository.deleteAll();
    }

    @Override
    public List<Map<String, String>> sqlTextList(DataSourceModel model) {
        List<Map<String, String>> sqlTextModelList = new ArrayList<>();
        Long itemId = Objects.requireNonNull(StpUtils.getCurrentActiveTeam()).getId();
        List<DbSqlText> all = dbSqlTextRepository.findAll().stream().filter(s -> s.getTeamId().equals(itemId)).sorted(Comparator.comparing(DbSqlText::getId).reversed()).collect(Collectors.toList());
        for (DbSqlText dbSqlText : all) {
            Map<String, String> item = new HashMap<>(2);
            item.put("code", dbSqlText.getSqlText());
            item.put("value", dbSqlText.getTitle());
            sqlTextModelList.add(item);
        }
        return sqlTextModelList;
    }

    @Override
    public AjaxResult findTableField(String database) {
        SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
        if (ObjectUtil.isNull(sysSetup) || ObjectUtil.notEqual(sysSetup.getEnabledHint(), 0)) {
            return AjaxResult.success("系统关闭提示功能，可以再参数中设置开启。");
        }
        String key = "table_field_list" + database + StpUtils.getCurrentActiveTeam().getId();
        Map<String, List<String>> resultMap = CacheUtils.get(key, Map.class);
        if (ObjectUtil.isNull(resultMap)) {
            String viewSql = TableFieldSqlUtils.getViewSql(database);
            if (ObjectUtil.isEmpty(viewSql)) {
                return AjaxResult.error("不兼容的数据库类型!");
            }
            Map<String, Object> map = JdbcUtils.findMoreResult(database, viewSql, new ArrayList<>());
            if (ObjectUtil.notEqual(map.get("code"), "1")) {
                return AjaxResult.error("查询此数据库表名时失败!");
            }
            List<Map> list = (List<Map>) map.get("data");
            resultMap = list.stream().collect(Collectors.groupingBy(s -> s.get("TABLE_NAME").toString(), Collectors.mapping(s -> s.get("TABLE_FIELD").toString(), Collectors.toList())));
            CacheUtils.put(key, resultMap,30000);
        }
        return AjaxResult.success(resultMap);
    }

    @Override
    public void updateDataSourceName(Long id, String name) throws SQLException {
        DataSourceModel dataSourceModel = dbSourceRepository.selectById(id);
        dataSourceModel.setDbName(name);
        dbSourceRepository.save(dataSourceModel);
        CacheUtils.remove("data_source_model");
        if (ObjectUtil.isNotEmpty(dataSourceModel.getDbPassword()) && ObjectUtil.isNotEmpty(dataSourceModel.getDbAccount())) {
            String account = PasswordUtil.decrypt(dataSourceModel.getDbAccount());
            dataSourceModel.setDbAccount(account);
            String password = PasswordUtil.decrypt(dataSourceModel.getDbPassword());
            dataSourceModel.setDbPassword(password);
            DataSourceFactory.initDataSource(Collections.singletonList(dataSourceModel));
        }
    }

    @Override
    public AjaxResult findMetaTable(String database, String table) {
        String key = "data_source_meta_" + database + "_" + table;
        DataSourceMeta dataSourceMeta = CacheUtils.get(key, DataSourceMeta.class);
        if (ObjectUtil.isNotNull(dataSourceMeta)) {
            return AjaxResult.success(dataSourceMeta);
        }
        try {
            Future<DataSourceMeta> tableMetaFuture = ThreadUtil.execAsync(() -> JdbcUtils.getTableMeta(database, table));
            Future<List<DataSourceTableMeta>> columnsMetaFuture = ThreadUtil.execAsync(() -> JdbcUtils.getColumnsMeta(database, table));
            Future<List<DataSourceIndexMeta>> indexInfoMetaFuture = ThreadUtil.execAsync(() -> JdbcUtils.getIndexInfoMeta(database, table));
            Future<List<DataSourceTableMeta>> keyMetaFuture = ThreadUtil.execAsync(() -> JdbcUtils.getKeyMeta(database, table));
            DataSourceMeta tableMeta = tableMetaFuture.get();
            if (ObjectUtil.isNull(tableMeta)) {
                columnsMetaFuture.cancel(true);
                indexInfoMetaFuture.cancel(true);
                keyMetaFuture.cancel(true);
                return AjaxResult.error("数据库中没有[" + table + "]信息!");
            }
            dataSourceMeta = JdbcUtils.getDataSourceMeta(database, table);
            dataSourceMeta.setDatabaseName(tableMeta.getDatabaseName());
            dataSourceMeta.setTableType(tableMeta.getTableType());
            dataSourceMeta.setTableComment(tableMeta.getTableComment());
            dataSourceMeta.setTableSchema(tableMeta.getTableSchema());
            List<DataSourceTableMeta> tableMetas = columnsMetaFuture.get();
            dataSourceMeta.setTablesMetaList(tableMetas);
            List<DataSourceTableMeta> columnsMeta = keyMetaFuture.get();
            dataSourceMeta.setTablesKeysMetaList(columnsMeta);
            List<DataSourceIndexMeta> dataSourceIndexMetas = indexInfoMetaFuture.get();
            dataSourceMeta.setTablesIndexMetaList(dataSourceIndexMetas);
            CacheUtils.put(key, dataSourceMeta);
            return AjaxResult.success(dataSourceMeta);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.success(dataSourceMeta);
        }
    }


    @Override
    public AjaxResult showTableSql(String databaseKey, String table) {

        String sql = "show create table " + table;
        Map<String, Object> dataResult = JdbcUtils.findOneResult(databaseKey, sql, null);
        Map<String, String> resultMap = new HashMap<>(2);

        //create 模板
        if (ObjectUtil.notEqual("1", dataResult.get("code"))) {
            return AjaxResult.error(dataResult.get("msg").toString());
        }
        if (ObjectUtil.isNotNull(dataResult.get("Create Table"))) {
            resultMap.put("createTableSql", dataResult.get("Create Table").toString());
        }
        if (ObjectUtil.isNotNull(dataResult.get("statement"))) {
            resultMap.put("createTableSql", dataResult.get("statement").toString());
        }

        //insert 模板
        String inertSql = TableFieldSqlUtils.getInertSql(databaseKey);
        if (ObjectUtil.isNotNull(inertSql)) {
            String dataBaseName = DataSourceFactory.getDataBaseName(databaseKey);
            String insertSql = StrFormatter.format(inertSql, dataBaseName, table);
            Map<String, Object> dataResult2 = JdbcUtils.findOneResult(databaseKey, insertSql, null);
            if (ObjectUtil.equal("1", dataResult2.get("code"))) {
                resultMap.put("insertSql", dataResult2.get("insertText").toString());
            }
        }
        resultMap.put("updateSql", "todo");
        resultMap.put("deleteSql", "todo");
        return AjaxResult.success(resultMap);
    }


    @Override
    public DataSourceModel selectDbById(Long id) {
        return dbSourceRepository.selectById(id);
    }

    @Override
    public List<DbSqlText> sqlTextListAll() {
        return dbSqlTextRepository.findAll();
    }

    @Override
    public void deleteDataSourceAll() {
        List<DataSourceModel> all = dbSourceRepository.findAll();
        for (DataSourceModel dataSourceModel : all) {
            try {
                deleteDataBaseSource(dataSourceModel.getId());
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    public Map<String, Object> createAsyncExport(ExecuteSql executeSql) {
        SysExportModel sysExportModel = new SysExportModel();
        BeanUtil.copyProperties(executeSql, sysExportModel);
        sysExportModel.setUserId(StpUtils.getUserExtName());
        sysExportModel.setBeginDate(DateUtil.date());
        sysExportModel.setState("导出中");
        sysExportLogRepository.save(sysExportModel);
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Boolean async = EnvBeanUtil.getBoolean("export.config.async");
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("async", async);
        resultMap.put("id", sysExportModel.getId());
        resultMap.put("date", sysExportModel.getBeginDate());
        if (async) {
            ThreadUtil.execAsync(() -> {
                RequestContextHolder.setRequestAttributes(requestAttributes);
                String excel = createExcel(executeSql, sysExportModel, async);
                RequestContextHolder.resetRequestAttributes();
                return excel;
            });
            return resultMap;
        }
        String path = createExcel(executeSql, sysExportModel, async);
        resultMap.put("path", path);
        return resultMap;
    }

    @Override
    public SysExportModel exportAsyncData(Long id) {
        return sysExportLogRepository.selectById(id);
    }

    /**
     * 创建多sheet文件并存放本地目录
     *
     * @param executeSql
     * @param sysExportModel
     * @return
     */
    private String createExcel(ExecuteSql executeSql, SysExportModel sysExportModel, Boolean async) {
        try {
            executeSql.setExport(true);
            AjaxResult ajaxResult = executeSqlNew(executeSql);
            Integer code = ajaxResult.getCode();
            if (!ObjectUtil.equal(HttpStatus.HTTP_OK, code)) {
                throw new RuntimeException(ajaxResult.getMsg());
            }
            List<SqlExecuteResultVo> dataList = (List<SqlExecuteResultVo>) ajaxResult.getData();
            List<Map<String, Object>> exportTaskSheet = new ArrayList<>();
            for (SqlExecuteResultVo sqlExecuteResultVo : dataList) {
                if (sqlExecuteResultVo.getStatus() == 1 && sqlExecuteResultVo.getExecuteType().equals("SELECT")) {
                    JSONArray itemArray = (JSONArray) sqlExecuteResultVo.getData();
                    Set<String> headSet = itemArray.getJSONObject(0).keySet();
                    List<List<String>> headList = headSet.stream()
                            .map(Collections::singletonList)
                            .collect(Collectors.toList());
                    List<List<Object>> sheetDataList = itemArray.stream()
                            .map(item -> {
                                List<Object> col = new ArrayList<>();
                                ((JSONObject) item).forEach((k, v) -> col.add(v));
                                return col;
                            })
                            .collect(Collectors.toList());
                    Map<String, Object> task = new HashMap<>();
                    task.put("headList", headList);
                    task.put("sheetDataList", sheetDataList);
                    exportTaskSheet.add(task);
                }
            }
            String filePath = EnvBeanUtil.getString("export.config.path");
            if (ObjectUtil.isEmpty(filePath)) {
                throw new RuntimeException("未配置文件存放路径，请检查export.config.path配置");
            }
            filePath = FileUtil.mkdir(System.getProperty("user.dir") + File.separator + filePath).getAbsolutePath() + File.separator;
            String fileName = filePath + "exportData" + System.currentTimeMillis() + ".xlsx";
            try (ExcelWriter excelWriter = EasyExcel.write(fileName).registerConverter(new ExcelLocalDateStringConverter()).build()) {
                for (int i = 0; i < exportTaskSheet.size(); i++) {
                    Map<String, Object> sheetDataMap = exportTaskSheet.get(i);
                    WriteSheet writeSheet = EasyExcel.writerSheet(i, "结果集" + (i + 1)).build();
                    writeSheet.setHead((List<List<String>>) sheetDataMap.get("headList"));
                    excelWriter.write((List<List<String>>) sheetDataMap.get("sheetDataList"), writeSheet);
                }
            }
            sysExportModel.setFiles(fileName);
            sysExportModel.setMessage("共" + exportTaskSheet.size() + "个sheet页");
            sysExportModel.setState("完成");
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            sysExportModel.setMessage(e.getMessage());
            sysExportModel.setState("失败");
            sysExportModel.setFiles(null);
        } finally {
            sysExportModel.setEndDate(DateUtil.date());
            sysExportLogRepository.save(sysExportModel);
        }
        if (!async) {
            throw new RuntimeException(sysExportModel.getMessage());
        }
        return "error";
    }

    @Override
    public Result<SysExportModel> exportFilesLogList(SysExportModel model) {
        Result<SysExportModel> result = new Result<>();
        Specification<SysExportModel> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>(3);
            if (ObjectUtil.isNotEmpty(model.getState())) {
                predicates.add(cb.like(root.get("state"), "%" + model.getState() + "%"));
            }
            query.orderBy(cb.desc(root.get("id")));
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        };
        Page<SysExportModel> all = sysExportLogRepository.findAll(spec, PageRequest.of(model.getPage() - 1, model.getLimit()));
        result.setList(all.getContent());
        result.setCount((int) all.getTotalElements());
        return result;
    }

    @Override
    public List<MetaTreeTable> metaTreeTableList() {
        List<MetaTreeTable> resultList = new ArrayList<>();
        List<Map<String, String>> databaseList = this.dbsourceSqlList(null);
        if (databaseList == null || databaseList.isEmpty()) {
            return resultList;
        }
        for (Map<String, String> item : databaseList) {
            MetaTreeTable metaTreeTable = createDataSourceNode(item);
            resultList.add(metaTreeTable);
        }
        for (MetaTreeTable database : resultList) {
            AjaxResult table = findTableField(database.getTitle());
            Map<String, Object> tableMap = (Map<String, Object>) table.getData();
            List<MetaTreeTable> tableList = new ArrayList<>();
            int tableCount = tableMap.size();
            tableMap.forEach((k, v) -> {
                AjaxResult tableField = this.findMetaTable(database.getTitle(), k);
                DataSourceMeta tableFieldData = (DataSourceMeta) tableField.getData();
                if (ObjectUtil.isNotNull(tableFieldData)) {
                    DataSourceMeta item = new DataSourceMeta();
                    BeanUtil.copyProperties(tableFieldData, item);
                    tableList.add(createTableNode(item, tableCount));
                }
            });
            if (ObjectUtil.isNotNull(tableList) && !tableList.isEmpty()) {
                MetaTreeTable metaTreeTable = tableList.get(0);
                DataSourceMeta tableMeta = metaTreeTable.getTableMeta();
                DataSourceMeta item = new DataSourceMeta();
                BeanUtil.copyProperties(tableMeta, item);
                item.setTablesKeysMetaList(null);
                item.setTablesIndexMetaList(null);
                item.setTablesMetaList(null);
                item.setTableComment(null);
                item.setTableName(null);
                database.setTableCount(metaTreeTable.getTableCount());
                database.setTableMeta(item);
            }
            database.setChildren(tableList);
        }
        return resultList;
    }

    private MetaTreeTable createDataSourceNode(Map<String, String> item) {
        MetaTreeTable metaTreeTable = new MetaTreeTable();
        metaTreeTable.setId(item.get("id"));
        metaTreeTable.setTitle(item.get("value"));
        metaTreeTable.setField("database");
        return metaTreeTable;
    }

    private MetaTreeTable createTableNode(DataSourceMeta meta, int tableCount) {
        List<MetaTreeTable> fieldList = new ArrayList<>();
        for (DataSourceTableMeta dataSourceTableMeta : meta.getTablesMetaList()) {
            MetaTreeTable item = createFieldNode(dataSourceTableMeta);
            fieldList.add(item);
        }
        MetaTreeTable table = new MetaTreeTable();
        table.setField("table");
        table.setTableCount(tableCount);
        table.setFieldCount(fieldList.size());
        table.setId(meta.getTableName());
        String title = Objects.equals(meta.getTableComment(), "") || meta.getTableComment() == null ? meta.getTableName() : String.join(":", meta.getTableName(), meta.getTableComment());
        table.setTitle(title);
        table.setTableMeta(meta);
        table.setChildren(fieldList);
        return table;
    }

    private MetaTreeTable createFieldNode(DataSourceTableMeta fieldMeta) {
        MetaTreeTable field = new MetaTreeTable();
        field.setField("field");
        String title = Objects.equals(fieldMeta.getComment(), "") || fieldMeta.getComment() == null ? fieldMeta.getColumnName() : String.join(":", fieldMeta.getColumnName(), fieldMeta.getComment());
        field.setTitle(title);
        field.setId(fieldMeta.getColumnName());
        field.setDisabled(true);
        return field;
    }
}
