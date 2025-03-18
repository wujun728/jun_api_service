package io.github.wujun728.api.core.controller;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.bean.Page;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.core.dto.add.ApiBatchAddDto;
import io.github.wujun728.api.core.dto.list.ApiInfoListDto;
import io.github.wujun728.api.core.entity.ApiInfo;
import io.github.wujun728.api.core.factory.SQLParamServiceFactory;
import io.github.wujun728.api.core.manager.SQLParamManager;
import io.github.wujun728.api.core.service.ApiInfoService;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.template.service.PageCreateService;
import io.github.wujun728.api.core.vo.ApiInfoTreeVo;
import io.github.wujun728.api.core.vo.ApiInfoVo;
import cn.hutool.core.util.ObjectUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * API信息
 * 
 * @version 1.0
 * @date 2024-05-08
 */
@Api(tags="API信息")
@ApiSupport(author = "admin",order = 1)
@RequestMapping(value = "/lowcode/")
@RestController("lowCodeApiInfoController")
public class ApiInfoController{

	@Resource
	private ApiInfoService apiInfoService;

	@Resource
	private SQLParamManager sqlParamManager;

	@Resource
	private PageCreateService pageCreateService;

	@ApiOperation(value="分页查询API信息")
	@GetMapping(value = "/apiInfos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<Page<ApiInfoVo>> pageList(ApiInfoListDto apiInfoListDto) {
		return ApiResult.page(apiInfoService.selectPage(apiInfoListDto));
	}

	@ApiOperation(value="查询API集合")
	@GetMapping(value = "/apiInfoTrees", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<ApiInfoTreeVo>> apiInfoTrees(ApiInfoListDto apiInfoListDto) {
		return ApiResult.data(apiInfoService.selectApiTrees(apiInfoListDto));
	}

	@ApiOperation(value="查询API分组")
	@GetMapping(value = "/apiGroups", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> apiGroups(@RequestParam(required = false) Integer sourceId,
											 @RequestParam(required = false) String databaseName) {
		return ApiResult.data(apiInfoService.selectApiGroups(sourceId, databaseName));
	}

	@ApiOperation(value="查询单个API信息")
	@GetMapping(value = "/apiInfo/{apiId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<ApiInfoVo> selectById(@PathVariable Integer apiId) {
		return ApiResult.data(apiInfoService.selectById(apiId));
	}

	@ApiOperation(value="新增API信息")
	@PostMapping(value = "/apiInfo",produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult insert(@RequestBody ApiInfo apiInfo) throws Exception {
		//处理sql与参数
		if (ObjectUtil.isEmpty(apiInfo.getParamVos())){
			ApiParamService apiParamService = SQLParamServiceFactory.getApiParamService(apiInfo.getOpType());
			apiParamService.dearApiParam(apiInfo);
		}
		apiInfoService.insert(apiInfo);

		if (ConstantUtil.IS_VALID_Y.equals(apiInfo.getCreatePage())){
			pageCreateService.createFile(apiInfo);
		}
		return ApiResult.success(EnumCode.INSERT_SUCCESS, apiInfo.getApiId());
	}

	@ApiOperation(value="批量生成API信息")
	@PostMapping(value = "/apiBatchCreate",produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult apiBatchCreate(@RequestBody ApiBatchAddDto apiBatchAddDto) throws Exception {
		apiInfoService.batchInsert(apiBatchAddDto);
		return ApiResult.success(EnumCode.CREATE_SUCCESS);
	}

	@ApiOperation(value="修改API信息")
	@PutMapping(value = "/apiInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult update(@RequestBody ApiInfo apiInfo) throws Exception {
		//处理sql与参数
		if (ObjectUtil.isEmpty(apiInfo.getParamVos())){
			ApiParamService apiParamService = SQLParamServiceFactory.getApiParamService(apiInfo.getOpType());
			apiParamService.dearApiParam(apiInfo);
		}
		apiInfoService.update(apiInfo);

		if (ConstantUtil.IS_VALID_Y.equals(apiInfo.getCreatePage())){
			pageCreateService.createFile(apiInfo);
		}
		return ApiResult.success(EnumCode.UPDATE_SUCCESS);
	}

	@ApiOperation(value="删除API信息")
	@DeleteMapping(value = "/apiInfo/{apiId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult deleteOne(@PathVariable Integer apiId){
		apiInfoService.delete(apiId);
		return ApiResult.success(EnumCode.DELETE_SUCCESS);
	}

	@ApiOperation(value="批量删除API信息")
	@DeleteMapping(value = "/apiInfos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult deleteBatch(@RequestBody ArrayList<Integer> apiIds){
		apiInfoService.delete(apiIds.toArray(new Integer[0]));
		return ApiResult.success(EnumCode.DELETE_SUCCESS);
	}
}
