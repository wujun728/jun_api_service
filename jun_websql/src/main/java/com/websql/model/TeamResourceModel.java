package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * @ClassName TeamResourceModel
 * @Description 团队授权资源
 * @Author rabbit boy_0214@sina.com
 * @Date 2024/03/22 18:26
 **/

@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_TEAM_RESOURCE")
public class TeamResourceModel extends Pages implements Serializable {

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
     * 资源ID
     */
    @Column
    private Long resourceId;

    /**
     * 资源类型 ，USER/DATASOURCE
     */
    @Column(nullable = false)
    private String resourceType;


    /**
     * 创建时间
     */
    private Date createTime;

    /***
     * 角色类型，负责人，研发人员
     */
    private String roleType;

}

