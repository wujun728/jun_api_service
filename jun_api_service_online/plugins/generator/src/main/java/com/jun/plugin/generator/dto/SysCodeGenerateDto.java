package com.jun.plugin.generator.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.jun.plugin.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * @description 代码生成基础配置
 * @author Wujun
 * @date 2023-10-09
 */
@Data
@ApiModel("代码生成基础配置")
public class SysCodeGenerateDto  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @ApiModelProperty("主键") 
    private String id;

    /**
    * 作者姓名
    */
    @ApiModelProperty("作者姓名") 
    private String authorName;

    /**
    * 类名
    */
    @ApiModelProperty("类名") 
    private String clasname;

    /**
    * 是否移除表前缀
    */
    @ApiModelProperty("是否移除表前缀") 
    private String tablePrefix;

    /**
    * 生成位置类型
    */
    @ApiModelProperty("生成位置类型") 
    private String generateType;

    /**
    * 数据库表名
    */
    @ApiModelProperty("数据库表名") 
    private String tableName;

    /**
    * 包名称
    */
    @ApiModelProperty("包名称") 
    private String packageName;

    /**
    * 业务名
    */
    @ApiModelProperty("业务名") 
    private String buname;

    /**
    * 功能名
    */
    @ApiModelProperty("功能名") 
    private String tableComment;

    /**
    * 所属应用
    */
    @ApiModelProperty("所属应用") 
    private String appCode;

    /**
    * 菜单上级
    */
    @ApiModelProperty("菜单上级") 
    private String menuPid;

    /**
    * 创建人
    */
    @ApiModelProperty("创建人") 
    private Long createUser;

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间") 
    private Date createTime;

    /**
    * 更新人
    */
    @ApiModelProperty("更新人") 
    private Long updateUser;

    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间") 
    private Date updateTime;

    public SysCodeGenerateDto() {}
}
