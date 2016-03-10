package net.vinote.smartboot.restful;

import org.springframework.ui.ModelMap;

import net.vinote.smartboot.service.facade.result.ServiceResult;

/**
 * smartboot Controller抽象类
 * 
 * @author Seer
 * @version BaseController.java, v 0.1 2015年11月5日 上午11:47:40 Seer Exp.
 */
public abstract class BaseController {
	protected static final String SUCCESS_FLAG = "success";

	protected void writeSuccess(ModelMap modelMap, Object data) {
		modelMap.put(SUCCESS_FLAG, true);
		modelMap.put("data", data);
	}

	protected void writeError(ModelMap modelMap, String message) {
		modelMap.put(SUCCESS_FLAG, false);
		modelMap.put("msg", message);
	}

	protected <T> void writeData(ModelMap modelMap, ServiceResult<T> result) {
		if (result.isSuccess())
			writeSuccess(modelMap, result.getData());
		else
			writeError(modelMap, result.getMessage());
	}
}