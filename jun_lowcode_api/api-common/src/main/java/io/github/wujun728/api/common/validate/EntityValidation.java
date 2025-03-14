package io.github.wujun728.api.common.validate;

import io.github.wujun728.api.common.exception.BusinessException;

/**
 * 实体校验接口
 *
 * @version 1.0
 * @date 2024-05-06
 */
public interface EntityValidation<T> {

    /**
     * 校验
     * @param t
     * @param validationType
     * @throws BusinessException
     */
    public void validate(T t, ValidationType validationType) throws BusinessException;

}
