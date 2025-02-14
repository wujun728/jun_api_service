package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;

/**
 * @ClassName DbSourceModel
 * @Description 数据源BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/14 0014 16:15
 **/
@Data
@Entity
@Accessors(chain = true)
@Table(name = "SQL_DATASOURCE")
public class DataSourceModel extends Pages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String dbName;
    @Column(length = 500)
    private String dbUrl;
    @Transient
    private String dbPort;
    @Column
    private String dbAccount;
    @Column
    private String dbPassword;
    @Column
    private String dbState;
    @Column
    private String driverClass;
    @Column
    private int initialSize;
    @Column
    private int maxActive;
    @Column
    private int maxIdle;
    @Column
    private int maxWait;
    @Column
    private String dbCheckUrl;

}
