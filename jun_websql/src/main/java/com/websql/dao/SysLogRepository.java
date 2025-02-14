package com.websql.dao;

import com.websql.model.SysLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysLogRepository extends JpaSpecificationExecutor<SysLog>, JpaRepository<SysLog, Long> {


    /**
     * 查询最近一次的执行数据源
     *
     * @return
     */
    @Query(value = "SELECT LOG_DB_SOURCE   FROM SYS_LOG  order by id desc limit 1", nativeQuery = true)
    String querySysLogDataSource();

}
