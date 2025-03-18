package io.github.wujun728.api.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 配置国际化语言
 * @version 1.0
 * @date 2024-05-06
 */
@Configuration("lowCodeLocaleConfig")
public class LocaleConfig {

    /**
     * 默认解析器 其中locale表示默认语言
     */
    @Bean("lowCodeLocaleResolver")
    public LocaleResolver localeResolver() {
        return new MyLocaleResolver();
    }

}
