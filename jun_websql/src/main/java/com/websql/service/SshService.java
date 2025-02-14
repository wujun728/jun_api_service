package com.websql.service;

import com.jcraft.jsch.Session;
import com.websql.model.SysSshDto;
import com.websql.model.SysSshModel;

/**
 * @ClassName : SshService
 * @Description : ssh服务
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/8/15 14:32
 */
public interface SshService {

    /**
     * 创建ssh
     */
    void createSession(SysSshModel model);

    /**
     * 关闭连接
     * @param title 名称
     */
    void closeSession(String title);

    /**
     * 获取连接
     *
     * @param title 名称
     * @return
     */
    Session getSession(String title);

    /**
     * 执行名称
     */
    String exec(SysSshDto dto);


    /**
     * ssh 端口映射
     *
     * @return
     */
    Boolean bindPort(SysSshDto dto);


    /**
     * 解绑
     *
     * @return
     */
    Boolean unBindPort(SysSshDto dto);


}
