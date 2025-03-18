package io.github.wujun728.web.server.controller;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.core.manager.ApiExecuteManager;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * API信息
 * 
 * @version 1.0
 * @date 2024-05-08
 */
@Api(tags="API请求")
@ApiSupport(author = "admin",order = 1)
@RequestMapping(value = "/lowcode/")
@RestController("lowCodeApiRequestController")
public class ApiRequestController{

	@Resource
	private ApiExecuteManager apiExecuteManager;

	@ApiOperation(value="API接口调用GET")
	@GetMapping(value = "/api/{apiPath}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult apiAccessGet(@PathVariable String apiPath, HttpServletRequest request) throws Exception {
		Map<String, Object> map = initRequestMap(request);

		String path = apiPath;
		String fileName = extractPathFromPattern(request);
		if (StrUtil.isNotBlank(fileName)) {
			path += "/" + fileName;
		}
		return apiExecuteManager.executeGet(path, map);
	}

	@ApiOperation(value="API接口调用POST")
	@PostMapping(value = "/api/{apiPath}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult apiAccessPost(@PathVariable String apiPath, @RequestBody JSONObject params, HttpServletRequest request,
								   HttpServletResponse response) throws Exception {
		initPost(params);

		String path = apiPath;
		String fileName = extractPathFromPattern(request);
		if (StrUtil.isNotBlank(fileName)) {
			path += "/" + fileName;
		}
		return apiExecuteManager.executePost(path,params);
	}

	@ApiOperation(value="API接口调用PUT")
	@PutMapping(value = "/api/{apiPath}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult apiAccessPut(@PathVariable String apiPath, @RequestBody JSONObject params, HttpServletRequest request,
								   HttpServletResponse response) throws Exception {
		initPut(params);

		String path = apiPath;
		String fileName = extractPathFromPattern(request);
		if (StrUtil.isNotBlank(fileName)) {
			path += "/" + fileName;
		}
		return apiExecuteManager.executePut(path,params);
	}

	@ApiOperation(value="API接口调用DELETE")
	@DeleteMapping(value = "/api/{apiPath}/**", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult apiAccessDelete(@PathVariable String apiPath, @RequestBody JSONObject params, HttpServletRequest request,
								  HttpServletResponse response) throws Exception {
		String path = apiPath;
		String fileName = extractPathFromPattern(request);
		if (StrUtil.isNotBlank(fileName)) {
			path += "/" + fileName;
		}
		return apiExecuteManager.executeDelete(path,params);
	}

	/**
	 * 处理路径
	 * @param request
	 * @return
	 */
	public static String extractPathFromPattern(final HttpServletRequest request) {
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
	}


	/**
	 * 获取参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> initRequestMap(HttpServletRequest request) {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String[]> params = request.getParameterMap();
		if (params != null) {
			for (Map.Entry<String, String[]> entry : params.entrySet()) {
				String key = entry.getKey();
				String[] value = entry.getValue();
				if (StrUtil.isNotBlank(key)
						&& value != null && value.length > 0) {
					if (StrUtil.isNotBlank(value[0])){
						if(value.length==1){
							map.put(key, value[0]);
						}else{
							map.put(key, value);
						}
					}
				}
			}
		}
		return map;
	}


	private void initPost(JSONObject params){
		params.put("createTime", DateUtil.now());
		params.put("updateTime", DateUtil.now());
		params.put("state", ConstantUtil.IS_VALID_Y);
	}

	private void initPut(JSONObject params){
		params.put("updateTime", DateUtil.now());
	}
}
