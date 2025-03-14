package io.github.wujun728.api.core.dto;

import com.alibaba.fastjson.JSON;
import io.github.wujun728.api.common.bean.Wrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * API信息查询参数
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API信息查询参数",description = "API信息查询参数")
public class ApiInfoDto extends Wrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "接口ID")
	private Integer apiId;

	@ApiModelProperty(value = "不包含接口ID")
	private Integer noApiId;

	@ApiModelProperty(value = "多个接口ID,逗号(,)分隔")
	private String apiIds;

	@ApiModelProperty(value = "数据源ID")
	private Integer sourceId;

	@ApiModelProperty(value = "操作表 多表用,分割")
	private String tableName;

	@ApiModelProperty(value = "数据库名称（模糊搜索）")
	private String databaseNameLike;

	@ApiModelProperty(value = "数据库名称")
	private String databaseName;

	@ApiModelProperty(value = "接口名称（模糊搜索）")
	private String apiNameLike;

	@ApiModelProperty(value = "接口名称")
	private String apiName;

	@ApiModelProperty(value = "接口类型")
	private String apiPath;

	@ApiModelProperty(value = "接口类型 模糊查询")
	private String apiPathLike;

	@ApiModelProperty(value = "请求方式GET/POST/PUT/DELETE")
	private String apiType;

	@ApiModelProperty(value = "接口创建类型 sql语句/param自定义参数/auto 自动生成")
	private String paramType;

	@ApiModelProperty(value = "sql语句")
	private String sql;

	@ApiModelProperty(value = "参数json")
	private String params;

	@ApiModelProperty(value = "操作类型（增删改查INSERT/DELETE/UPDATE/SELECT/SELECT_ONE）")
	private String opType;

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

	@ApiModelProperty(value = "分组名称")
	private String groupName;

	public String getApiPathLike() {
		return apiPathLike;
	}

	public void setApiPathLike(String apiPathLike) {
		this.apiPathLike = apiPathLike;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getApiPath() {
		return apiPath;
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) { 
		this.apiId = apiId;
	}

	public Integer getNoApiId() {
		return noApiId;
	}

	public void setNoApiId(Integer noApiId) { 
		this.noApiId = noApiId;
	}

	public String getApiIds() {
		return apiIds;
	}

	public void setApiIds(String apiIds) { 
		this.apiIds = apiIds;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) { 
		this.sourceId = sourceId;
	}

	public String getDatabaseNameLike() {
		return databaseNameLike;
	}

	public void setDatabaseNameLike(String databaseNameLike) { 
		this.databaseNameLike = databaseNameLike;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) { 
		this.databaseName = databaseName;
	}

	public String getApiNameLike() {
		return apiNameLike;
	}

	public void setApiNameLike(String apiNameLike) { 
		this.apiNameLike = apiNameLike;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) { 
		this.apiName = apiName;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) { 
		this.apiType = apiType;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) { 
		this.sql = sql;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) { 
		this.params = params;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) { 
		this.opType = opType;
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
