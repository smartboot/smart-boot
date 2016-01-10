package net.vinote.smartboot.service.util.permission;

/**
 * 权限实体类
 * 
 * @author 三刀
 * @version PermissionName.java, v 0.1 2015年11月4日 下午4:26:13 Seer Exp.
 */
public class PermissionName {
	/** 权限code值 */
	private String name;

	/** 权限描述 */
	private String describe;

	public PermissionName(String code, String describe) {
		this.name = code;
		this.describe = describe;
	}

	/**
	 * Getter method for property <tt>name</tt>.
	 *
	 * @return property value of name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter method for property <tt>name</tt>.
	 *
	 * @param name
	 *            value to be assigned to property name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Getter method for property <tt>describe</tt>.
	 *
	 * @return property value of describe
	 */
	public String getDescribe() {
		return describe;
	}

	/**
	 * Setter method for property <tt>describe</tt>.
	 *
	 * @param describe
	 *            value to be assigned to property describe
	 */
	public void setDescribe(String describe) {
		this.describe = describe;
	}

}
