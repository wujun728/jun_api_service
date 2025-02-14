package com.ibeetl.olexec.conf;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ibeetl.olexec.util.JsonResult;
import org.beetl.sql.core.engine.PageQuery;
import org.beetl.sql.core.page.PageResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;

@Configuration
public class JasonConfig {
	@Bean
	@ConditionalOnMissingBean(ObjectMapper.class)
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
		objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		SimpleModule simpleModule = new SimpleModule("SimpleModule",
				Version.unknownVersion());
		simpleModule.addSerializer(JsonResult.class, new CustomJsonResultSerializer());
		objectMapper.registerModule(simpleModule);
		return objectMapper;
	}
	/**
	 * layui 前端要求后台返回的数据格式
	 * @author xiandafu
	 *
	 */
	public static class CustomJsonResultSerializer extends JsonSerializer<JsonResult> {

	    public CustomJsonResultSerializer() {
	    }


		@Override
		public void serialize(JsonResult value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
			gen.writeStartObject();
			if(value.getCode().equals("200")) {
				gen.writeObjectField("code", 0);
			}else {
				gen.writeObjectField("code", Integer.parseInt(value.getCode()));
			}
			gen.writeStringField("msg", value.getMsg());
			Object data = value.getData();
			if(data instanceof PageResult) {
				PageResult query = (PageResult)(data);
				gen.writeObjectField("count", query.getTotalRow());
				gen.writeObjectField("data", query.getList());
			}else {

				gen.writeObjectField("data", data);
			}
			gen.writeEndObject();
		}

	}
}
