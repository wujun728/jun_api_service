package com.jun.plugin.generator.entity;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jun.plugin.common.entity.BaseEntity;

/**
 * @description 代码生成基础配置
 * @author Wujun
 * @date 2023-10-09
 */
@Data
@TableName("sys_code_generate")
public class SysCodeGenerateEntity  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id" ,type = IdType.AUTO )
    private String id;

    /**
    * 作者姓名
    */
    @TableField(value = "author_name" )
    private String authorName;

    /**
    * 类名
    */
    @TableField(value = "class_name" )
    private String clasname;

    /**
    * 是否移除表前缀
    */
    @TableField(value = "table_prefix" )
    private String tablePrefix;

    /**
    * 生成位置类型
    */
    @TableField(value = "generate_type" )
    private String generateType;

    /**
    * 数据库表名
    */
    @TableField(value = "table_name" )
    private String tableName;

    /**
    * 包名称
    */
    @TableField(value = "package_name" )
    private String packageName;

    /**
    * 业务名
    */
    @TableField(value = "bus_name" )
    private String buname;

    /**
    * 功能名
    */
    @TableField(value = "table_comment" )
    private String tableComment;

    /**
    * 所属应用
    */
    @TableField(value = "app_code" )
    private String appCode;

    /**
    * 菜单上级
    */
    @TableField(value = "menu_pid" )
    private String menuPid;

    /**
    * 创建人
    */
    @TableField(value = "create_user" )
    private Long createUser;

    /**
    * 创建时间
    */
    @TableField(value = "create_time" )
    private Date createTime;

    /**
    * 更新人
    */
    @TableField(value = "update_user" )
    private Long updateUser;

    /**
    * 更新时间
    */
    @TableField(value = "update_time" )
    private Date updateTime;

    public SysCodeGenerateEntity() {}
}
