package net.vinote.smartboot.service.util;

import org.apache.logging.log4j.Level;

/**
 * dbapi统一异常类
 * 
 * @author 三刀
 * @version DbApiException.java, v 0.1 2015年11月4日 下午3:26:56 Seer Exp.
 */
public class DbApiException extends RuntimeException {
	/** */
	private static final long serialVersionUID = 1L;
	private int code = 1;
	private Level level;

	public DbApiException(int code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public DbApiException(String message) {
		super(message);
	}

	public DbApiException(Level level, String message) {
		super(message);
		this.level = level;
	}

	public DbApiException(Throwable cause) {
		super(cause);
	}

	public DbApiException(int code, String message) {
		super(message);
		this.code = code;
	}

	/**
	 * Getter method for property <tt>code</tt>.
	 *
	 * @return property value of code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * Getter method for property <tt>level</tt>.
	 *
	 * @return property value of level
	 */
	public final Level getLevel() {
		return level;
	}

}
