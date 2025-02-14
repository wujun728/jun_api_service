package com.websql.task;


import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.cron.task.Task;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName : ScheduleUtils
 * @Description : 定时任务
 * @Author rabbit boy_0214@sina.com
 * @Date: 2024/03/25 15:08
 */
@Slf4j
public class ScheduleUtils {

    private static final Map<String, Map<Long, String>> BUSINESS_MAP = new ConcurrentHashMap<>(0);

    private ScheduleUtils() {
    }

    /**
     * 添加任务
     *
     * @param cron       执行表达式
     * @param task       执行类
     * @param taskId     任务ID
     * @param businessId 业务ID
     * @return
     */
    public static void addTask(Long taskId, String cron, Task task, String businessId) {
        Map<Long, String> taskMap = BUSINESS_MAP.get(businessId);
        if (ObjectUtil.isNull(taskMap)) {
            taskMap = new ConcurrentHashMap<>();
        }
        if (ObjectUtil.isNotNull(taskMap.get(taskId))) {
            throw new RuntimeException("添加定时任务失败，任务ID已经存在请检查!");
        }
        String schedule = CronUtil.schedule(cron, task);
        taskMap.put(taskId, schedule);
        BUSINESS_MAP.put(businessId, taskMap);
    }

    /**
     * 删除任务
     *
     * @param taskId     任务ID
     * @param businessId 业务ID
     * @param all        删除掉所有业务任务
     */
    public static void removeTask(Long taskId, String businessId, boolean all) {
        Map<Long, String> taskMap = BUSINESS_MAP.get(businessId);
        if (ObjectUtil.isNotNull(taskMap) && ObjectUtil.isNotNull(taskMap.get(taskId))) {
            CronUtil.remove(taskMap.get(taskId));
            taskMap.remove(taskId);
        }
        if (all && ObjectUtil.isNotNull(businessId)) {
            BUSINESS_MAP.remove(businessId);
        }
    }

    /**
     * 增加一个数据同步任务
     *
     * @param cron     执行时间
     * @param taskId   同步任务id
     * @param taskName 同步任务名称
     */
    public static void addTimingTask(String cron, Long taskId, String taskName) {
        JobExecuteFactory jobExecuteFactory = new JobExecuteFactory(taskId, taskName);
        addTask(taskId, cron, jobExecuteFactory, "TIMING");
    }

    public static void removeTimingTask(Long taskId) {
        removeTask(taskId, "TIMING", false);
    }

    public static void addDetectionTask(String cron, Long taskId, String taskName) {
        DetectionJobFactory detectionJobFactory = new DetectionJobFactory(taskId, taskName);
        addTask(taskId, cron, detectionJobFactory, "DETECTION");
    }

    public static void removeDetectionTask(Long taskId) {
        removeTask(taskId, "DETECTION", false);
    }


}