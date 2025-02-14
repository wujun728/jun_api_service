package com.websql.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @ClassName SysExportModel
 * @Description 导出记录
 * @Author rabbit boy_0214@sina.com
 * @Date 2024/8/28 18:41
 **/
@Entity
@Table(name = "SYS_EXPORT_LOG")
@Data
public class SysExportModel extends Pages {

    @Id
    @GenericGenerator(name = "generator", strategy = "native")
    @GeneratedValue(generator = "generator")
    private Long id;

    /**
     * sql文本
     */
    @Column
    private String sqlText;

    /**
     * 数据源名称
     */
    @Column
    private String dataBaseName;

    /**
     * 导出人
     */
    @Column
    private String userId;


    /**
     * 导出时间
     */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date beginDate;

    /**
     * 导出时间
     */
    @Column
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 文件地址
     */
    private String files;

    /**
     * 状态
     */
    private String state;

    /**
     * 描述信息
     */
    private String message;


}
