package io.github.wujun728.api.core.entity;

import io.github.wujun728.api.core.vo.SQLParamVo;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * API信息实体类
 * @version 1.0
 * @date 2024-05-08
 */
@TableName("lowcode_api_info")
@ApiModel(value = "API信息",description = "API信息")
public class ApiInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String PARAM_TYPE_SQL = "sql";
	public static final String PARAM_TYPE_PARAM = "param";
	public static final String PARAM_TYPE_AUTO = "auto";

	@ApiModelProperty(value = "接口ID")
	@TableId(type = IdType.AUTO)
	private Integer apiId;

	@ApiModelProperty(value = "数据源ID")
	private Integer sourceId;

	@ApiModelProperty(value = "分组名称")
	private String groupName;

	@ApiModelProperty(value = "数据库名称")
	private String databaseName;

	@ApiModelProperty(value = "接口名称")
	private String apiName;

	@ApiModelProperty(value = "操作表")
	private String tableName;

	@ApiModelProperty(value = "接口地址 支持多级")
	private String apiPath;

	@ApiModelProperty(value = "请求方式GET/POST/PUT/DELETE")
	private String apiType;

	@ApiModelProperty(value = "sql语句")
	@TableField(value="`sql`")
	private String sql;

	@ApiModelProperty(value = "参数json")
	private String params;

	@ApiModelProperty(value = "接口创建类型 sql语句/param自定义参数/auto 自动生成")
	private String paramType;

	@TableField(exist = false)
	@ApiModelProperty(value = "参数集合")
	private List<SQLParamVo> paramVos;

	@ApiModelProperty(value = "是否分页 Y是 N否 默认N")
	private String pageFlag;

	@ApiModelProperty(value = "操作类型（增删改查INSERT/DELETE/UPDATE/SELECT/SELECT_ONE）")
	private String opType;

	@ApiModelProperty(value = "接口描述")
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

	@TableField(exist = false)
	@ApiModelProperty(value = "是否创建页面 Y 是 N否")
	private String createPage;

	@TableField(exist = false)
	@ApiModelProperty(value = "唯一性校验字段集合")
	private List<String> verifyColumns;

	/**
	 * 传递参数使用
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "删除API地址")
	private String deleteApiPath;

	/**
	 * 仅传递参数用
	 */
	@TableField(exist = false)
	@ApiModelProperty(value = "SQL参数集合")
	private List<Object> sqlParams;

	/**
	 * 参数传递
	 */
	@JsonIgnore
	@JSONField(serialize = false)
	@TableField(exist = false)
	@ApiModelProperty(value = "数据源名称")
	private DataSource dataSource;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public List<String> getVerifyColumns() {
		return verifyColumns;
	}

	public void setVerifyColumns(List<String> verifyColumns) {
		this.verifyColumns = verifyColumns;
	}

	public String getDeleteApiPath() {
		return deleteApiPath;
	}

	public void setDeleteApiPath(String deleteApiPath) {
		this.deleteApiPath = deleteApiPath;
	}

	public String getCreatePage() {
		return createPage;
	}

	public void setCreatePage(String createPage) {
		this.createPage = createPage;
	}

	public List<Object> getSqlParams() {
		return sqlParams;
	}

	public void setSqlParams(List<Object> sqlParams) {
		this.sqlParams = sqlParams;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public List<SQLParamVo> getParamVos() {
		if(StrUtil.isNotBlank(params)){
			paramVos = JSON.parseArray(params, SQLParamVo.class);
		}
		return paramVos;
	}

	public void setParamVos(List<SQLParamVo> paramVos) {
		this.paramVos = paramVos;
	}

	public String getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(String pageFlag) {
		this.pageFlag = pageFlag;
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

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) { 
		this.apiId = apiId;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) { 
		this.sourceId = sourceId;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) { 
		this.databaseName = databaseName;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) { 
		this.apiName = apiName;
	}

	public String getApiPath() {
		return apiPath;
	}

	public void setApiPath(String apiPath) { 
		this.apiPath = apiPath;
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


	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
