package com.ibeetl.olexec.entity;

import lombok.Data;
import org.beetl.sql.annotation.entity.AutoID;
import org.beetl.sql.annotation.entity.Table;

/*
 *   用户实体
 *
 */
@Table(name = "department")
@Data
public class DepartmentEntity {
	@AutoID
	private Integer id;
	private String name;

}
