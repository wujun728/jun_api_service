package com.websql.util;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @ClassName : EnvBeanUtil
 * @Description : 获取环境变量
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/3/13 15:35
 */
@Component
public class EnvBeanUtil implements EnvironmentAware {

    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static String getString(String key) {
        return env.getProperty(key);
    }

    public static Boolean getBoolean(String key) {
        return env.getProperty(key, Boolean.class);
    }
}
