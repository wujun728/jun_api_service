package com.websql.dao;

import com.websql.model.DataSourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbSourceRepository extends JpaSpecificationExecutor<DataSourceModel>, JpaRepository<DataSourceModel, Long> {


    @Query(value = "SELECT count(*)   FROM SQL_DATASOURCE WHERE db_name= ?1", nativeQuery = true)
    Integer findDataSourceByName(String paramName);

    @Query(value = "select * from SQL_DATASOURCE where id = ?1",nativeQuery = true)
    DataSourceModel selectById(Long id);

    @Query(value = "select * from SQL_DATASOURCE where db_state = '有效'",nativeQuery = true)
    List<DataSourceModel> reloadDataSourceList();

}
