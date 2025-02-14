package com.websql.task;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.extra.expression.ExpressionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.websql.config.JdbcUtils;
import com.websql.config.SqlParserHandler;
import com.websql.dao.DetectionLogsRepository;
import com.websql.model.SqlParserVo;
import com.websql.model.SysDetectionLogsModel;
import com.websql.model.SysDetectionModel;
import com.websql.service.DetectionService;
import com.websql.service.MessageTemplateService;
import com.websql.util.JsonToLowerUtils;
import com.websql.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: DetectionJobFactory
 * @description: 定时检测任务执行类
 * @author: rabbit boy_0214@sina.com
 * @create: 2024-10-12 17:20
 **/
@Slf4j
public class DetectionJobFactory implements Task {


    private final Long id;

    private final String jobName;


    public DetectionJobFactory(Long id, String jobName) {
        this.id = id;
        this.jobName = jobName;
    }

    /**
     * 执行作业
     */
    @Override
    public void execute() {
        Long begin = System.currentTimeMillis();
        log.debug("{}-监测任务开始...", jobName);
        DetectionService detectionService = SpringContextHolder.getBean(DetectionService.class);
        SysDetectionModel vo = detectionService.selectById(id);
        if (ObjectUtil.isNull(vo)) {
            ScheduleUtils.removeTask(id, "DETECTION", false);
            return;
        }
        SysDetectionLogsModel detectionLogsModel = new SysDetectionLogsModel();
        BeanUtil.copyProperties(vo, detectionLogsModel);
        detectionLogsModel.setTeamId(vo.getTeamId());
        detectionLogsModel.setCreateTime(DateUtil.date());
        detectionLogsModel.setTaskId(vo.getId());
        detectionLogsModel.setId(null);
        detectionLogsModel.setData(null);
        try {
            String sql = Base64Decoder.decodeStr(vo.getSqlContent());
            if (ObjectUtil.isEmpty(sql)) {
                throw new NullPointerException("监测SQL为空");
            }
            List<SqlParserVo> parserVoList = SqlParserHandler.getParserVo(vo.getDataBaseName(), sql);
            SqlParserVo sqlParserVo = parserVoList.get(0);
            if (!SqlParserHandler.SELECT.equals(sqlParserVo.getMethodType())) {
                throw new RuntimeException("SQL类型不是查询语句，无法执行!");
            }
            Map<String, Object> moreResult = JdbcUtils.findMoreResult(vo.getDataBaseName(), sqlParserVo.getSqlContent(), new ArrayList<>());
            JSONArray dataArray = (JSONArray) moreResult.get("data");
            if (!dataArray.isEmpty()) {
                JSONObject data = JsonToLowerUtils.transToLowerObject(JSONObject.toJSONString(dataArray.get(0)));
                BigDecimal number = data.getBigDecimal(vo.getColumnName().toLowerCase());
                detectionLogsModel.setData(number);
            }
            detectionLogsModel.setErrorMessage(moreResult.get("msg").toString());
            //计算告警表达式并推送消息通知
            if (ObjectUtil.isNotNull(vo.getExpression()) && ObjectUtil.isNotNull(vo.getMessageId())) {
                Dict dict = Dict.create().set(vo.getColumnName().toLowerCase(), detectionLogsModel.getData());
                Object eval = ExpressionUtil.eval(vo.getExpression(), dict);
                if (ObjectUtil.equal(eval, true)) {
                    detectionLogsModel.setStateName("推送通知");
                    MessageTemplateService messageService = SpringContextHolder.getBean(MessageTemplateService.class);
                    Map<String, Object> param = new HashMap<>(3);
                    param.put("标题", vo.getName());
                    param.put("时间", DateUtil.now());
                    param.put("等级", vo.getAlarmLevel());
                    param.put("状态", "失败");
                    param.put("数据值", detectionLogsModel.getData());
                    String result = messageService.sendMessage(vo.getMessageId(), param) ? "成功" : "失败";
                    log.info("{}-监测任务触发表达式结果：{}进行告警返回结果:{}...", jobName, eval, result);
                    detectionLogsModel.setErrorMessage("发送告警信息" + result);
                } else {
                    detectionLogsModel.setStateName("无需告警");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("{}-监测任务执行失败.msg:{}", jobName, e.getMessage());
            detectionLogsModel.setErrorMessage(e.getMessage());
        } finally {
            Long end = System.currentTimeMillis();
            Long time = (end - begin);
            detectionLogsModel.setExecTime(time);
            DetectionLogsRepository detectionLogsRepository = SpringContextHolder.getBean(DetectionLogsRepository.class);
            detectionLogsRepository.save(detectionLogsModel);
            log.debug("{}-监测任务执行完成,time:{}", jobName, time);
        }
    }


}
