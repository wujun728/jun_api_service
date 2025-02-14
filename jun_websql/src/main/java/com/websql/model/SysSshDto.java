package com.websql.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName : SysSshModel
 * @Description : 连接实体
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/8/15 14:36
 */
@Accessors(chain = true)
@Data
public class SysSshDto implements Serializable {


    /**
     * 远程ip
     */
    private String remoteHost;

    /**
     * 远程端口
     */
    private Integer remotePort;

    /**
     * 本地ip
     */
    private String localHost;

    /**
     * 本地端口
     */
    private Integer localPort;

    /**
     * 名称
     */
    private String title;


    /**
     * 命令
     */
    private String command;

}
