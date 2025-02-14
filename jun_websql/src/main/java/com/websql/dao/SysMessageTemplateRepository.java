package com.websql.dao;

import com.websql.model.SysMessageTemplateModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SysMessageTemplateRepository extends JpaSpecificationExecutor<SysMessageTemplateModel>, JpaRepository<SysMessageTemplateModel, Long> {

    @Query(value = "select  * FROM SYS_MESSAGE_TEMPLATE_INFO WHERE id= ?1", nativeQuery = true)
    SysMessageTemplateModel getConfigById(Long id);
}
