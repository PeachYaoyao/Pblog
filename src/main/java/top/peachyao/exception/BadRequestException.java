package top.peachyao.exception;

/**
 * @Description: 非法请求异常
 * @Author: PeachYao
 * @Date: 2026-03-26
 */

public class BadRequestException extends RuntimeException {
	public BadRequestException() {
	}

	public BadRequestException(String message) {
		super(message);
	}

	public BadRequestException(String message, Throwable cause) {
		super(message, cause);
	}
}
