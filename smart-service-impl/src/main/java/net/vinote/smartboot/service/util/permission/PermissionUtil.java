package net.vinote.smartboot.service.util.permission;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import net.vinote.smartboot.service.util.DbApiException;

public class PermissionUtil {

	/**
	 * 权限集合是否满足限定条件
	 * @param permissions 权限集合
	 * @param need 权限关系
	 * @return
	 */
	public static boolean hasPermission(String[] permissions, Permission need) {
		if(need==null||need.getRelation()==null||ArrayUtils.isEmpty(need.getPermissions())){
			return true;
		}
		switch (need.getRelation()) {
		case OR:
			for (PermissionName perm : need.getPermissions()) {
				for (String code : permissions) {
					if (StringUtils.equalsIgnoreCase(code, perm.getName())) {
						return true;
					}
				}
			}
			return false;
		case AND:
			for (PermissionName perm : need.getPermissions()) {
				boolean has = false;
				for (String code : permissions) {
					if (StringUtils.equalsIgnoreCase(code, perm.getName())) {
						has = true;
						break;
					}
				}
				if (!has) {
					return false;
				}
			}
			return true;
		case NOT:
			for (String code : permissions) {
				for (PermissionName perm : need.getPermissions()) {
					if (StringUtils.equalsIgnoreCase(code, perm.getName())) {
						return false;
					}
				}
			}
			return true;
		default:
			throw new DbApiException("unsupport relation " + need.getRelation());
		}

	}

}