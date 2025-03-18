package io.github.wujun728.api.core.entity;

import io.github.wujun728.api.common.jdbc.bean.Connector;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 数据源实体类
 * @version 1.0
 * @date 2024-05-07
 */
@TableName("lowcode_data_source")
@ApiModel(value = "数据源",description = "数据源")
public class DataSource implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String REPLACE_DB_STR = "DATA_BASE";
	public static final String MYSQL_URL_PARAMS = "/DATA_BASE?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	public static final String SQLSERVER_URL_PARAMS = ";databaseName=DATA_BASE";
	public static final String DB2_URL_PARAMS = "/DATA_BASE";

	@ApiModelProperty(value = "数据源ID")
	@TableId(type = IdType.AUTO)
	private Integer sourceId;

	@ApiModelProperty(value = "数据源名称")
	private String sourceName;

	@ApiModelProperty(value = "数据库类型")
	private String dbType;

	@TableField(value = "`schema`")
	@ApiModelProperty(value = "模式 db2 必须指定")
	private String schema;

	@TableField(value = "`driver`")
	@ApiModelProperty(value = "驱动")
	private String driver;

	@TableField(value = "`host`")
	@ApiModelProperty(value = "主机或IP")
	private String host;

	@TableField(value = "`port`")
	@ApiModelProperty(value = "端口")
	private String port;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@TableField(value = "`password`")
	@ApiModelProperty(value = "密码")
	private String password;

	//jdbc:mysql://127.0.0.1:3306/DATA_BASE?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&allowPublicKeyRetrieval=true
	//jdbc:sqlserver://127.0.0.1:1433;databaseName=DATA_BASE
	//jdbc:db2://127.0.0.1:50000/DATA_BASE
	@ApiModelProperty(value = "链接")
	private String url;

	@ApiModelProperty(value = "备注")
	private String remark;

	@ApiModelProperty(value = "创建时间")
	private String createTime;

	@ApiModelProperty(value = "更新时间")
	private String updateTime;

	@ApiModelProperty(value = "创建人ID")
	private Integer createBy;

	@ApiModelProperty(value = "更新人ID")
	private Integer updateBy;

	@ApiModelProperty(value = "数据状态 Y有效 N无效")
	private String state;

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) { 
		this.sourceId = sourceId;
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

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) { 
		this.driver = driver;
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

	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) { 
		this.remark = remark;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) { 
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) { 
		this.updateTime = updateTime;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) { 
		this.createBy = createBy;
	}

	public Integer getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Integer updateBy) { 
		this.updateBy = updateBy;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) { 
		this.state = state;
	}

	public static String getDefaultUrl(DataSource dataSource){
		String url = getNoDBUrl(dataSource);
		if (Connector.DB_TYPE_MYSQL.equals(dataSource.getDbType())){
			url += MYSQL_URL_PARAMS;
		}else if (Connector.DB_TYPE_SQLSERVER.equals(dataSource.getDbType())){
			url += SQLSERVER_URL_PARAMS;
		}else if (Connector.DB_TYPE_DB2.equals(dataSource.getDbType())){
			url += DB2_URL_PARAMS;
		}
		return url;
	}

	public String getRealUrl(String database){
		String realUrl = this.getUrl();
		if(StrUtil.isNotBlank(realUrl)){
			realUrl = realUrl.replace(REPLACE_DB_STR,database);
		}
		return realUrl;
	}

	public static String getNoDBUrl(DataSource dataSource){
		return "jdbc:"+dataSource.getDbType()+"://"+dataSource.getHost()+":"+dataSource.getPort();
	}


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
