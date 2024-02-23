package com.jun.plugin.generator.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
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
public class SysCodeGenerateConfigDto  extends BaseEntity  implements Serializable {

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
    private String codeGenId;

    /**
    * 数据库字段名
    */
    @ApiModelProperty("数据库字段名") 
    private String columnName;

    /**
    * java类字段名
    */
    @ApiModelProperty("java类字段名") 
    private String javaName;

    /**
    * 物理类型
    */
    @ApiModelProperty("物理类型") 
    private String dataType;

    /**
    * 字段描述
    */
    @ApiModelProperty("字段描述") 
    private String columnComment;

    /**
    * java类型
    */
    @ApiModelProperty("java类型") 
    private String javaType;

    /**
    * 作用类型（字典）
    */
    @ApiModelProperty("作用类型（字典）") 
    private String effectType;

    /**
    * 字典code
    */
    @ApiModelProperty("字典code") 
    private String dictTypeCode;

    /**
    * 列表展示
    */
    @ApiModelProperty("列表展示") 
    private String whetherTable;

    /**
    * 增改
    */
    @ApiModelProperty("增改") 
    private String whetherAddUpdate;

    /**
    * 列表是否缩进（字典）
    */
    @ApiModelProperty("列表是否缩进（字典）") 
    private String whetherRetract;

    /**
    * 是否必填（字典）
    */
    @ApiModelProperty("是否必填（字典）") 
    private String whetherRequired;

    /**
    * 是否是查询条件
    */
    @ApiModelProperty("是否是查询条件") 
    private String queryWhether;

    /**
    * 查询方式
    */
    @ApiModelProperty("查询方式") 
    private String queryType;

    /**
    * 主键
    */
    @ApiModelProperty("主键") 
    private String columnKey;

    /**
    * 主外键名称
    */
    @ApiModelProperty("主外键名称") 
    private String columnKeyName;

    /**
    * 是否是通用字段
    */
    @ApiModelProperty("是否是通用字段") 
    private String whetherCommon;

    /**
    * 创建时间
    */
    @ApiModelProperty("创建时间") 
    private Date createTime;

    /**
    * 创建人
    */
    @ApiModelProperty("创建人") 
    private String createUser;

    /**
    * 修改时间
    */
    @ApiModelProperty("修改时间") 
    private Date updateTime;

    /**
    * 修改人
    */
    @ApiModelProperty("修改人") 
    private String updateUser;

    public SysCodeGenerateConfigDto() {}
}
