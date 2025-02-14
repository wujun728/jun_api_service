package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @ClassName : SysUserRole
 * @Description : 用户管理角色
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 23:11
 */
@Entity
@Accessors(chain = true)
@Table(name = "AUTH_USER_ROLE")
@Data
public class SysUserRole {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;

    private Long userId;

    private Long roleId;

    private String role;
}
