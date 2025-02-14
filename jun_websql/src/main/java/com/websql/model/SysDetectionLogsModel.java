package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @program: websql
 * @description: 定时监测日志
 * @author: rabbit boy_0214@sina.com
 * @create: 2024-10-14 11:08
 **/
@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_DETECTION_LOGS_INFO")
@EntityListeners(AuditingEntityListener.class)
public class SysDetectionLogsModel extends Pages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long taskId;

    @Column
    private String name;

    private String columnName;

    @Column
    private String expression;

    @Column
    private BigDecimal data;

    private String sqlContent;

    private String dataBaseName;

    private String errorMessage;

    private Long execTime;

    //无需告警，推送告警
    private String stateName;

    //告警等级
    private String alarmLevel;

    @Column
    private Long teamId;

    @Column
    private Date createTime;

    @Transient
    private String beginDate;

    @Transient
    private String endDate;

}
