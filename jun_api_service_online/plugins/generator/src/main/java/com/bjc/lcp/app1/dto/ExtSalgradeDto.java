package com.bjc.lcp.app1.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import com.jun.plugin.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * @description 
 * @author Wujun
 * @date 2024-01-25
 */
@Data
@ApiModel("")
public class ExtSalgradeDto  extends BaseEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @ApiModelProperty("") 
    private Integer grade;

    /**
    * 
    */
    @ApiModelProperty("") 
    private String losal;

    /**
    * 
    */
    @ApiModelProperty("") 
    private String hisal;

    /**
    * 
    */
    @ApiModelProperty("") 
    private Long id;

    public ExtSalgradeDto() {}
}
