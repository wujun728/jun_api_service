package com.ibeetl.olexec.conf;


import com.ibeetl.starter.BeetlTemplateCustomize;
import org.beetl.core.GroupTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeetlTemplateConf {
	public static Long ver = System.currentTimeMillis();
	@Bean
	public BeetlTemplateCustomize beetlTemplateCustomize(){
		return new BeetlTemplateCustomize(){
			public void customize(GroupTemplate groupTemplate){
				groupTemplate.getSharedVars().put("jsVer",ver);

			}
		};
	}
}
