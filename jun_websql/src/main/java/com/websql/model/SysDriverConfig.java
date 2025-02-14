package com.websql.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @ClassName SysRole
 * @Description 驱动管理
 * @Author rabbit boy_0214@sina.com
 * @Date 2023/8/07 11:19
 **/
@Entity
@Table(name = "SYS_DRIVER_CONFIG")
@Data
public class SysDriverConfig extends Pages {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;

    /**
     * 驱动名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 驱动加载类名
     */
    private String driverClass;

    /**
     * 默认驱动地址
     */
    private String url;

    /**
     * 检查数据库有效性
     */
    private String dbCheckUrl;


    /**
     * 数据源分类 内置，自定义
     */
    private String typeName;
}
