package io.github.wujun728.api.core.controller;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.core.service.DataSourceService;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 数据源
 * 
 * @version 1.0
 * @date 2024-05-07
 */
@Api(tags="数据库CRUD")
@ApiSupport(author = "admin",order = 1)
@RequestMapping(value = "/lowcode/")
@RestController("lowCodeDataSQLController")
public class DataSQLController {

	@Resource
	private DataSourceService dataSourceService;

	@ApiOperation(value="数据源SQL")
	@GetMapping(value = "/sqlCRUD", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult sqlCRUD(
			@RequestParam(required = false) Integer sourceId,
			@RequestParam(required = false) String database,
			@RequestParam String sql) throws Exception {

		if(StrUtil.isBlank(sql)){
			throw new BusinessException(EnumCode.PARAMS_NOT_EMPTY);
		}
		if(sql.indexOf("$")!=-1 || sql.indexOf("#")!=-1){
			throw new BusinessException(EnumCode.VALID_FAIL,"SQL参数错误，请检查正确后再执行！");
		}
		return dataSourceService.executeSQL(sourceId,database,sql,null, false, null);
	}

}
