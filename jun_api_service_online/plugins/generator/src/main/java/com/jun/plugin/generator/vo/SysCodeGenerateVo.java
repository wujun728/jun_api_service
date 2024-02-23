package com.jun.plugin.generator.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
public class SysCodeGenerateVo  extends BaseEntity  implements Serializable {

    public interface Retrieve{}
    public interface Delete {}
    public interface Update {}
    public interface Create {}

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
    @NotNull(message = "包名称不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "包名称长度限制255位")
    private String packageName;

    /**
    * 业务名
    */
    @ApiModelProperty("业务名") 
    @NotNull(message = "业务名不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "业务名长度限制255位")
    private String buname;

    /**
    * 功能名
    */
    @ApiModelProperty("功能名") 
    @NotNull(message = "功能名不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "功能名长度限制255位")
    private String tableComment;

    /**
    * 所属应用
    */
    @ApiModelProperty("所属应用") 
    @NotNull(message = "所属应用不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "所属应用长度限制255位")
    private String appCode;

    /**
    * 菜单上级
    */
    @ApiModelProperty("菜单上级") 
    @NotNull(message = "菜单上级不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "菜单上级长度限制255位")
    private String menuPid;

    /**
    * 创建人
    */
    @ApiModelProperty("创建人") 
    @NotNull(message = "创建人不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "创建人长度限制19位")
    private Long createUser;

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间") 
    @NotNull(message = "创建时间不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "创建时间长度限制19位")
    private Date createTime;

    /**
    * 更新人
    */
    @ApiModelProperty("更新人") 
    @NotNull(message = "更新人不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "更新人长度限制19位")
    private Long updateUser;

    /**
    * 更新时间
    */
    @ApiModelProperty("更新时间") 
    @NotNull(message = "更新时间不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "更新时间长度限制19位")
    private Date updateTime;

    public SysCodeGenerateVo() {}
}
