package io.github.wujun728.api.core.manager;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.core.dto.ApiInfoDto;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.factory.SQLParamServiceFactory;
import io.github.wujun728.api.core.service.ApiInfoService;
import io.github.wujun728.api.core.service.ApiParamService;
import io.github.wujun728.api.core.service.DataSourceService;
import io.github.wujun728.api.core.vo.ApiInfoVo;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * SQL参数处理
 */
@Service
public class SQLParamManager {


    @Resource
    private ApiInfoService apiInfoService;

    @Resource
    private SQLInitManager sqlInitManager;

    @Resource
    private DataSourceService dataSourceService;

    public ApiInfoVo filterSQL(String apiPath, String apiType, Map<String, Object> params) throws Exception {
        //根据path查询ApiInfo
        ApiInfoDto apiInfoDto = new ApiInfoDto();
        apiInfoDto.setApiPath(apiPath);
        apiInfoDto.setApiType(apiType);
        List<ApiInfoVo> apiInfoVos = apiInfoService.selectList(apiInfoDto);
        if (ObjectUtil.isEmpty(apiInfoVos)) {
            throw new BusinessException(EnumCode.PARAMS_ERROR, "请求地址错误");
        }
        ApiInfoVo apiInfoVo = apiInfoVos.get(0);

        //获取数据源
        DataSource dataSource = null;
        if(apiInfoVo.getSourceId()==null){
            dataSource = sqlInitManager.getDefaultDatasource();
            apiInfoVo.setDatabaseName(dataSource.getSchema());
        }else{
            dataSource = dataSourceService.selectById(apiInfoVo.getSourceId());
        }

        if(dataSource == null){
            throw new BusinessException(EnumCode.NO_DATA_FOUND, "数据源未查到");
        }
        apiInfoVo.setDataSource(dataSource);

        ApiParamService apiParamService = SQLParamServiceFactory.getApiParamService(apiInfoVo.getOpType());
        apiParamService.dearApiSQL(apiInfoVo, params);

        return apiInfoVo;
    }

}
