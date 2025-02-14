package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @ClassName SysUser
 * @Description 用户BEAN
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/26 0026 16:37
 **/
@Entity
@Table(name = "AUTH_USER")
@Data
public class SysUser extends Pages implements Serializable {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long userId;
    /**
     * 登录账号
     */
    @Column(nullable = false, unique = true)
    private String userName;

    /**
     * 昵称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 密码
     */
    @Column(nullable = false)
    private String password;

    /**
     * 状态 0有效 1无效
     */
    private Integer state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String email;

    /**
     * 所属团队id，多个#分割
     */
    @Transient
    private String sysTeamName;

    @Transient
    private List<SysUserRole> sysRoles;

    @Transient
    private String sysRoleName;

    @Transient
    private String stateName;

}
