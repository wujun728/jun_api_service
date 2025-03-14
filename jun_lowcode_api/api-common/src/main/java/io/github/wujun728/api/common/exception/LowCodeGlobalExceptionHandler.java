package io.github.wujun728.api.common.exception;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.jdbc.util.DaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 * 如果项目中也有 GlobalExceptionHandler 只会扫描一个 所以这个有问题 需要注释
 * @version 1.0
 * @date 2024-05-06
 */
@RestControllerAdvice(basePackages = {
        "io.github.wujun728.api.core.controller"
})
public class LowCodeGlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(LowCodeGlobalExceptionHandler.class);

    /**
     * 全局异常处理，异常返回统一格式
     *
     * @param e 异常对象
     * @return 消息结果
     */
    @ExceptionHandler(value = BusinessException.class)
    public ApiResult businessExceptionHandler(BusinessException e) {
        logger.error("GlobalExceptionHandler exceptionHandler error ", e);
        return ApiResult.failure(EnumCode.getEnumCode(e.getCode()), e.getMessage());
    }

    /**
     * 全局异常处理，异常返回统一格式
     *
     * @param e 异常对象
     * @return 消息结果
     */
    @ExceptionHandler(value = DaoException.class)
    public ApiResult daoExceptionHandler(DaoException e) {
        logger.error("GlobalExceptionHandler exceptionHandler error ", e);
        return ApiResult.failure(EnumCode.SERVER_EXCEPTION, e.getMessage());
    }
}
