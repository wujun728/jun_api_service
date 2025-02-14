package com.ibeetl.olexec.entity;

import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;

import java.util.Date;

/**
 * 角色
 */
@Table(name = "sys_user")
@Data
public class UserEntity {

	@AutoID
	private Integer id;
	private String name;
	private Integer departmentId;
	private Date createTime;

}
