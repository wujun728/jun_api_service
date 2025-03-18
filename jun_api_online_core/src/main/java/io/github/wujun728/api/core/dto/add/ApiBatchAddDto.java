package io.github.wujun728.api.core.dto.add;

import io.github.wujun728.api.common.jdbc.bean.TableInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 数据源查询参数
 * @version 1.0
 * @date 2024-05-07
 */
@ApiModel(value = "API批量生成参数",description = "API批量生成参数")
public class ApiBatchAddDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据源ID")
	private Integer sourceId;

	@ApiModelProperty(value = "数据库名称")
	private String databaseName;

	@ApiModelProperty(value = "表名集合")
	private List<TableInfo> tableInfos;

	public Integer getSourceId() {
		return sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public List<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public void setTableInfos(List<TableInfo> tableInfos) {
		this.tableInfos = tableInfos;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
}
