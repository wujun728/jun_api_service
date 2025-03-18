package io.github.wujun728.api.core.manager;

import io.github.wujun728.api.common.config.DataSourceConfig;
import io.github.wujun728.api.common.jdbc.bean.Connector;
import io.github.wujun728.api.common.jdbc.dao.BaseDao;
import io.github.wujun728.api.common.jdbc.util.DaoException;
import io.github.wujun728.api.core.dto.DataSourceDto;
import io.github.wujun728.api.core.entity.DataSource;
import io.github.wujun728.api.core.service.DataSourceService;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;


@Service
public class SQLInitManager {

    private Logger logger  = LoggerFactory.getLogger(SQLInitManager.class);

    @Resource
    private DataSourceConfig dataSourceConfig;

    @Resource
    private DataSourceService dataSourceService;

    private String[] TABLES = {"lowcode_data_source","lowcode_api_info"};
    private String[] TABLES_SQL = {
            "CREATE TABLE`lowcode_data_source`(`source_id`int NOT NULL AUTO_INCREMENT COMMENT'数据源ID',`source_name`varchar(255)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'数据源名称',`schema`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'模式',`driver`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'驱动',`db_type`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'数据库类型',`host`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'IP主机地址',`port`varchar(40)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'端口',`user_name`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'用户名',`password`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'密码',`url`varchar(200)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'链接',`remark`varchar(255)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'备注',`create_time`datetime NULL DEFAULT NULL COMMENT'创建时间',`update_time`datetime NULL DEFAULT NULL COMMENT'更新时间',`create_by`int NULL DEFAULT NULL COMMENT'创建人ID',`update_by`int NULL DEFAULT NULL COMMENT'更新人ID',`state`varchar(1)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT'Y'COMMENT'数据状态 Y有效 N无效',PRIMARY KEY(`source_id`)USING BTREE)ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据源'ROW_FORMAT=Dynamic",
            "CREATE TABLE`lowcode_api_info`(`api_id`int NOT NULL AUTO_INCREMENT COMMENT'接口ID',`group_name`varchar(200)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'分组名称',`source_id`int NULL DEFAULT NULL COMMENT'数据源ID',`database_name`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'数据库名称',`api_name`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'接口名称',`api_path`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'接口地址 支持多级',`api_type`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'请求方式GET/POST',`op_type`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'操作类型（增删改查INSERT/DELETE/UPDATE/SELECT/SELECT_ONE）',`table_name`varchar(100)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'操作表',`params`json NULL COMMENT'参数json',`sql`text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT'sql语句',`param_type`varchar(40)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'接口创建类型 sql/param/auto',`page_flag`varchar(1)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT'N'COMMENT'是否分页 Y是 N否 默认为N',`remark`varchar(255)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT'接口描述',`create_time`datetime NULL DEFAULT NULL COMMENT'创建时间',`update_time`datetime NULL DEFAULT NULL COMMENT'更新时间',`create_by`int NULL DEFAULT NULL COMMENT'创建人ID',`update_by`int NULL DEFAULT NULL COMMENT'更新人ID',`state`varchar(1)CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT'Y'COMMENT'数据状态 Y有效 N无效',PRIMARY KEY(`api_id`)USING BTREE)ENGINE=InnoDB AUTO_INCREMENT=1 CHARACTER SET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='API信息'ROW_FORMAT=Dynamic"
    };

    //初始化操作
    public void initExecuteTask(){
        //初始化表
        initExecuteSQL();

        //创建数据源
        createDatasource();
    }


    private void createDatasource(){
        DataSource dataSource = getDefaultDatasource();
        //校验是否存在
        DataSourceDto dataSourceDto = new DataSourceDto();
        dataSourceDto.setDbType(dataSource.getDbType());
        dataSourceDto.setHost(dataSource.getHost());
        dataSourceDto.setPort(dataSource.getPort());
        dataSourceDto.setUserName(dataSource.getUserName());
        long total = dataSourceService.selectCount(dataSourceDto);
        logger.info("createDatasource select dataSource total ={}", total);
        if (total>0) {
            return;
        }
        dataSourceService.insert(dataSource);
        logger.info("createDatasource insert dataSource completed ");
    }

