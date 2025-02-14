package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @ClassName SysLog
 * @Description 操作日志
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/7/4 0004 11:49
 **/
@Entity
@Accessors(chain = true)
@Table(name = "SYS_USERLOG")
@Data
@EntityListeners(AuditingEntityListener.class)
public class SysUserLog extends Pages {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Integer id;

    @Column
    private String logName;

    @Column
    private Long userId;

    @Column
    private String userName;

    @Column
    private String loginFlag;

    @Column
    private String logIp;

    @Column
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String logDate;

}
