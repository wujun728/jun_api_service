package io.github.wujun728.api.core.service;

import io.github.wujun728.api.core.entity.ApiInfo;

import java.util.Map;

public interface ApiParamService {

    /**
     * 参数处理
     *
     * @param apiInfo
     * @throws Exception
     */
    public void dearApiParam(ApiInfo apiInfo) throws Exception;

    /**
     * SQL语句处理
     *
     * @param apiInfo
     * @param params
     * @throws Exception
     */
    public void dearApiSQL(ApiInfo apiInfo, Map<String, Object> params) throws Exception;

}
