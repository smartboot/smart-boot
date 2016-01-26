package net.vinote.smartboot.service.util;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Level;
import org.springframework.util.CollectionUtils;

import net.vinote.smartboot.shared.SmartException;

/**
 * 断言工具类
 * 
 * @author 三刀
 * @version AssertUtils.java, v 0.1 2015年11月4日 下午3:22:59 Seer Exp.
 */
public class AssertUtils {
	/**
	 * <p>
	 * 若被检查对象为空白字符串,则抛异常
	 * </p>
	 *
	 * <pre>
	 * StringUtils.isBlank(null)      = true
	 * StringUtils.isBlank("")        = true
	 * StringUtils.isBlank(" ")       = true
	 * StringUtils.isBlank("bob")     = false
	 * StringUtils.isBlank("  bob  ") = false
	 * </pre>
	 *
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is null, empty or whitespace
	 * @since 2.0
	 */
	public static void isNotBlank(String str, String msg) {
		if (StringUtils.isBlank(str)) {
			throw new SmartException(msg);
		}
	}

	/**
	 * obj对象不为null，若obj为null则抛异常
	 * 
	 * @param obj
	 * @param message
	 *            异常信息
	 */
	public static void isNotNull(Object obj, String message) {
		if (obj == null) {
			throw new SmartException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@code IllegalArgumentException} if
	 * the test result is {@code false}.
	 * 
	 * <pre class="code">
	 * Assert.isTrue(i &gt; 0, "The value must be greater than zero");
	 * </pre>
	 * 
	 * @param expression
	 *            a boolean expression
	 * @param message
	 *            the exception message to use if the assertion fails
	 * @throws SmartException
	 *             if expression is {@code false}
	 */
	public static void isTrue(boolean flag, String message) {
		isTrue(flag, message, null);
	}

	public static void isTrue(boolean flag, String message, Level level) {
		if (!flag) {
			throw new SmartException(level, message);
		}
	}

	public static void isFalse(boolean flag, String message) {
		isFalse(flag, message, null);
	}

	public static void isFalse(boolean flag, String message, Level level) {
		if (flag) {
			throw new SmartException(level, message);
		}
	}

	public static <E> void notEmpty(Collection<E> collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new SmartException(message);
		}
	}
}
