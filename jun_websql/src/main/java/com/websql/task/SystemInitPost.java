package com.websql.task;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import com.websql.config.DbSourceFactory;
import com.websql.dao.DetectionRepository;
import com.websql.model.SysDetectionModel;
import com.websql.model.TimingVo;
import com.websql.service.TimingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @ClassName : SystemInitPost
 * @Description : 统一管理系统初始化任务
 * @Author rabbit boy_0214@sina.com
 * @Date: 2024/03/25 16:35
 */
@Component
@Slf4j
public class SystemInitPost {

    @Autowired
    private DbSourceFactory dbSourceFactory;

    @Autowired
    private ExamineVersionFactory examineVersionFactory;

    @Autowired
    private TimingService timingService;

    @Autowired
    private ExcelClearFactory excelClearFactory;

    @Autowired
    private DetectionRepository detectionRepository;


    /**
     * 初始化作业任务，需要依赖数据源加载完成后执行。
     */
    void initTask() {
        List<TimingVo> initList = timingService.queryTimingJobList(null);
        log.info("Successful initialization  {}  timerTask.", initList.size());
        ThreadUtil.execAsync(() -> {
            for (TimingVo timingVo : initList) {
                ScheduleUtils.addTimingTask(timingVo.getExecuteTime(), timingVo.getId(), timingVo.getTimingName());
            }
        });
    }

    /**
     * 加载定时任务
     */
    @PostConstruct
    void initCron() {
        CronUtil.setMatchSecond(true);
        CronUtil.start();
    }

    /**
     * 加载数据源
     */
    @PostConstruct
    void initDataSource() {
        dbSourceFactory.initSystem();
        initTask();
        initDetectionTask();
    }

    /**
     * 加载版本检查
     */
    @PostConstruct
    void examineVersionFactory() {
        examineVersionFactory.run();
    }

    /**
     * 加载需要清理的历史文件
     */
    @PostConstruct
    void excelClearFactory() {
        excelClearFactory.run();
    }

    /**
     * 加载监测任务初始化调度
     */
    void initDetectionTask() {
        SysDetectionModel param = new SysDetectionModel();
        param.setState("开始监控");
        List<SysDetectionModel> initList = detectionRepository.findAll(Example.of(param));
        log.info("Successful initialization  {}  detection Task.", initList.size());
        ThreadUtil.execAsync(() -> {
            for (SysDetectionModel vo : initList) {
                ScheduleUtils.addDetectionTask(vo.getCron(), vo.getId(), vo.getName());
            }
        });
    }

}
