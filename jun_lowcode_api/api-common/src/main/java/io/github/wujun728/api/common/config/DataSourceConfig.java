package io.github.wujun728.api.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "spring.datasource")
@Component
public class DataSourceConfig {

    private String driverClassName;

    private String jdbcUrl;

    private String username;

    private String password;

    private String database;

    private Map<String,String> druid;

    public String getDriverClassName() {
        if(druid!=null && druid.size()>0){
            driverClassName = druid.get("driver-class-name");
        }
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        if(druid!=null && druid.size()>0){
            jdbcUrl = druid.get("url");
        }
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        if(druid!=null && druid.size()>0){
            username = druid.get("username");
        }
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        if(druid!=null && druid.size()>0){
            password = druid.get("password");
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatabase() {
        if(druid!=null && druid.size()>0){
            database = druid.get("database");
        }
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Map<String, String> getDruid() {
        return druid;
    }

    public void setDruid(Map<String, String> druid) {
        this.druid = druid;
    }
}
