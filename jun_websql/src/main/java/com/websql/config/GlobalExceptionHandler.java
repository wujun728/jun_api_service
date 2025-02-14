package com.websql.config;

import cn.dev33.satoken.exception.ApiDisabledException;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.stp.StpUtil;
import com.websql.model.AjaxResult;
import com.websql.util.StpUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName : GlobalExceptionHandler
 * @Description : 全局异常处理
 * @Author rabbit boy_0214@sina.com
 * @Date: 2023/1/28 23:42
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(NotLoginException.class)
    public Object handlerException(HttpServletRequest request, NotLoginException e) {
        return hintErrorMsg(request, e, "您还未登录!");
    }

    @ExceptionHandler(NotPermissionException.class)
    public Object handlerException(HttpServletRequest request, NotPermissionException e) {
        String message = "您没有[" + e.getPermission() + "]权限不允许访问此功能，请联系管理员开通!";
        if (isAjax(request)) {
            return AjaxResult.error(message, e.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView("main");
            modelAndView.addObject("errorMsg", message);
            return modelAndView;
        }
    }

    @ExceptionHandler(NotRoleException.class)
    public Object handlerException(HttpServletRequest request, NotRoleException e) {
        boolean isDemo = StpUtil.hasRole("demo-admin");
        String message = isDemo ? "抱歉,演示角色不允许执行此操作!" : "您没有[" + e.getRole() + "]角色不允许访问此功能，请联系管理员开通!";
        if (isAjax(request)) {
            return AjaxResult.error(message, e.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView("main");
            modelAndView.addObject("errorMsg", message);
            return modelAndView;
        }
    }

    @ExceptionHandler(ApiDisabledException.class)
    @ResponseBody
    public AjaxResult handlerException(HttpServletRequest request, ApiDisabledException e) {
        return AjaxResult.error(e.getMessage(), "请联系管理员处理!");
    }


    @ExceptionHandler(Exception.class)
    public Object handlerException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        return hintErrorMsg(request, e, "未知异常!");
    }

    public Object hintErrorMsg(HttpServletRequest request, Exception e, String msg) {
        if (isAjax(request)) {
            return AjaxResult.error(msg, e.getMessage());
        } else {
            ModelAndView modelAndView = new ModelAndView("index");
            if (!StpUtils.getCurrentLogin()) {
                modelAndView.setViewName("login");
            }
            return modelAndView;
        }
    }

    public static boolean isAjax(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null && "XMLHttpRequest".equals(request.getHeader("X-Requested-With").toString()));
    }

}
