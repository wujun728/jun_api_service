package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName DbSourceModel
 * @Description 数据源BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 16:15
 **/
@Data
@Entity
@Table(name = "sql_timing")
@EntityListeners(AuditingEntityListener.class)
public class TimingVo extends Pages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 作业数据源
     */
    @Column
    private String timingName;
    /**
     * 同步数据源
     */
    @Column
    private String syncName;

    /**
     * 同步表名
     */
    @Column
    private String syncTable;
    /**
     * 执行时间
     */
    @Column
    private String executeTime;

    /**
     * 执行的sql
     */
    @Column(columnDefinition = "TEXT")
    private String sqlText;

    /**
     * 状态
     */
    @Column
    private String state;
    /**
     * 任务标题
     */
    @Column
    private String title;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String sqlCreateDate;

    @Column
    private String sqlCreateUser;

    @Column
    private Long teamId;

}
