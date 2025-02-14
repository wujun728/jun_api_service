package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @ClassName : SysSshModel
 * @Description : 连接实体
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/8/15 14:36
 */
@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_SSH_DATA_SOURCE")
public class SysSshModel extends Pages {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;

    private String host;

    private Integer port;

    private String userName;

    private String password;

    private String title;

    private Integer local;

    private Date createTime;

}
