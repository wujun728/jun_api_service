package com.websql.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @ClassName
 * @Description 系统配置
 * @Author rabbit boy_0214@sina.com
 * @Date 2019/6/22 0022 1:52
 **/
@Data
@Entity
@Table(name = "SYS_SETUP")
public class SysSetup {


    public SysSetup() {

    }

    private static class SysSetupHolder {
        private final static SysSetup instance = new SysSetup();
    }

    public static SysSetup getInstance() {
        return SysSetupHolder.instance;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 主动初始化数据源
     */
    @Column
    private int initDataSource;

    /**
     * 开启sql执行记录功能
     */
    @Column
    private int enabledSqlLog;

    /**
     * 展示帮助文档按钮 0展示 1不展示
     */
    @Column
    private Integer enabledHelp;

    /**
     * 开启锁屏功能 0开启 1不开启
     */
    @Column
    private Integer enabledLockView;

    /**
     * 默认分页展示条数
     */
    @Column
    private Integer pageLimitMax;

    /**
     * 检查非法SQL
     */
    @Column(length = 500)
    private String riskText;

    /**
     * 登录失败次数
     */
    @Column(length = 500)
    private Integer failLogin;

    /**
     * 编辑器开启表字段提示 0开启 1不开启
     */
    @Column(length = 1)
    private Integer enabledHint;

    /**
     * 发送window消息 0开启 1不开启
     */
    @Column(length = 1)
    private Integer enabledNotification;
}
