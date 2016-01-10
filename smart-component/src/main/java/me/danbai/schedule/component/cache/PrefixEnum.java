package me.danbai.schedule.component.cache;

/**
 * 缓存前缀枚举
 *
 * @author 三刀
 * @version v0.1 2015年11月06日 下午1:46 Seer Exp.
 */
public enum PrefixEnum {

	/** 用户权限 */
	USER_PERMISSION("user", "permission"),

	/** 根据ID缓存的活动信息 */
	ACTIVITY_INFO_BY_ID("activity_info", "id");
	;
	/** 缓存前缀所属域 */
	private String domain;

	/** 缓存前缀值 */
	private String value;

	/**
	 * constructor
	 *
	 * @param domain
	 * @param value
	 */
	PrefixEnum(String domain, String value) {
		this.domain = domain;
		this.value = value;
	}

	/**
	 * getter
	 *
	 * @return
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * getter
	 *
	 * @return
	 */
	public String getValue() {
		return value;
	}

	/**
	 * 获得完整前缀
	 *
	 * @return
	 */
	public String getPrefix() {
		return domain + CacheInterceptor.KEY_SEPERATOR + value;
	}
}
