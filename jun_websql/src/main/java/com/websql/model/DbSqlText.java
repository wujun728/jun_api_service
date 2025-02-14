package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName DbSqlText
 * @Description sql文本BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/22 0022 1:52
 **/
@Data
@Entity
@Table(name = "sql_text")
@EntityListeners(AuditingEntityListener.class)
public class DbSqlText extends Pages {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String sqlText;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String sqlCreateDate;

    @Column
    private String sqlCreateUser;

    @Column
    private Long teamId;
}
