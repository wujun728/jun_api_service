package io.github.wujun728.api.core.dto;

import com.alibaba.fastjson.JSON;
import io.github.wujun728.api.common.bean.Wrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 数据源查询参数
 * @version 1.0
 * @date 2024-05-07
 */
@ApiModel(value = "数据源查询参数",description = "数据源查询参数")
public class DataSourceDto extends Wrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据源ID")
	private Integer sourceId;

	@ApiModelProperty(value = "不包含数据源ID")
	private Integer noSourceId;

	@ApiModelProperty(value = "多个数据源ID,逗号(,)分隔")
	private String sourceIds;

	@ApiModelProperty(value = "数据源名称（模糊搜索）")
	private String sourceNameLike;

	@ApiModelProperty(value = "模式 db2 必须指定")
	private String schema;

	@ApiModelProperty(value = "数据源名称")
	private String sourceName;

	@ApiModelProperty(value = "数据库类型（模糊搜索）")
	private String dbTypeLike;

	@ApiModelProperty(value = "数据库类型")
	private String dbType;

	@ApiModelProperty(value = "驱动")
	private String driver;

	@ApiModelProperty(value = "host")
	private String host;

	@ApiModelProperty(value = "端口")
	private String port;

	@ApiModelProperty(value = "用户名（模糊搜索）")
	private String userNameLike;

	@ApiModelProperty(value = "用户名")
	private String userName;

	@ApiModelProperty(value = "密码")
	private String password;

	@ApiModelProperty(value = "创建时间-开始（yyyy-MM-dd HH:mm:ss）")
	private String createTimeStart;

	@ApiModelProperty(value = "创建时间-截止（yyyy-MM-dd HH:mm:ss）")
	private String createTimeEnd;

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

	public Integer getNoSourceId() {
		return noSourceId;
	}

	public void setNoSourceId(Integer noSourceId) { 
		this.noSourceId = noSourceId;
	}

	public String getSourceIds() {
		return sourceIds;
	}

	public void setSourceIds(String sourceIds) { 
		this.sourceIds = sourceIds;
	}

	public String getSourceNameLike() {
		return sourceNameLike;
	}

	public void setSourceNameLike(String sourceNameLike) { 
		this.sourceNameLike = sourceNameLike;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) { 
		this.sourceName = sourceName;
	}

	public String getDbTypeLike() {
		return dbTypeLike;
	}

	public void setDbTypeLike(String dbTypeLike) { 
		this.dbTypeLike = dbTypeLike;
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

	public String getUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(String userNameLike) { 
		this.userNameLike = userNameLike;
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

	public String getCreateTimeStart() {
		return createTimeStart;
	}

	public void setCreateTimeStart(String createTimeStart) { 
		this.createTimeStart = createTimeStart;
	}

	public String getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(String createTimeEnd) { 
		this.createTimeEnd = createTimeEnd;
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


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
