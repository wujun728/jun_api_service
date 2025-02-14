package com.websql.dao;

import com.websql.model.SysExportModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysExportLogRepository extends JpaSpecificationExecutor<SysExportModel>, JpaRepository<SysExportModel, Long> {


    @Query(value = "select  * FROM SYS_EXPORT_LOG WHERE id= ?1", nativeQuery = true)
    SysExportModel selectById(Long id);
}
