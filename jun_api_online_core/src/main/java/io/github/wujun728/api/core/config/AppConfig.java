package io.github.wujun728.api.core.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;


@MapperScan("io.github.wujun728.**.mapper.**")
@Configuration("lowCodeAppConfig")
public class AppConfig {

}
