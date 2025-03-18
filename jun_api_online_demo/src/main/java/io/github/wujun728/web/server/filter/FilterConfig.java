package io.github.wujun728.web.server.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 * @version 1.0
 * @date 2024-05-06
 */
@Configuration
public class FilterConfig {
 
    @Bean
    public FilterRegistrationBean registFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(1);
        return registration;
    }
 
}
