package io.github.wujun728.web.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * api-server启动类
 *
 * @version 1.0
 * @date 2024-05-06
 */
@EnableAsync
@ComponentScan({"io.github.wujun728"})
@SpringBootApplication
public class ApiWebServerApplication{

    public static void main(String[] args) {
        SpringApplication.run(ApiWebServerApplication.class, args);
    }
}
