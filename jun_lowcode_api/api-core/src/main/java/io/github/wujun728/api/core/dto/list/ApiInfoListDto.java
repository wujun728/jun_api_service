package io.github.wujun728.api.core.dto.list;

import com.alibaba.fastjson.JSON;
import io.github.wujun728.api.common.bean.Wrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * API信息分页查询参数
 * @version 1.0
 * @date 2024-05-08
 */
@ApiModel(value = "API信息分页查询参数",description = "API信息分页查询参数")
public class ApiInfoListDto extends Wrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据库名称")
	private String databaseName;

	@ApiModelProperty(value = "数据源ID")
	private String sourceId;

	@ApiModelProperty(value = "接口名称（模糊搜索）")
	private String apiNameLike;

	@ApiModelProperty(value = "接口类型")
	private String apiPath;

	@ApiModelProperty(value = "分组名称")
	private String groupName;

	@ApiModelProperty(value = "操作表 多表用,分割")
	private String tableName;

	@ApiModelProperty(value = "请求方式GET/POST/PUT/DELETE")
	private String apiType;

	@ApiModelProperty(value = "操作类型（增删改查INSERT/DELETE/UPDATE/SELECT/SELECT_ONE）")
	private String opType;

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getApiType() {
		return apiType;
	}

	public void setApiType(String apiType) {
		this.apiType = apiType;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@ApiModelProperty(value = "创建时间-开始（yyyy-MM-dd HH:mm:ss）")
	private String createTimeStart;

	@ApiModelProperty(value = "创建时间-截止（yyyy-MM-dd HH:mm:ss）")
	private String createTimeEnd;

	@ApiModelProperty(value = "数据状态 Y有效 N无效")
	private String state;

	public String getApiPath() {
		return apiPath;
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
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
