package com.websql.dao;

import com.websql.model.SysDetectionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectionRepository extends JpaSpecificationExecutor<SysDetectionModel>, JpaRepository<SysDetectionModel, Long> {


    @Query(value = "select  * FROM SYS_DETECTION_INFO WHERE ID= ?1", nativeQuery = true)
    SysDetectionModel selectById(Long id);
}
