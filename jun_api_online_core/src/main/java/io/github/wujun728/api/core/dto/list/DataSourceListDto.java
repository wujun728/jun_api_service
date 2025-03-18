package io.github.wujun728.api.core.dto.list;

import com.alibaba.fastjson.JSON;
import io.github.wujun728.api.common.bean.Wrapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 数据源分页查询参数
 * @version 1.0
 * @date 2024-05-07
 */
@ApiModel(value = "数据源分页查询参数",description = "数据源分页查询参数")
public class DataSourceListDto extends Wrapper implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "数据源名称（模糊搜索）")
	private String sourceNameLike;

	@ApiModelProperty(value = "数据库类型（模糊搜索）")
	private String dbTypeLike;

	@ApiModelProperty(value = "用户名（模糊搜索）")
	private String userNameLike;

	@ApiModelProperty(value = "创建时间-开始（yyyy-MM-dd HH:mm:ss）")
	private String createTimeStart;

	@ApiModelProperty(value = "创建时间-截止（yyyy-MM-dd HH:mm:ss）")
	private String createTimeEnd;

	public String getSourceNameLike() {
		return sourceNameLike;
	}

	public void setSourceNameLike(String sourceNameLike) { 
		this.sourceNameLike = sourceNameLike;
	}

	public String getDbTypeLike() {
		return dbTypeLike;
	}

	public void setDbTypeLike(String dbTypeLike) { 
		this.dbTypeLike = dbTypeLike;
	}

	public String getUserNameLike() {
		return userNameLike;
	}

	public void setUserNameLike(String userNameLike) { 
		this.userNameLike = userNameLike;
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

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