    /**
     * //jdbc:mysql://39.108.159.155:3306/medical_bill?useUnicode=true&useSSL=false&characterEncoding=utf-8&serverTimezone=UTC&allowPublicKeyRetrieval=true
     *     //jdbc:sqlserver://127.0.0.1:1433;databaseName=base_db?
     *     //jdbc:db2://127.0.0.1:50000/base_db?
     * @return
     */
    public DataSource getDefaultDatasource(){
        DataSource dataSource = new DataSource();
        String jdbcUrl = dataSourceConfig.getJdbcUrl();
        if (StrUtil.isNotBlank(jdbcUrl)) {
            String dbType = Connector.DB_TYPE_MYSQL;
            String port = "";
            String schema = "";
            if (jdbcUrl.indexOf(Connector.DB_TYPE_MYSQL) != -1) {
                dbType = Connector.DB_TYPE_MYSQL;

                if (jdbcUrl.indexOf("?") != -1) {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, jdbcUrl.indexOf("?"));
                } else {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
                }
                port = jdbcUrl.substring(jdbcUrl.lastIndexOf(":") + 1, jdbcUrl.lastIndexOf("/"));
            } else if (jdbcUrl.indexOf(Connector.DB_TYPE_SQLSERVER) != -1) {
                dbType = Connector.DB_TYPE_SQLSERVER;

                if (jdbcUrl.indexOf("?") != -1) {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("databaseName=") + "databaseName=".length(), jdbcUrl.indexOf("?"));
                } else {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
                }
                port = jdbcUrl.substring(jdbcUrl.lastIndexOf(":") + 1, jdbcUrl.lastIndexOf(";"));

            } else if (jdbcUrl.indexOf(Connector.DB_TYPE_DB2) != -1) {
                dbType = Connector.DB_TYPE_DB2;
                if (jdbcUrl.indexOf("?") != -1) {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1, jdbcUrl.indexOf("?"));
                } else {
                    schema = jdbcUrl.substring(jdbcUrl.lastIndexOf("/") + 1);
                }
                port = jdbcUrl.substring(jdbcUrl.lastIndexOf(":") + 1, jdbcUrl.lastIndexOf(";"));
            }
            dataSource.setDbType(dbType);

            String host = jdbcUrl.substring(jdbcUrl.indexOf("//") + 2, jdbcUrl.lastIndexOf(":"));
            dataSource.setSchema(schema);
            dataSource.setSourceName("默认数据源");
            dataSource.setDriver(dataSourceConfig.getDriverClassName());
            dataSource.setHost(host);
            dataSource.setPort(port);
            dataSource.setUserName(dataSourceConfig.getUsername());
            dataSource.setPassword(dataSourceConfig.getPassword());
            dataSource.setUrl(jdbcUrl);
            dataSource.setRemark("default");
        }
        return dataSource;
    }

    //处理SQL
    private void initExecuteSQL(){
        Connector connector = new Connector();
        connector.setSchema(dataSourceConfig.getDatabase());
        if(StrUtil.isNotBlank(dataSourceConfig.getJdbcUrl())){
            if(dataSourceConfig.getJdbcUrl().indexOf(Connector.DB_TYPE_MYSQL)!=-1){
                connector.setDbType(Connector.DB_TYPE_MYSQL);
            }else if(dataSourceConfig.getJdbcUrl().indexOf(Connector.DB_TYPE_SQLSERVER)!=-1){
                connector.setDbType(Connector.DB_TYPE_SQLSERVER);
            }else if(dataSourceConfig.getJdbcUrl().indexOf(Connector.DB_TYPE_DB2)!=-1){
                connector.setDbType(Connector.DB_TYPE_DB2);
            }
        }else{
            connector.setDbType(Connector.DB_TYPE_MYSQL);
        }
        connector.setDbType(Connector.DB_TYPE_MYSQL);
        connector.setDriver(dataSourceConfig.getDriverClassName());
        connector.setUrl(dataSourceConfig.getJdbcUrl());
        connector.setUser(dataSourceConfig.getUsername());
        connector.setPassword(dataSourceConfig.getPassword());

        BaseDao baseDao = new BaseDao(connector);
        for (int i=0;i<TABLES.length;i++){
            String sql = "SHOW TABLES LIKE '"+TABLES[i]+"'";
            try {
                List<LinkedHashMap<String, Object>> result = baseDao.findResult(sql, null, true);
                if(ObjectUtil.isEmpty(result)){
                    //新增
                    PreparedStatement statement = baseDao.getConnection().prepareStatement(TABLES_SQL[i]);
                    statement.execute();
                    logger.info("initExecuteSQL create table "+TABLES[i] );
                }
            } catch (DaoException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
