package io.github.wujun728.web.server.config;

import io.github.wujun728.api.common.bean.ApiResult;
import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 全局异常处理
 * @version 1.0
 * @date 2023-10-12
 */
@RestControllerAdvice(basePackages = {
        "io.github.wujun728.web.server",
        "io.github.wujun728.api.core.controller"
})
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 全局异常处理，异常返回统一格式
     *
     * @param e 异常对象
     * @return 消息结果
     */
    @ExceptionHandler(value = Exception.class)
    public ApiResult exceptionHandler(Exception e) {
        logger.error("GlobalExceptionHandler exceptionHandler error ", e);
        ApiResult apiResult = null;
        if (e instanceof BindException) {
            List<ObjectError> allErrors = ((BindException) e).getAllErrors();
            if (!CollectionUtils.isEmpty(allErrors)) {
                String errorMessage = allErrors.get(0).getDefaultMessage();
                return ApiResult.failure(EnumCode.VALID_FAIL, errorMessage);
            }
        }else if (e instanceof MethodArgumentNotValidException) {
            List<ObjectError> allErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
            if (!CollectionUtils.isEmpty(allErrors)) {
                String errorMessage = allErrors.get(0).getDefaultMessage();
                return ApiResult.failure(EnumCode.VALID_FAIL, errorMessage);
            }
        }else if(e instanceof BusinessException){
            return ApiResult.failure(EnumCode.getEnumCode(((BusinessException) e).getCode()), e.getMessage());
        }
        apiResult = ApiResult.failure(EnumCode.SERVER_EXCEPTION);
        return apiResult;
    }
}
