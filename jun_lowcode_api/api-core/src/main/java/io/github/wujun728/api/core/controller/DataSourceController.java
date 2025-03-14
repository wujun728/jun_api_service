package io.github.wujun728.api.core.controller;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.bean.Page;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.bean.EntityInfo;
import io.github.wujun728.api.common.jdbc.bean.TableInfo;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.factory.EntityServiceFactory;
import io.github.wujun728.api.common.jdbc.factory.TableInfoFactory;
import io.github.wujun728.api.common.utils.BeanUtil;
import io.github.wujun728.api.core.dto.add.DataSourceAddDto;
import io.github.wujun728.api.core.dto.list.DataSourceListDto;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.manager.SQLInitManager;
import io.github.wujun728.api.core.service.DataSourceService;
import io.github.wujun728.api.core.vo.DataSourceVo;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据源
 * 
 * @version 1.0
 * @date 2024-05-07
 */
@Api(tags="数据源")
@ApiSupport(author = "admin",order = 1)
@RequestMapping(value = "/lowcode/")
@RestController("lowCodeDataSourceController")
public class DataSourceController{

	@Resource
	private DataSourceService dataSourceService;

	@Resource
	private SQLInitManager sqlInitManager;

	@ApiOperation(value="分页查询数据源")
	@GetMapping(value = "/dataSources", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<Page<DataSourceVo>> pageList(DataSourceListDto dataSourceListDto) {
		return ApiResult.page(dataSourceService.selectPage(dataSourceListDto));
	}

	@ApiOperation(value="查询单个数据源")
	@GetMapping(value = "/dataSource/{sourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<DataSourceVo> selectById(@PathVariable Integer sourceId) {
		return ApiResult.data(dataSourceService.selectById(sourceId));
	}

	@ApiOperation(value="新增数据源")
	@PostMapping(value = "/dataSource",produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult insert(@RequestBody DataSourceAddDto dataSourceAddDto) throws Exception{
		DataSource dataSource = BeanUtil.copy(dataSourceAddDto,DataSource.class);
		dataSource.setDriver(Connector.DRIVER_MAP.get(dataSourceAddDto.getDbType()));
		dataSource.setUrl(DataSource.getDefaultUrl(dataSource));
		dataSourceService.insert(dataSource);
		return ApiResult.success(EnumCode.INSERT_SUCCESS, dataSource.getSourceId());
	}

    @ApiOperation(value="测试连接")
    @PostMapping(value = "/connectTest",produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult connectTest(@RequestBody DataSourceAddDto dataSourceAddDto) throws Exception{
        DataSource dataSource = BeanUtil.copy(dataSourceAddDto,DataSource.class);
        dataSource.setDriver(Connector.DRIVER_MAP.get(dataSourceAddDto.getDbType()));
        dataSource.setUrl(DataSource.getDefaultUrl(dataSource));

        Connector connector = new Connector();
        connector.setSchema(dataSource.getSchema());
        connector.setDbType(dataSource.getDbType());
        connector.setDriver(dataSource.getDriver());
        connector.setUrl(DataSource.getNoDBUrl(dataSource));
        connector.setUser(dataSource.getUserName());
        connector.setPassword(dataSource.getPassword());

        BaseDao baseDao = new BaseDao(connector);
        Connection connection = baseDao.getConnection();
        if(connection!=null){
            baseDao.closeAll(connection, null,null);
        }
        return ApiResult.message("连接成功");
    }

	@ApiOperation(value="修改数据源")
	@PutMapping(value = "/dataSource", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult update(@RequestBody DataSource dataSource) {
		dataSource.setDriver(Connector.DRIVER_MAP.get(dataSource.getDbType()));
		dataSource.setUrl(DataSource.getDefaultUrl(dataSource));
		dataSourceService.update(dataSource);
		return ApiResult.success(EnumCode.UPDATE_SUCCESS);
	}

	@ApiOperation(value="删除数据源")
	@DeleteMapping(value = "/dataSource/{sourceId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult deleteOne(@PathVariable Integer sourceId){
		dataSourceService.delete(sourceId);
		return ApiResult.success(EnumCode.DELETE_SUCCESS);
	}

	@ApiOperation(value="批量删除数据源")
	@DeleteMapping(value = "/dataSources", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult deleteBatch(@RequestBody ArrayList<Integer> sourceIds){
		dataSourceService.delete(sourceIds.toArray(new Integer[0]));
		return ApiResult.success(EnumCode.DELETE_SUCCESS);
	}

	@ApiOperation(value="查询数据库")
	@GetMapping(value = "/databases", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<String>> databases(@RequestParam(required = false) Integer sourceId) throws Exception {
		DataSource dataSource = null;
		if(sourceId == null){
			dataSource = sqlInitManager.getDefaultDatasource();
			List<String> databases = new ArrayList<>();
			databases.add(dataSource.getSchema());
			return ApiResult.data(databases);
		}else{
			dataSource = dataSourceService.selectById(sourceId);
		}
		if(dataSource == null){
			throw new BusinessException(EnumCode.NO_DATA_FOUND);
		}

		Connector connector = new Connector();
		connector.setSchema(dataSource.getSchema());
		connector.setDbType(dataSource.getDbType());
		connector.setDriver(dataSource.getDriver());
		connector.setUrl(DataSource.getNoDBUrl(dataSource));
		connector.setUser(dataSource.getUserName());
		connector.setPassword(dataSource.getPassword());

		BaseDao baseDao = new BaseDao(connector);
		List<String> databases = baseDao.getDatabases();
		return ApiResult.data(databases);
	}

	@ApiOperation(value="查询数据库表集合")
	@GetMapping(value = "/dbTables", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<List<TableInfo>> dbTables(@RequestParam(required = false) Integer sourceId,
											   @RequestParam(required = false) String database) throws Exception {
		DataSource dataSource = null;
		if(sourceId == null){
			dataSource = sqlInitManager.getDefaultDatasource();
			database = dataSource.getSchema();
		}else{
			dataSource = dataSourceService.selectById(sourceId);
		}
		if(dataSource == null){
			throw new BusinessException(EnumCode.NO_DATA_FOUND);
		}
		Connector connector = new Connector();
		connector.setSchema(dataSource.getSchema());
		connector.setDbType(dataSource.getDbType());
		connector.setDriver(dataSource.getDriver());
		connector.setUrl(dataSource.getRealUrl(database));
		connector.setUser(dataSource.getUserName());
		connector.setPassword(dataSource.getPassword());

		List<TableInfo> tableInfos = TableInfoFactory.getTableService(connector.getDbType()).selectTables(connector);
		return ApiResult.data(tableInfos);
	}

	@ApiOperation(value="查询数据库表信息")
	@GetMapping(value = "/tableEntityInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	public ApiResult<EntityInfo> tableEntityInfo(@RequestParam(required = false) Integer sourceId,
												 @RequestParam(required = false) String database,
												 @RequestParam String tableName) throws Exception {
		DataSource dataSource = null;
		if(sourceId == null){
			dataSource = sqlInitManager.getDefaultDatasource();
			database = dataSource.getSchema();
		}else{
			dataSource = dataSourceService.selectById(sourceId);
		}
		if(dataSource == null){
			throw new BusinessException(EnumCode.NO_DATA_FOUND);
		}
		Connector connector = new Connector();
		connector.setSchema(dataSource.getSchema());
		connector.setDbType(dataSource.getDbType());
		connector.setDriver(dataSource.getDriver());
		connector.setUrl(dataSource.getRealUrl(database));
		connector.setUser(dataSource.getUserName());
		connector.setPassword(dataSource.getPassword());

		EntityInfo entityInfo = EntityServiceFactory.getEntityService(connector.getDbType()).getEntityInfo(connector, tableName);
		return ApiResult.data(entityInfo);
	}
}
