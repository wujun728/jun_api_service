package com.websql.task;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.cron.task.Task;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.websql.model.VersionModel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ExamineVersionFactory
 * @Description : 版本检查
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 13:12
 */
@Slf4j
@Component
public class ExamineVersionFactory implements Task {

    @Value("${examine.version.enabled}")
    private Boolean enabled;

    @Getter
    private VersionModel versionModel;



    public void run() {
        versionModel = JSON.parseObject(ResourceUtil.readUtf8Str("version.json"), VersionModel.class);
        versionModel.setLocalVersion(versionModel.getVersion());
        if (!enabled) {
            return;
        }
        execute();
        ScheduleUtils.addTask(10000L, "0 0 10 * * ?", this,"SYSTEM");
    }

    @Override
    public void execute() {
        try {
            VersionModel remoteVersion = JSON.parseObject(HttpUtil.get("https://gitee.com/boy_0214/websql/raw/master/src/main/resources/version.json", CharsetUtil.CHARSET_UTF_8), VersionModel.class);
            log.info("remote new version :{},release:{}", remoteVersion.getVersion(), remoteVersion.getDate());
            String localVersion = versionModel.getVersion();
            int compare = VersionComparator.INSTANCE.compare(remoteVersion.getVersion(), localVersion);
            if (compare != 0) {
                versionModel = remoteVersion;
                versionModel.setPush(true);
                versionModel.setLocalVersion(localVersion);
            }
        } catch (Exception e) {
            log.error("pull remote new version error!");
        }
    }


}
