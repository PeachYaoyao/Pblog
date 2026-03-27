package top.peachyao.exception;


/**
 * @Description: 404异常
 * @Author: PeachYao
 * @Date: 2026-03-26
 */

public class NotFoundException extends RuntimeException {
	public NotFoundException() {
	}

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
