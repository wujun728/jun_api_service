package com.websql.dao;

import com.websql.model.TimingVo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimingRepository extends JpaSpecificationExecutor<TimingVo>, JpaRepository<TimingVo, Long> {


    @Query(value = "select * from sql_timing where state = '休眠'",nativeQuery = true)
    List<TimingVo> queryTimingJobList();

    @Query(value = "select * from sql_timing where title = ?1",nativeQuery = true)
    List<TimingVo> queryTimingJobByTitleName(String name);

    @Query(value = "select * from sql_timing where team_id = ?1",nativeQuery = true)
    List<TimingVo> queryListByTeamId(Long id);
}
