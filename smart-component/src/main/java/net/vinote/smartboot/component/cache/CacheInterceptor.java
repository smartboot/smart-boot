package net.vinote.smartboot.component.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;

import net.vinote.smartboot.integration.cache.CacheClient;

/**
 * 缓存拦截器,通过配置化拦截设置于接口处的注解
 * 
 * @author Seer
 * @version v0.1 2015年11月06日 下午1:46 Seer Exp.
 */
//@Aspect
//@Component
public class CacheInterceptor {

	/** key间隔符 */
	public static final String KEY_SEPERATOR = "_";

	/**
	 * logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(CacheInterceptor.class);

	/**
	 * 缓存管理器
	 */
	@Autowired
	private CacheClient cacheClient;

	@Pointcut("@annotation(CacheEnable)")
	public void pointcut() {
		System.err.println("aa");
	}

	/**
	 * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
	 */
	@Around("pointcut()")
	public Object invoke(ProceedingJoinPoint pjp) throws Throwable {
		// 获取方法上的注解
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		Cached cachedAnn = method.getAnnotation(Cached.class);
		if (cachedAnn == null) {
			return pjp.proceed();
		}

		String key = null;
		try {
			key = getCacheKey(pjp, cachedAnn);
		} catch (Exception ex) {
			LOGGER.warn("组装缓存Key异常", ex);
			return pjp.proceed();
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("组装缓存查询KEY完成,method[" + method.getDeclaringClass().getName() + "." + method.getName()
				+ "],key[" + key + "]");
		}

		if (cachedAnn.operateType() != OperationEnum.READ) {
			// 不管最后是成功还是失败， 都优先清理缓存
			Long effectNums = removeFromCache(key);
			if (LOGGER.isInfoEnabled()) {
				LOGGER.info("清理缓存,method[" + method.getDeclaringClass().getName() + "." + method.getName() + "],key["
					+ key + "],结果影响记录数[" + effectNums + "]");
			}

			return pjp.proceed();
		}

		// 读取
		Object value = readCachedObj(key);
		if (value != null) {
			// 如果从缓存获取到值， 则直接返回此值
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("缓存命中,method[" + method.getDeclaringClass().getName() + "." + method.getName() + "],key["
					+ key + "],value[" + value + "]");
			}
			return value;
		}
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("缓存miss,method[" + method.getDeclaringClass().getName() + "." + method.getName() + "],key["
				+ key + "]");
		}
		value = pjp.proceed();
		if (value != null) {
			writeCache(key, value, cachedAnn.expire());
		}
		return value;
	}

	/**
	 * 组装cache key
	 *
	 * @param methodSignature
	 * @param cachedAnn
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String getCacheKey(ProceedingJoinPoint pjp, Cached cachedAnn) {
		Method method = ((MethodSignature) pjp.getSignature()).getMethod();

		StringBuilder keyBuffer = new StringBuilder(cachedAnn.prefix().getPrefix());

		if (!StringUtils.isBlank(cachedAnn.staticKey())) {
			keyBuffer.append(KEY_SEPERATOR).append(cachedAnn.staticKey());
		}

		Object[] args = pjp.getArgs();
		Class<?>[] parameterTypes = method.getParameterTypes();
		Annotation[][] paramAnns = method.getParameterAnnotations();
		if (paramAnns != null && paramAnns.length > 0) {
			for (int i = 0; i < paramAnns.length; i++) {
				if (paramAnns[i] == null || paramAnns[i].length == 0) {
					continue;
				}
				for (int j = 0; j < paramAnns[i].length; j++) {
					Annotation ann = paramAnns[i][j];
					if (!(ann instanceof CacheKey)) {
						continue;
					}

					Object param = args[i];
					if (param == null) {
						keyBuffer.append(KEY_SEPERATOR).append(StringUtils.EMPTY);
						continue;
					}

					Class<?> parameterType = parameterTypes[i];
					if (BeanUtils.isSimpleProperty(parameterType)) {
						keyBuffer.append(KEY_SEPERATOR).append(param);
						continue;
					}

					CacheKey keyFlag = (CacheKey) ann;
					String[] fieldNames = keyFlag.fields();
					if (fieldNames == null || fieldNames.length == 0) {
						keyBuffer.append(KEY_SEPERATOR)
							.append(ToStringBuilder.reflectionToString(param, ToStringStyle.SHORT_PREFIX_STYLE));
						continue;
					}

					if (Map.class.isAssignableFrom(parameterType)) {
						for (String fieldName : fieldNames) {
							Map<Object, Object> map = (Map<Object, Object>) param;
							keyBuffer.append(KEY_SEPERATOR).append(map.get(fieldName));
						}
					} else {
						BeanWrapper wrapper = new BeanWrapperImpl(param);
						for (String fieldName : fieldNames) {
							if (!wrapper.isReadableProperty(fieldName)) {
								keyBuffer.append(KEY_SEPERATOR).append(StringUtils.EMPTY);
							} else {
								Object propertyValue = wrapper.getPropertyValue(fieldName);
								keyBuffer.append(KEY_SEPERATOR)
									.append(propertyValue == null ? StringUtils.EMPTY : propertyValue);
							}
						}
					}
				}
			}
		}

		return keyBuffer.toString();
	}

	/**
	 * 写入cache
	 *
	 * @param key
	 * @param value
	 */
	private void writeCache(String key, Object value, int expire) {
		try {
			cacheClient.putObject(key, value, expire);
		} catch (Exception ex) {
			LOGGER.warn("系统异常:更新缓存对象失败,cacheKey=" + key, ex);
		}
	}

	/**
	 * 从cache中读取
	 *
	 * @param key
	 * @return
	 */
	private Object readCachedObj(String key) {
		Object value = null;
		try {
			value = cacheClient.getObject(key);
		} catch (Exception ex) {
			LOGGER.warn("系统异常:获取缓存对象失败,cacheKey=" + key, ex);
		}
		return value;
	}

	/**
	 * 清除cache
	 *
	 * @param cacheKey
	 * @return
	 */
	private Long removeFromCache(String cacheKey) {
		Long result = 0l;
		try {
			result = cacheClient.remove(cacheKey);
		} catch (Exception e) {
			LOGGER.warn("系统异常：删除缓存对象失败,cacheKey=" + cacheKey, e);
		}
		return result;
	}

	/**
	 * Setter method for property <tt>cacheClient</tt>.
	 *
	 * @param cacheClient value to be assigned to property cacheClient
	 */
	public final void setCacheClient(CacheClient cacheClient) {
		this.cacheClient = cacheClient;
	}
	
	
}
