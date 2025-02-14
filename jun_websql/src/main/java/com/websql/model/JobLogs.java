package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @program: websql
 * @description: 定时作业日志
 * @author: rabbit boy_0214@sina.com
 * @create: 2019-09-19 10:00
 **/
@Data
@Entity
@Table(name = "sql_timing_log")
@EntityListeners(AuditingEntityListener.class)
public class JobLogs extends Pages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private Long taskId;//作业id
    @Column
    private String taskName;//作业名称

    @Column(columnDefinition = "TEXT")
    private String taskContent;//执行的sql

    @Column(columnDefinition = "TEXT")
    private String taskValue;//参数

    @Column
    private String taskState;//状态

    @Column(columnDefinition = "TEXT")
    private String taskError;//错误记录

    @Column
    private String co1;//备用1

    @Column
    private String co2;//备用2

    @Column
    private String co3;//备用3

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String executeDate;

    @Column
    private Long teamId;
}
