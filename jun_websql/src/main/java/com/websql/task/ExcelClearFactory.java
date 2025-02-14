package com.websql.task;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.cron.task.Task;
import com.websql.dao.SysExportLogRepository;
import com.websql.model.SysExportModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName : ExcelClearFactory
 * @Description : 历史文件删除
 * @Author rabbit boy_0214@sina.com
 * @Date: 2024/10/08 17:12
 */
@Slf4j
@Component
public class ExcelClearFactory implements Task {


    @Resource
    private SysExportLogRepository sysExportLogRepository;


    @Value("${export.config.clear}")
    private Boolean enabled;

    public void run() {
        if (!enabled) {
            return;
        }
        execute();
        ScheduleUtils.addTask(20000L, "0 0 1 * * ?", this,"SYSTEM");
    }

    @Override
    public void execute() {
        try {
            List<SysExportModel> list = sysExportLogRepository.findAll();
            for (SysExportModel model : list) {
                if (ObjectUtil.isNotEmpty(model.getFiles())) {
                    FileUtil.del(model.getFiles());
                    sysExportLogRepository.delete(model);
                }
            }
        } catch (Exception e) {
            log.error("Excel Clear error!");
        }
    }


}
