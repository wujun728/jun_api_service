package com.websql.controller;

import com.websql.model.AjaxResult;
import com.websql.model.ExecuteSql;
import com.websql.service.DbSourceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @ClassName : OpenApiController
 * @Description : 开放能力接口
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/3/13 15:02
 */
@RestController
@RequestMapping("/openApiManager")
public class OpenApiController {


    @Resource
    private DbSourceService dbSourceService;


    /**
     * 查询可执行的数据源
     *
     * @return
     */
    @GetMapping("/queryDataBaseList")
    public AjaxResult queryDataBaseList() {
        return AjaxResult.success(dbSourceService.dbsourceSqlList(null));
    }

    /**
     * 执行SQL
     *
     * @param executeSql
     * @return
     */
    @PostMapping("/executeSql")
    public AjaxResult executeSql(@RequestBody ExecuteSql executeSql) {
        return dbSourceService.executeSqlNew(executeSql);
    }

}
