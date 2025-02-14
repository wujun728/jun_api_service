package com.websql.config;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.websql.model.DataSourceModel;
import com.websql.model.SysSetup;
import com.websql.service.DbSourceService;
import com.websql.service.LoginService;
import com.websql.util.CacheUtils;
import com.websql.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName DbSourceFactory
 * @Description 初始化系统配置, 数据源
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/21 0021 16:50
 **/
@Component
@Slf4j
public class DbSourceFactory {


    @Autowired
    private DbSourceService dbSourceService;

    @Autowired
    private LoginService loginService;


    public void initSystem() {
        SysSetup sysSetup = getSysSetUp();
        //系统第一次启动会加载数据
        if (sysSetup.getInitDataSource() == 0) {
            log.info("Initializing System...");
            loginService.initSystem();
        }
        if (sysSetup.getInitDataSource() == 1) {
            initDataSource();
        }
    }

    /**
     * 初始化系统配置
     *
     * @return
     */
    public SysSetup getSysSetUp() {
        SysSetup sysSetup = CacheUtils.get("sys_setup", SysSetup.class);
        if (ObjectUtil.isNull(sysSetup)) {
            sysSetup = dbSourceService.initSysSetup();
            CacheUtils.putNoDue("sys_setup", sysSetup);
        }
        return sysSetup;
    }


    public int initDataSource() {
        log.info("Initializing DataSource...");
        List<DataSourceModel> dblist = dbSourceService.reloadDataSourceList();
        for (DataSourceModel model : dblist) {
            if (ObjectUtil.isNotEmpty(model.getDbPassword())) {
                String encrypt = PasswordUtil.decrypt(model.getDbPassword());
                model.setDbPassword(encrypt);
            }
            if (ObjectUtil.isNotEmpty(model.getDbAccount())) {
                String encrypt = PasswordUtil.decrypt(model.getDbAccount());
                model.setDbAccount(encrypt);
            }
        }
        if (dblist.size() > 2) {
            log.info("dataSource Size:{}  Async initDataSource ...", dblist.size());
            ThreadUtil.execAsync(() -> DataSourceFactory.initDataSource(dblist));
        } else {
            DataSourceFactory.initDataSource(dblist);
        }
        return dblist.size();
    }
}
