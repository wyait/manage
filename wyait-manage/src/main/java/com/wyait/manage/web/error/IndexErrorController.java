package com.wyait.manage.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
/**
 * 
 * @项目名称：wyait-manage
 * @类名称：IndexErrorController
 * @类描述：手动error异常
 * @创建人：wyait
 * @创建时间：2018年6月6日 下午5:29:27 
 * @version：
 */
@Controller
@RequestMapping("/error")
public class IndexErrorController {
	/**
	 * 
	 * @描述：普通请求异常
	 * @创建人：wyait
	 * @创建时间：2018年5月24日 下午5:30:50
	 */
	@RequestMapping("getError")
	public void toError(){
		System.out.println(1/0);
	}
	/**
	 * 
	 * @描述：异步异常
	 * @创建人：wyait
	 * @创建时间：2018年5月24日 下午5:30:39
	 */
	@RequestMapping("ajaxError")
	@ResponseBody
	public String ajaxError(){
		System.out.println(1/0);
		return "异步请求成功！";
	}
}
