package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @ClassName TeamSourceModel
 * @Description 团队管理功能
 * @Author rabbit boy_0214@sina.com
 * @Date 2024/03/21 18:15
 **/

@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_TEAM_SOURCE")
public class TeamSourceModel extends Pages implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;


    /***
     * 团队负责人ID
     */
    @Column(nullable = false)
    private Long userId;

    /**
     * 团队名称
     */
    @Column
    private String teamName;

    /**
     * 团队描述
     */
    @Column(nullable = false)
    private String description;


    /**
     * 状态 0有效 1无效
     */
    @Column
    private Integer state;

    /**
     * 创建时间
     */
    private Date createTime;

    /***
     * 团队负责人名称
     */
    @Transient
    private String userName;
    /***
     * 状态名称
     */
    @Transient
    private String stateName;
}

