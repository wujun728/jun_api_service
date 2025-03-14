package io.github.wujun728.api.core.dto.add;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 数据源查询参数
 * @version 1.0
 * @date 2024-05-07
 */
@ApiModel(value = "数据源新增参数",description = "数据源新增参数")
public class DataSourceAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据源名称", required = true)
	private String sourceName;

	@ApiModelProperty(value = "数据库类型", example = "mysql", allowableValues = "mysql,db2,sqlserver", required = true)
	private String dbType;

	@ApiModelProperty(value = "模式 db2 必须指定")
	private String schema;

	@ApiModelProperty(value = "主机", required = true)
	private String host;

	@ApiModelProperty(value = "端口", example = "3306", required = true)
	private String port;

	@ApiModelProperty(value = "用户名",example = "root", required = true)
	private String userName;

	@ApiModelProperty(value = "密码", required = true)
	private String password;


	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
