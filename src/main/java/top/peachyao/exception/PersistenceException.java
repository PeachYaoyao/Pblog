package top.peachyao.exception;


/**
 * @Description: 持久化异常
 * @Author: PeachYao
 * @Date: 2026-03-26
 */

public class PersistenceException extends RuntimeException {
	public PersistenceException() {
	}

	public PersistenceException(String message) {
		super(message);
	}

	public PersistenceException(String message, Throwable cause) {
		super(message, cause);
	}
}
