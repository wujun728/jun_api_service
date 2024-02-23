package com.bjc.lcp.app1.entity;

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
 * @description 
 * @author Wujun
 * @date 2024-01-25
 */
@Data
@TableName("ext_salgrade")
public class ExtSalgradeEntity  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @TableField(value = "grade" )
    private Integer grade;

    /**
    * 
    */
    @TableField(value = "losal" )
    private String losal;

    /**
    * 
    */
    @TableField(value = "hisal" )
    private String hisal;

    /**
    * 
    */
    @TableId(value = "id" ,type = IdType.AUTO )
    private Long id;

    public ExtSalgradeEntity() {}
}
