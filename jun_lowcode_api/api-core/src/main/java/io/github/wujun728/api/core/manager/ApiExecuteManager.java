package io.github.wujun728.api.core.manager;


import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.bean.Page;
import io.github.wujun728.api.common.jdbc.util.SqlUtl;
import io.github.wujun728.api.common.utils.ConstantUtil;
import io.github.wujun728.api.core.factory.SQLParamServiceFactory;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.service.DataSourceService;
import io.github.wujun728.api.core.vo.ApiInfoVo;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.Method;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ApiExecuteManager {

    @Resource
    private SQLParamManager sqlParamManager;

    @Resource
    private DataSourceService dataSourceService;

    /**
     * GET
     * @param apiPath
     * @return
     * @throws Exception
     */
    public ApiResult executeGet(String apiPath, Map<String, Object> params) throws Exception {
        ApiInfoVo apiInfoVo = sqlParamManager.filterSQL(apiPath, Method.GET.name(), params);

        ApiResult apiResult = dataSourceService.executeSQL(apiInfoVo.getDataSource(), apiInfoVo.getDatabaseName(), apiInfoVo.getSql(),apiInfoVo.getSqlParams(), true,apiInfoVo.getOpType());

        if(SqlUtl.OP_TYPE_SELECT.equals(apiInfoVo.getOpType()) && ConstantUtil.IS_VALID_Y.equals(apiInfoVo.getPageFlag())){
            //分页 需要查询总数
            apiInfoVo.setSql(null);
            apiInfoVo.setOpType(SqlUtl.OP_TYPE_COUNT);

            Page<List<Map<String, Object>>> page = new Page();
            page.setRecords((List<List<Map<String, Object>>>) apiResult.getData());
            //查询总数
            ApiParamService apiParamService = SQLParamServiceFactory.getApiParamService(apiInfoVo.getOpType());
            apiParamService.dearApiSQL(apiInfoVo, params);

            List<String> result = dataSourceService.executeUniqueSQL(apiInfoVo.getDataSource(), apiInfoVo.getDatabaseName(), apiInfoVo.getSql(),apiInfoVo.getSqlParams());
            if(ObjectUtil.isNotEmpty(result)){
                page.setTotal(Long.parseLong(result.get(0)));
            }
            apiResult.setData(page);
        }
        return apiResult;
    }

    /**
     * POST
     * @param apiPath
     * @return
     * @throws Exception
     */
    public ApiResult executePost(String apiPath, JSONObject params) throws Exception {
        ApiInfoVo apiInfoVo = sqlParamManager.filterSQL(apiPath, Method.POST.name(), params);

        ApiResult apiResult = dataSourceService.executeSQL(apiInfoVo.getDataSource(), apiInfoVo.getDatabaseName(), apiInfoVo.getSql(), apiInfoVo.getSqlParams(), true,apiInfoVo.getOpType());

        if(SqlUtl.OP_TYPE_SELECT.equals(apiInfoVo.getOpType()) && ConstantUtil.IS_VALID_Y.equals(apiInfoVo.getPageFlag())){
            //分页 需要查询总数
            apiInfoVo.setSql(null);
            apiInfoVo.setOpType(SqlUtl.OP_TYPE_COUNT);

            Page<List<Map<String, Object>>> page = new Page();
            page.setRecords((List<List<Map<String, Object>>>) apiResult.getData());
            //查询总数
            ApiParamService apiParamService = SQLParamServiceFactory.getApiParamService(apiInfoVo.getOpType());
            apiParamService.dearApiSQL(apiInfoVo, params);

            List<String> result = dataSourceService.executeUniqueSQL(apiInfoVo.getDataSource(), apiInfoVo.getDatabaseName(), apiInfoVo.getSql(), apiInfoVo.getSqlParams());
            if(ObjectUtil.isNotEmpty(result)){
                page.setTotal(Long.parseLong(result.get(0)));
            }
            apiResult.setData(page);
        }

        return apiResult;
    }

    /**
     * PUT
     * @param apiPath
     * @return
     * @throws Exception
     */
    public ApiResult executePut(String apiPath, JSONObject params) throws Exception {
        ApiInfoVo apiInfoVo = sqlParamManager.filterSQL(apiPath, Method.PUT.name(), params);
        return dataSourceService.executeSQL(apiInfoVo.getDataSource(),apiInfoVo.getDatabaseName(),apiInfoVo.getSql(), apiInfoVo.getSqlParams(),true, apiInfoVo.getOpType());
    }

    /**
     * DELETE
     * @param apiPath
     * @return
     * @throws Exception
     */
    public ApiResult executeDelete(String apiPath, JSONObject params) throws Exception {
        ApiInfoVo apiInfoVo = sqlParamManager.filterSQL(apiPath, Method.DELETE.name(), params);
        return dataSourceService.executeSQL(apiInfoVo.getDataSource(),apiInfoVo.getDatabaseName(),apiInfoVo.getSql(), apiInfoVo.getSqlParams(),true, apiInfoVo.getOpType());
    }

}
