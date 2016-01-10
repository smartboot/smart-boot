package net.vinote.smartboot.service.util.permission;

import org.apache.commons.lang.StringUtils;

/**
 * 权限关系
 * 
 * @author 三刀
 * @version PermissionRelation.java, v 0.1 2015年11月4日 下午4:29:19 Seer Exp.
 */
public enum PermissionRelation {
	AND("AND", "与"), OR("OR", "或"), NOT("NOT", "非");
	private PermissionRelation(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private String code;
	private String desc;

	/**
	 * Getter method for property <tt>code</tt>.
	 *
	 * @return property value of code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Getter method for property <tt>desc</tt>.
	 *
	 * @return property value of desc
	 */
	public String getDesc() {
		return desc;
	}

	public static PermissionRelation getRelation(String code) {
		if (StringUtils.isBlank(code)) {
			return null;
		}
		for (PermissionRelation realtion : values()) {
			if (StringUtils.equals(realtion.code, code)) {
				return realtion;
			}
		}
		return null;
	}
}