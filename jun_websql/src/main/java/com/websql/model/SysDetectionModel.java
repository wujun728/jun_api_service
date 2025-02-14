package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_DETECTION_INFO")
@EntityListeners(AuditingEntityListener.class)
public class SysDetectionModel extends Pages{

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;


    /***
     * 团队ID
     */
    @Column(nullable = false)
    private Long teamId;

    /**
     * 监测任务名称
     */
    private String name;

    /**
     * 执行数据源名称
     */
    private String dataBaseName;

    /**
     * 执行sql
     */
    @Column(columnDefinition = "TEXT")
    private String sqlContent;

    /**
     * 返回值列名
     */
    private String columnName;

    /**
     * 计算表达式
     */
    private String expression;

    /**
     * 告警级别
     */
    private String alarmLevel;

    /**
     * 执行时间
     */
    private String cron;

    /**
     * 状态
     */
    private String state;

    /**
     * 创建时间
     */
    private Date createTime;

    private String createUser;

    private Long messageId;

    /**
     * 告警模板名称
     */
    @Transient
    private String messageName;
}
