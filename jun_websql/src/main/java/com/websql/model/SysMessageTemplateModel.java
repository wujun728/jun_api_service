package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: websql
 * @description: 告警模板实体
 * @author: rabbit boy_0214@sina.com
 * @create: 2024-10-16 11:33
 **/
@Accessors(chain = true)
@Data
@Entity
@Table(name = "SYS_MESSAGE_TEMPLATE_INFO")
@EntityListeners(AuditingEntityListener.class)
public class SysMessageTemplateModel extends Pages {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String url;

    @Column
    private String secret;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    private Date createTime;

    private String createUser;

}
