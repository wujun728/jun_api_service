package com.websql.dao;

import com.websql.model.SysDriverConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysDriverConfigRepository extends JpaSpecificationExecutor<SysDriverConfig>, JpaRepository<SysDriverConfig, Long> {

    @Query(value = "select  * FROM SYS_DRIVER_CONFIG WHERE ID= ?1", nativeQuery = true)
    SysDriverConfig selectById(Long id);
}
