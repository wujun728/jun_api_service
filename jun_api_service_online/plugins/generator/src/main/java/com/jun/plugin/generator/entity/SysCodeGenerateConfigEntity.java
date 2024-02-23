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
 * @description 代码生成详细配置
 * @author Wujun
 * @date 2023-10-09
 */
@Data
@TableName("sys_code_generate_config")
public class SysCodeGenerateConfigEntity  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    @TableId(value = "id" ,type = IdType.AUTO )
    private String id;

    /**
    * 代码生成主表ID
    */
    @TableField(value = "code_gen_id" )
    private String codeGenId;

    /**
    * 数据库字段名
    */
    @TableField(value = "column_name" )
    private String columnName;

    /**
    * java类字段名
    */
    @TableField(value = "java_name" )
    private String javaName;

    /**
    * 物理类型
    */
    @TableField(value = "data_type" )
    private String dataType;

    /**
    * 字段描述
    */
    @TableField(value = "column_comment" )
    private String columnComment;

    /**
    * java类型
    */
    @TableField(value = "java_type" )
    private String javaType;

    /**
    * 作用类型（字典）
    */
    @TableField(value = "effect_type" )
    private String effectType;

    /**
    * 字典code
    */
    @TableField(value = "dict_type_code" )
    private String dictTypeCode;

    /**
    * 列表展示
    */
    @TableField(value = "whether_table" )
    private String whetherTable;

    /**
    * 增改
    */
    @TableField(value = "whether_add_update" )
    private String whetherAddUpdate;

    /**
    * 列表是否缩进（字典）
    */
    @TableField(value = "whether_retract" )
    private String whetherRetract;

    /**
    * 是否必填（字典）
    */
    @TableField(value = "whether_required" )
    private String whetherRequired;

    /**
    * 是否是查询条件
    */
    @TableField(value = "query_whether" )
    private String queryWhether;

    /**
    * 查询方式
    */
    @TableField(value = "query_type" )
    private String queryType;

    /**
    * 主键
    */
    @TableField(value = "column_key" )
    private String columnKey;

    /**
    * 主外键名称
    */
    @TableField(value = "column_key_name" )
    private String columnKeyName;

    /**
    * 是否是通用字段
    */
    @TableField(value = "whether_common" )
    private String whetherCommon;

    /**
    * 创建时间
    */
    @TableField(value = "create_time" )
    private Date createTime;

    /**
    * 创建人
    */
    @TableField(value = "create_user" )
    private String createUser;

    /**
    * 修改时间
    */
    @TableField(value = "update_time" )
    private Date updateTime;

    /**
    * 修改人
    */
    @TableField(value = "update_user" )
    private String updateUser;

    public SysCodeGenerateConfigEntity() {}
}
