package com.websql.dao;

import com.websql.model.JobLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JobLogsRepository extends JpaSpecificationExecutor<JobLogs>, JpaRepository<JobLogs, Long> {


}
