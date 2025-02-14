package com.websql.dao;

import com.websql.model.DbSqlText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName DbSqlTextRepository
 * @Description sql文本持久层
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/22 0022 2:08
 **/
public interface DbSqlTextRepository extends JpaSpecificationExecutor<DbSqlText>, JpaRepository<DbSqlText, Long> {

    @Transactional
    @Query("DELETE FROM DbSqlText WHERE id= ?1")
    @Modifying
    void delsqlText(Integer id);

    @Query(value = "select * from sql_text where team_id = ?1",nativeQuery = true)
    List<DbSqlText> queryListByTeamId(Long id);
}
