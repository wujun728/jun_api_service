package com.websql.dao;

import com.websql.model.TeamResourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TeamResourceRepository extends JpaSpecificationExecutor<TeamResourceModel>, JpaRepository<TeamResourceModel, Long> {

    @Transactional
    @Query("DELETE FROM TeamResourceModel WHERE resourceId  in(?1) and resourceType = ?2 ")
    @Modifying
    void deleteResourceByResId(List<Long> resIds, String type);

    @Transactional
    @Query(value = "select * FROM SYS_TEAM_RESOURCE WHERE resource_id  in(?1) and resource_type = ?2 ", nativeQuery = true)
    List<TeamResourceModel> queryTeamResourceById(List<Long> ids, String type);


    @Transactional
    @Query(value = "select * FROM SYS_TEAM_RESOURCE WHERE team_id  in(?1) and resource_type = ?2 ", nativeQuery = true)
    List<TeamResourceModel> queryTeamResourceByTeamId(List<Long> teamIds, String type);
}
