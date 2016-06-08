package net.vinote.smartboot.service.api.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;

import net.vinote.smartboot.service.api.ApiAuthBean;
import net.vinote.smartboot.service.api.ApiCodeEnum;
import net.vinote.smartboot.service.api.RestApiHandler;
import net.vinote.smartboot.service.api.RestApiResult;
import net.vinote.smartboot.service.api.RestApiService;
import net.vinote.smartboot.service.api.VersionEnum;
import net.vinote.smartboot.service.util.AbstractService;
import net.vinote.smartboot.service.util.AssertUtils;
import net.vinote.smartboot.service.util.ServiceCallback;
import net.vinote.smartboot.service.util.permission.Permission;
import net.vinote.smartboot.service.util.permission.PermissionName;
import net.vinote.smartboot.service.util.permission.PermissionRelation;
import net.vinote.sosa.core.json.JsonUtil;

/**
 * API服务实现类
 * 
 * @author Seer
 * @version RestApiServiceImpl.java, v 0.1 2016年2月10日 下午3:17:16 Seer Exp.
 */
public class RestApiServiceImpl extends AbstractService implements RestApiService {
	private static final Logger LOGGER = LogManager.getLogger(RestApiServiceImpl.class);
	/** API服务集合 */
	private Map<String, RestApiHandler> handlers;

	/** 各服务的权限集合 */
	private Map<String, Permission> permissions;

	/*
	 * (non-Javadoc)
	 * 
	 * @see DynApiService#executeBizLogic(java.lang. String, java.lang.String,
	 * java.util.Map)
	 */
	public RestApiResult<Object> execute(ApiAuthBean authBean, Map<String, String> requestMap) {
		final RestApiResult<Object> result = new RestApiResult<Object>();
		operateTemplate.operateWithoutTransaction(result, new ServiceCallback() {
			RestApiHandler handler;
			VersionEnum version;

			/*
			 * (non-Javadoc)
			 * 
			 * @see ServiceCallback#doCheck()
			 */
			@Override
			public void doCheck() {
				AssertUtils.isNotNull(authBean, "鉴权参数异常");
				AssertUtils.isNotBlank(authBean.getSrvname(), "服务名称未指定");

				// 版本校验
				if (StringUtils.isBlank(authBean.getApiVersion())) {
					version = VersionEnum.CURRENT_VERSION;
				} else {
					version = VersionEnum.getVersion(authBean.getApiVersion());
				}
				AssertUtils.isNotNull(version, "服务器不支持版本号" + authBean.getApiVersion() + ",请升级至最新版");
				result.setVersion(version.getVersion());

				// 识别处理器
				handler = findApiBizHandler(authBean.getSrvname(), version);
				AssertUtils.isNotNull(handler, "无法处理该服务");

				// 校验本次服务是否具备权限
				// if (!CollectionUtils.isEmpty(permissions)) {
				// // TODO 读取当前用户的权限集合
				// UserComponent userComp =
				// serviceContext.getService(ServiceEnum.USER_SERVICE);
				// String[] userPerms =
				// userComp.queryUserPermissions(authBean.getUserId());
				// AssertUtils.isTrue(PermissionUtil.hasPermission(userPerms,
				// permissions.get(generateServerKey(authBean.getSrvname(),
				// version))), "无权进行本次操作");
				// }
			}

			@Override
			public void doOperate() {
				// 是否进行事务控制
				if (handler.needTransaction(authBean, requestMap)) {
					operateTemplate.operateWithTransaction(result, new ServiceCallback() {

						@Override
						public void doOperate() {
							Object execResult = handler.execute(authBean, requestMap);
							if (LOGGER.isDebugEnabled()) {
								LOGGER.debug(execResult);
							}
							result.setData(JsonUtil.getJsonObject(execResult));
						}
					});
					AssertUtils.isTrue(result.isSuccess(), result.getMessage());
				} else {
					Object execResult = handler.execute(authBean, requestMap);
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug(execResult);
					}
					result.setData(JsonUtil.getJsonObject(execResult));
				}

			}
		});
		if (StringUtils.isNotBlank(result.getMessage()) && result.getCode() == ApiCodeEnum.SUCCESS.getCode()) {
			result.setCode(ApiCodeEnum.FAIL.getCode());
		}
		return result;
	}

	/**
	 * 根据服务名和版本寻找服务，如果服务实例找不到的话，就自动降级寻找. 这样可以保证混合版本客户端的运行，而不是仅仅只能用一个版本的API。
	 * 使得对老版本移动端的代码增加新功能的时候更加方便。 version的格式1.0.0
	 * 在配置文件中，biz处理器的key是:srvname-version
	 * 
	 * @param srvName
	 * @param version
	 * @return
	 */
	private RestApiHandler findApiBizHandler(String srvName, VersionEnum version) {
		if (version == null || CollectionUtils.isEmpty(handlers)) {
			return null;
		}
		RestApiHandler handler = handlers.get(generateServerKey(srvName, version));
		if (handler != null) {
			return handler;
		}
		return findApiBizHandler(srvName, version.getParent());
	}

	private String generateServerKey(String srvName, VersionEnum version) {
		return srvName + "-" + version.getVersion();
	}

	/**
	 * Setter method for property <tt>handlers</tt>.
	 *
	 * @param handlers
	 *            value to be assigned to property handlers
	 */
	public void setHandlers(Map<String, RestApiHandler> handlers) {
		this.handlers = handlers;
	}

	/**
	 * Setter method for property <tt>permissions</tt>. key:对应handlers的key
	 * value格式为: 权限码1,权限码2|权限关系
	 * 
	 * @param permissions
	 *            value to be assigned to property permissions
	 */
	public void setPermissions(Map<String, String> permissions) {
		if (CollectionUtils.isEmpty(permissions)) {
			return;
		}
		this.permissions = new HashMap<String, Permission>(permissions.size());
		for (Entry<String, String> entry : permissions.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			Permission permission = generatePermission(value);
			if (permission == null) {
				continue;
			}
			this.permissions.put(key, permission);
		}
	}

	/**
	 * 解析权限配置: <br/>
	 * code1,code2|OR<br/>
	 * code1,code2|AND<br/>
	 * code1,code2|NOT
	 * 
	 * @param config
	 * @return
	 */
	private Permission generatePermission(String config) {
		String[] strs = StringUtils.split(config, '|');
		if (ArrayUtils.isEmpty(strs)) {
			return null;
		}
		String[] perNames = StringUtils.split(strs[0], ',');
		if (ArrayUtils.isEmpty(perNames)) {
			return null;
		}
		PermissionName[] permNames = new PermissionName[perNames.length];
		for (int i = 0; i < permNames.length; i++) {
			permNames[i] = new PermissionName(perNames[i], null);
		}
		PermissionRelation relation = null;
		if (strs.length > 1) {
			relation = PermissionRelation.getRelation(strs[1]);
		}
		if (relation == null) {
			relation = PermissionRelation.OR;
		}
		Permission permission = new Permission();
		permission.setRelation(relation);
		permission.setPermissions(permNames);
		return permission;
	}

}
