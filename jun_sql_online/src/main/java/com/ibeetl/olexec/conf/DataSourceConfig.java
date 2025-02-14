package com.ibeetl.olexec.conf;

import com.zaxxer.hikari.HikariDataSource;
import org.beetl.sql.core.SQLManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
public class DataSourceConfig {

//	@Bean(name = "baseDataSource")
//	public DataSource datasource(Environment env) {
//		HikariDataSource ds = new HikariDataSource();
//		ds.setJdbcUrl(env.getProperty("spring.datasource.url"));
//		ds.setUsername(env.getProperty("spring.datasource.username"));
//		ds.setPassword(env.getProperty("spring.datasource.password"));
//		ds.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
//		return ds;
//	}
}



