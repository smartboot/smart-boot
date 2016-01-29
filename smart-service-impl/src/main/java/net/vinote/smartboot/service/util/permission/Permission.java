package net.vinote.smartboot.service.util.permission;

/**
 * 权限关系模型
 * @author Seer
 * @version Permission.java, v 0.1 2015年11月4日 下午4:33:25 Seer Exp. 
 */
public class Permission {
	
	/**权限集合 */
	private PermissionName[] permissions;

	/**权限关系 */
	private PermissionRelation relation= PermissionRelation.OR;

	/**
	 * Getter method for property <tt>permissions</tt>.
	 *
	 * @return property value of permissions
	 */
	public PermissionName[] getPermissions() {
		return permissions;
	}

	/**
	 * Setter method for property <tt>permissions</tt>.
	 *
	 * @param permissions value to be assigned to property permissions
	 */
	public void setPermissions(PermissionName[] permissions) {
		this.permissions = permissions;
	}

	/**
	 * Getter method for property <tt>relation</tt>.
	 *
	 * @return property value of relation
	 */
	public PermissionRelation getRelation() {
		return relation;
	}

	/**
	 * Setter method for property <tt>relation</tt>.
	 *
	 * @param relation value to be assigned to property relation
	 */
	public void setRelation(PermissionRelation relation) {
		this.relation = relation;
	}
	
}