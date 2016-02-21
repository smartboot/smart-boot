package net.vinote.smartboot.service.api;

import net.vinote.smartboot.shared.ToString;

/**
 * API鉴权数据实体
 * 
 * @author Seer
 * @versio
 * n ApiAuthBean.java, v 0.1 2016年2月10日 下午2:20:43 Seer Exp.
 */
public class ApiAuthBean extends ToString {
	/** */
	private static final long serialVersionUID = -7176406155692157064L;
	/** 服务名称 */
	private String srvname;
	/** 执行事件 */
	private String actName;
	/** 版本号 */
	private String apiVersion;

	/**
	 * Getter method for property <tt>srvname</tt>.
	 *
	 * @return property value of srvname
	 */
	public final String getSrvname() {
		return srvname;
	}

	/**
	 * Setter method for property <tt>srvname</tt>.
	 *
	 * @param srvname
	 *            value to be assigned to property srvname
	 */
	public final void setSrvname(String srvname) {
		this.srvname = srvname;
	}

	/**
	 * Getter method for property <tt>actName</tt>.
	 *
	 * @return property value of actName
	 */
	public final String getActName() {
		return actName;
	}

	/**
	 * Setter method for property <tt>actName</tt>.
	 *
	 * @param actName
	 *            value to be assigned to property actName
	 */
	public final void setActName(String actName) {
		this.actName = actName;
	}

	/**
	 * Getter method for property <tt>apiVersion</tt>.
	 *
	 * @return property value of apiVersion
	 */
	public final String getApiVersion() {
		return apiVersion;
	}

	/**
	 * Setter method for property <tt>apiVersion</tt>.
	 *
	 * @param apiVersion
	 *            value to be assigned to property apiVersion
	 */
	public final void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

}
