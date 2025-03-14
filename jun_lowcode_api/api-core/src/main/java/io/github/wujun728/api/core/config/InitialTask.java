package io.github.wujun728.api.core.config;

import io.github.wujun728.api.core.manager.SQLInitManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 初始化启动执行
 * @version 1.0
 * @date 2024-05-06
 */
@Component("lowCodeInitialTask")
public class InitialTask {

    @Resource
    private SQLInitManager sqlInitManager;


    @PostConstruct
    public void init(){
        sqlInitManager.initExecuteTask();
    }
}
