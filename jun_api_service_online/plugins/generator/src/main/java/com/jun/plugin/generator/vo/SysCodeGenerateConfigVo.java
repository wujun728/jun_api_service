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
 * @description 代码生成详细配置
 * @author Wujun
 * @date 2023-10-09
 */
@Data
@ApiModel("代码生成详细配置")
public class SysCodeGenerateConfigVo  extends BaseEntity  implements Serializable {

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
    * 代码生成主表ID
    */
    @ApiModelProperty("代码生成主表ID") 
    @NotNull(message = "代码生成主表ID不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 20,message = "代码生成主表ID长度限制20位")
    private String codeGenId;

    /**
    * 数据库字段名
    */
    @ApiModelProperty("数据库字段名") 
    @NotNull(message = "数据库字段名不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "数据库字段名长度限制255位")
    private String columnName;

    /**
    * java类字段名
    */
    @ApiModelProperty("java类字段名") 
    @NotNull(message = "java类字段名不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "java类字段名长度限制255位")
    private String javaName;

    /**
    * 物理类型
    */
    @ApiModelProperty("物理类型") 
    @NotNull(message = "物理类型不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "物理类型长度限制255位")
    private String dataType;

    /**
    * 字段描述
    */
    @ApiModelProperty("字段描述") 
    @NotNull(message = "字段描述不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "字段描述长度限制255位")
    private String columnComment;

    /**
    * java类型
    */
    @ApiModelProperty("java类型") 
    @NotNull(message = "java类型不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "java类型长度限制255位")
    private String javaType;

    /**
    * 作用类型（字典）
    */
    @ApiModelProperty("作用类型（字典）") 
    @NotNull(message = "作用类型（字典）不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "作用类型（字典）长度限制255位")
    private String effectType;

    /**
    * 字典code
    */
    @ApiModelProperty("字典code") 
    @NotNull(message = "字典code不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "字典code长度限制255位")
    private String dictTypeCode;

    /**
    * 列表展示
    */
    @ApiModelProperty("列表展示") 
    @NotNull(message = "列表展示不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "列表展示长度限制255位")
    private String whetherTable;

    /**
    * 增改
    */
    @ApiModelProperty("增改") 
    @NotNull(message = "增改不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "增改长度限制255位")
    private String whetherAddUpdate;

    /**
    * 列表是否缩进（字典）
    */
    @ApiModelProperty("列表是否缩进（字典）") 
    @NotNull(message = "列表是否缩进（字典）不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "列表是否缩进（字典）长度限制255位")
    private String whetherRetract;

    /**
    * 是否必填（字典）
    */
    @ApiModelProperty("是否必填（字典）") 
    @NotNull(message = "是否必填（字典）不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "是否必填（字典）长度限制255位")
    private String whetherRequired;

    /**
    * 是否是查询条件
    */
    @ApiModelProperty("是否是查询条件") 
    @NotNull(message = "是否是查询条件不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "是否是查询条件长度限制255位")
    private String queryWhether;

    /**
    * 查询方式
    */
    @ApiModelProperty("查询方式") 
    @NotNull(message = "查询方式不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "查询方式长度限制255位")
    private String queryType;

    /**
    * 主键
    */
    @ApiModelProperty("主键") 
    @NotNull(message = "主键不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "主键长度限制255位")
    private String columnKey;

    /**
    * 主外键名称
    */
    @ApiModelProperty("主外键名称") 
    @NotNull(message = "主外键名称不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "主外键名称长度限制255位")
    private String columnKeyName;

    /**
    * 是否是通用字段
    */
    @ApiModelProperty("是否是通用字段") 
    @NotNull(message = "是否是通用字段不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 255,message = "是否是通用字段长度限制255位")
    private String whetherCommon;

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间") 
    @NotNull(message = "创建时间不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "创建时间长度限制19位")
    private Date createTime;

    /**
    * 创建人
    */
    @ApiModelProperty("创建人") 
    @NotNull(message = "创建人不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 20,message = "创建人长度限制20位")
    private String createUser;

    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间") 
    @NotNull(message = "修改时间不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 19,message = "修改时间长度限制19位")
    private Date updateTime;

    /**
    * 修改人
    */
    @ApiModelProperty("修改人") 
    @NotNull(message = "修改人不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 20,message = "修改人长度限制20位")
    private String updateUser;

    public SysCodeGenerateConfigVo() {}
}
