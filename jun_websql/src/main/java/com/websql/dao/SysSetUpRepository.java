package com.websql.dao;

import com.websql.model.SysSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysSetUpRepository extends JpaRepository<SysSetup, Long> {

}
