package com.websql.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @ClassName SysRole
 * @Description 角色BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/26 0026 16:39
 **/
@Entity
@Table(name = "AUTH_ROLE")
@Data
public class SysRole implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long roleId;

    /**
     * 角色标识
     */
    @Column(nullable = false, unique = true)
    private String role;

    /**
     * 描述
     */
    private String description;

    /**
     * 备注
     */
    private String remark;


    @Transient
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String value;
    @Transient
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String code;
}
