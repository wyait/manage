package com.wyait.manage.utils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 
 * @项目名称：wyait-manager
 * @类名称：ShiroFilterUtils
 * @类描述：shiro工具类
 * @创建人：wyait
 * @创建时间：2018年4月24日 下午5:12:04 
 * @version：
 */
public class ShiroFilterUtils {
	private static final Logger logger = LoggerFactory
			.getLogger(ShiroFilterUtils.class);
    /**
     * 
     * @描述：判断请求是否是ajax
     * @创建人：wyait
     * @创建时间：2018年4月24日 下午5:00:22
     * @param request
     * @return
     */
    public static boolean isAjax(ServletRequest request){
    	String header = ((HttpServletRequest) request).getHeader("X-Requested-With");
    	if("XMLHttpRequest".equalsIgnoreCase(header)){
    		logger.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,为Ajax请求");
    		return Boolean.TRUE;
    	}
    	logger.debug("shiro工具类【wyait-manager-->ShiroFilterUtils.isAjax】当前请求,非Ajax请求");
    	return Boolean.FALSE;
    }
}
