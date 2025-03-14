package io.github.wujun728.api.common.jdbc.util;

/**
 * 自定义异常
 * 
 * @author 兰平
 * @version 1.0
 * @date 2017-08-22
 */
public class DaoException extends Exception {

	public DaoException() {
		super();
	}

	public DaoException(Exception exception, String message) {
		super(message,exception);
	}

	public DaoException(String message) {
		super(message);
	}
}