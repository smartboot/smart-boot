package net.vinote.smartboot.restful.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import net.vinote.smartboot.restful.BaseController;
import net.vinote.smartboot.service.api.ApiAuthBean;
import net.vinote.smartboot.service.api.ApiCodeEnum;
import net.vinote.smartboot.service.api.RestApiResult;
import net.vinote.smartboot.service.api.RestApiService;

/**
 * 动态API适配接口(Facade), 这里的代码任何时候都不会改变.
 * 
 * @author Seer
 * @version DynApiController.java, v 0.1 2016年2月10日 下午2:25:45 Seer Exp.
 */
@RestController
@RequestMapping("/api")
public class RestApiController extends BaseController {

	@Autowired
	private RestApiService restApiService;

	/**
	 * 执行api服务
	 * 
	 * @param srvname
	 * @param request
	 * @return
	 */
	@RequestMapping(
		value = "/{srvname}", method = { RequestMethod.POST, RequestMethod.GET },
		produces = { "application/json; charset=UTF-8" })
	public ModelAndView api(@PathVariable("srvname") String srvname, HttpServletRequest request) {
		ApiAuthBean authBean = new ApiAuthBean();
		authBean.setApiVersion(request.getHeader("ver"));
		authBean.setSrvname(srvname);
		return executeAndReturnJSON(request, authBean);
	}

	/**
	 * 执行API服务下的某个接口
	 * 
	 * @param srvname
	 * @param actName
	 * @param request
	 * @return
	 */
	@RequestMapping(
		value = "/{srvname}/{actname}", method = { RequestMethod.POST, RequestMethod.GET },
		produces = { "application/json; charset=UTF-8" })
	public ModelAndView apiAct(@PathVariable("srvname") String srvname, @PathVariable("actname") String actName,
		HttpServletRequest request) {
		ApiAuthBean authBean = new ApiAuthBean();
		authBean.setApiVersion(request.getHeader("ver"));
		authBean.setSrvname(srvname);
		authBean.setActName(actName);
		return executeAndReturnJSON(request, authBean);
	}

	private ModelAndView executeAndReturnJSON(HttpServletRequest request, ApiAuthBean authBean) {
		Enumeration<String> names = request.getParameterNames();
		Map<String, String> requestMap = new HashMap<String, String>();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			requestMap.put(key, request.getParameter(key));
		}
		RestApiResult<Object> result = restApiService.execute(authBean, requestMap);
		ModelAndView mv = new ModelAndView();
		mv.addObject("code", result.getCode());
		mv.addObject("version", result.getVersion());
		if (result.getCode() == ApiCodeEnum.SUCCESS.getCode()) {
			mv.addObject("data", result.getData());
		} else {
			mv.addObject("message", result.getMessage());
		}
		return mv;
	}
}
