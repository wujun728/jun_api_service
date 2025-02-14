package com.websql.model;

import cn.hutool.http.HttpStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName : AjaxResult
 * @Description : 返回前端实体
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 15:22
 */
@Accessors(chain = true)
@Data
public class AjaxResult implements Serializable {


    private Integer code;

    private String msg;

    private Object data;

    private String detailMsg;


    public static AjaxResult success(String msg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(HttpStatus.HTTP_OK).setMsg(msg);
        return ajaxResult;
    }

    public static AjaxResult success() {
        return success("操作成功!");
    }

    public static AjaxResult success(Object data) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(HttpStatus.HTTP_OK).setMsg("操作成功!").setData(data);
        return ajaxResult;
    }


    public static AjaxResult error(String msg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg(msg);
        return ajaxResult;
    }

    public static AjaxResult error(String msg, String detailMsg) {
        AjaxResult ajaxResult = new AjaxResult();
        ajaxResult.setCode(HttpStatus.HTTP_INTERNAL_ERROR).setMsg(msg).setDetailMsg(detailMsg);
        return ajaxResult;
    }

    public static AjaxResult error() {
        return error("操作失败!");
    }

}


