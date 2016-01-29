package net.vinote.smartboot.shared.converter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.sf.cglib.beans.BeanCopier;
import net.sf.cglib.core.Converter;

/**
 * 通用转换器,要转换的对象间的变量名称要一致
 * 
 * @author Seer
 * @version CommonConverter.java, v 0.1 2015年11月18日 下午1:56:53 Seer Exp.
 */
public class CommonConverter {
	/** logger日志 */
	private static final Logger LOGGER = LogManager.getLogger(CommonConverter.class);

	/** 通用转换器缓存 */
	private static final Map<String, BeanCopier> beanCopierMap = new HashMap<String, BeanCopier>();

	/** Cglib转换器 */
	private static final Converter converter = new CglibConverter();

	/**
	 * 单个对象 source --> target
	 * 
	 * @param <T>
	 * @param <E>
	 * @param source
	 * @param target
	 */
	public static <T, E> void convert(T source, E target) {
		// 参数校验
		if (source == null || target == null) {
			return;
		}

		String key = source.getClass() + " " + target.getClass();

		BeanCopier beanCopier;
		if (beanCopierMap.get(key) == null) {
			beanCopier = BeanCopier.create(source.getClass(), target.getClass(), true);
			beanCopierMap.put(key, beanCopier);
		} else {
			beanCopier = beanCopierMap.get(key);
		}

		beanCopier.copy(source, target, converter);
	}

	/**
	 * 数组对象转换
	 * 
	 * @param <T>
	 * @param <E>
	 * @param targetClass
	 * @param sources
	 * @param targets
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T, E extends Object> void convertList(Class targetClass, Collection<T> sources,
		Collection<E> targets) {

		// 参数校验
		if (targetClass == null || CollectionUtils.isEmpty(sources) || targets == null) {
			return;
		}

		try {
			for (T source : sources) {
				E target = (E) targetClass.newInstance();

				convert(source, target);
				targets.add(target);
			}
		} catch (Exception e) {
			LOGGER.error("转换对象异常", e);
		}
	}
}
