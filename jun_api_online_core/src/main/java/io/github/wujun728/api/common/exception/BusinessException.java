package io.github.wujun728.api.common.exception;

import io.github.wujun728.api.common.enums.EnumCode;
import io.github.wujun728.api.common.utils.MessageUtils;

/**
 * 业务异常类
 * @version 1.0
 * @date 2024-05-06
 */
public class BusinessException extends RuntimeException {

	private int code;
	public BusinessException(EnumCode enumCode, String message) {
		super(message);
		this.code = enumCode.getCode();
	}

	public BusinessException(EnumCode enumCode) {
		super(MessageUtils.get(enumCode));
		this.code = enumCode.getCode();
	}

	public BusinessException(EnumCode enumCode, Throwable e) {
		super(MessageUtils.get(enumCode),e);
		this.code = enumCode.getCode();
	}

	public BusinessException(EnumCode enumCode, String message, Throwable e) {
		super(message,e);
		this.code = enumCode.getCode();
	}

	public static BusinessException paramsEmpty(String param) {
		BusinessException exception = new BusinessException(EnumCode.PARAMS_NOT_EMPTY,
				param + MessageUtils.get(EnumCode.PARAMS_NOT_EMPTY));
		return exception;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
