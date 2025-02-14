package com.websql.task;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.task.Task;
import com.alibaba.fastjson.JSONObject;
import com.websql.config.JdbcUtils;
import com.websql.config.SqlParserHandler;
import com.websql.model.JobLogs;
import com.websql.model.SqlParserVo;
import com.websql.model.TimingVo;
import com.websql.service.TimingService;
import com.websql.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: JobExecuteFactory
 * @description: 定时任务执行类
 * @author: rabbit boy_0214@sina.com
 * @create: 2019-09-17 15:58
 **/
@Slf4j
public class JobExecuteFactory implements Task {


    private final Long id;

    private final String jobName;

    public JobExecuteFactory(Long id, String jobName) {
        this.id = id;
        this.jobName = jobName;
    }

    /**
     * 执行作业
     */
    @Override
    public void execute() {
        Long begin = System.currentTimeMillis();
        log.info("{}-作业任务开始.", jobName);
        TimingService timingService = SpringContextHolder.getBean(TimingService.class);
        TimingVo vo = timingService.queryTimingJobById(id);
        if (ObjectUtil.isNull(vo)) {
            ScheduleUtils.removeTask(id,"timing",false);
            return;
        }
        vo.setState("作业中");
        timingService.updateStatus(vo);
        StringBuffer param = new StringBuffer();
        StringBuffer values = new StringBuffer();
        JobLogs logs = new JobLogs();
        logs.setTeamId(vo.getTeamId());
        try {
            String sql = Base64Decoder.decodeStr(vo.getSqlText());
            if (ObjectUtil.isEmpty(sql)) {
                throw new NullPointerException("作业SQL为空");
            }
            //SQL预处理
            List<SqlParserVo> parserVoList = SqlParserHandler.getParserVo(vo.getTimingName(), sql);
            //sync
            if (!ObjectUtil.isEmpty(vo.getSyncName()) && !"请选择".equals(vo.getSyncName())) {
                for (SqlParserVo executeSql : parserVoList) {
                    Map<String, Object> resultData = JdbcUtils.findMoreResult(vo.getTimingName(), executeSql.getSqlContent(), new ArrayList<>());
                    if ("2".equals(resultData.get("code"))) {
                        throw new NullPointerException("作业SQL执行异常.error:" + resultData.get("msg"));
                    }
                    List itemList = (List) resultData.get("data");
                    //表中没有数据时，数据占位忽略。
                    if (itemList.size() == 1) {
                        JSONObject object = (JSONObject) itemList.get(0);
                        List<Object> webSqlPlaceholder = object.values().stream().filter(s -> s.equals("WEB_SQL_PLACEHOLDER")).collect(Collectors.toList());
                        if (object.keySet().size() == webSqlPlaceholder.size()) {
                            return;
                        }
                    }
                    if (!itemList.isEmpty()) {
                        JSONObject jo = (JSONObject) itemList.get(0);
                        Iterator<String> keys = jo.keySet().iterator();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            param.append("," + key);
                            values.append(",?");
                        }
                        param = param.deleteCharAt(0);
                        values = values.deleteCharAt(0);
                        String tableName = executeSql.getTableNameList().get(0);
                        if (!ObjectUtil.isEmpty(vo.getSyncTable())) {
                            tableName = vo.getSyncTable();
                        }
                        String itemSql = "insert into " + tableName + "(" + param + ") values (" + values + ")";
                        Map<String, Object> result = JdbcUtils.updateTimers(vo.getSyncName(), itemSql, itemList, param);
                        logs.setTaskError((String) result.get("msg"));
                        logs.setTaskState(result.get("code") == "1" ? "执行成功" : "执行失败");
                        logs.setTaskContent(Base64Encoder.encode(itemSql));
                        logs.setTaskValue(itemList.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logs.setTaskState("执行失败");
            logs.setTaskError(e.getMessage());
            log.error("定时作业异常.", e);
        } finally {
            Long end = System.currentTimeMillis();
            Long exDate = end - begin;
            log.info("{}-作业任务结束. {}ms.", jobName, exDate);
            logs.setTaskId(vo.getId());
            logs.setTaskName(vo.getTitle());
            logs.setCo1(String.valueOf(exDate));
            vo.setState("休眠");
            timingService.updateStatus(vo);
            timingService.saveLogs(logs);
        }
    }


}
