package com.bjc.lcp.app1.vo;

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
 * @description 
 * @author Wujun
 * @date 2024-01-25
 */
@Data
@ApiModel("")
public class ExtSalgradeVo  extends BaseEntity  implements Serializable {

    public interface Retrieve{}
    public interface Delete {}
    public interface Update {}
    public interface Create {}

    private static final long serialVersionUID = 1L;

    /**
    * 
    */
    @ApiModelProperty("") 
    @NotNull(message = "不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 10,message = "长度限制10位")
    private Integer grade;

    /**
    * 
    */
    @ApiModelProperty("") 
    @NotNull(message = "不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 11,message = "长度限制11位")
    private String losal;

    /**
    * 
    */
    @ApiModelProperty("") 
    @NotNull(message = "不能为空", groups = {Create.class,Update.class,Delete.class})
    @Size( max = 65535,message = "长度限制65535位")
    private String hisal;

    /**
    * 
    */
    @ApiModelProperty("") 
    
    
    private Long id;

    public ExtSalgradeVo() {}
}
