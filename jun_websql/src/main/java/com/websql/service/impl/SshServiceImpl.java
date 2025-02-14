package com.websql.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.ssh.JschUtil;
import com.jcraft.jsch.Session;
import com.websql.model.SysSshDto;
import com.websql.model.SysSshModel;
import com.websql.service.SshService;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName : SshServiceImpl
 * @Description : 实现类
 * @Author : rabbit boy_0214@sina.com
 * @Date: 2023/8/15 14:33
 */
@Service
public class SshServiceImpl implements SshService {

    private static final ConcurrentHashMap<String, Session> CACHE = new ConcurrentHashMap<>(0);


    @Override
    public void createSession(SysSshModel model) {
        Session session = JschUtil.createSession(model.getHost(), model.getPort(), model.getUserName(), model.getPassword());
        if (session.isConnected()) {
            throw new RuntimeException("连接ssh异常!" + model.getTitle());
        }
        CACHE.put(model.getTitle(), session);
    }

    @Override
    public void closeSession(String title) {
        Session session = CACHE.get(title);
        if (ObjectUtil.isNotNull(session)) {
            CACHE.remove(title);
            session.disconnect();
        }
    }

    @Override
    public Session getSession(String title) {
        Session session = CACHE.get(title);
        if (ObjectUtil.isNull(session)) {
            throw new RuntimeException("获取session连接为空可能已经被关闭!" + title);
        }
        return session;
    }

    @Override
    public String exec(SysSshDto dto) {
        return JschUtil.exec(getSession(dto.getTitle()), dto.getCommand(), CharsetUtil.CHARSET_UTF_8);
    }

    @Override
    public Boolean bindPort(SysSshDto dto) {
        return JschUtil.bindPort(getSession(dto.getTitle()), dto.getRemoteHost(), dto.getRemotePort(), dto.getLocalHost(), dto.getLocalPort());
    }

    @Override
    public Boolean unBindPort(SysSshDto dto) {
        return JschUtil.unBindPort(getSession(dto.getTitle()), dto.getLocalPort());
    }
}
