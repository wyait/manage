package com.wyait.manage.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

import com.wyait.manage.shiro.RetryLimitHashedCredentialsMatcher;
import com.wyait.manage.shiro.ShiroRealm;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.config
 * @类描述：
 * @创建人：wyait
 * @创建时间：2017-12-12 18:51
 * @version：V1.0
 */
@Configuration
@EnableTransactionManagement
public class ShiroConfig {
	private static final Logger logger = LoggerFactory
			.getLogger(ShiroConfig.class);

	/**
	 * ShiroFilterFactoryBean 处理拦截资源文件过滤器
	 *	</br>1,配置shiro安全管理器接口securityManage;
	 *	</br>2,shiro 连接约束配置filterChainDefinitions;
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(
			org.apache.shiro.mgt.SecurityManager securityManager) {
		// shiroFilterFactoryBean对象
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		logger.debug("-----------------Shiro拦截器工厂类注入开始");
		// 配置shiro安全管理器 SecurityManager
		shiroFilterFactoryBean.setSecurityManager(securityManager);

		// 指定要求登录时的链接
		shiroFilterFactoryBean.setLoginUrl("/toLogin");
		// 登录成功后要跳转的链接
		shiroFilterFactoryBean.setSuccessUrl("/home");
		// 未授权时跳转的界面;
		shiroFilterFactoryBean.setUnauthorizedUrl("/error");

		// filterChainDefinitions拦截器=map必须用：LinkedHashMap，因为它必须保证有序
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		// 配置退出过滤器,具体的退出代码Shiro已经实现
		filterChainDefinitionMap.put("/logout", "logout");
		// 配置记住我或认证通过可以访问的地址
		filterChainDefinitionMap.put("/user/userList", "user");
		filterChainDefinitionMap.put("/", "user");
		//
		// // 配置不会被拦截的链接 从上向下顺序判断
		filterChainDefinitionMap.put("/login", "anon");
		filterChainDefinitionMap.put("/css/*", "anon");
		filterChainDefinitionMap.put("/js/*", "anon");
		filterChainDefinitionMap.put("/js/*/*", "anon");
		filterChainDefinitionMap.put("/js/*/*/*", "anon");
		filterChainDefinitionMap.put("/images/*/**", "anon");
		filterChainDefinitionMap.put("/layui/*", "anon");
		filterChainDefinitionMap.put("/layui/*/**", "anon");
		filterChainDefinitionMap.put("/treegrid/*", "anon");
		filterChainDefinitionMap.put("/treegrid/*/*", "anon");
		filterChainDefinitionMap.put("/fragments/*", "anon");
		filterChainDefinitionMap.put("/layout", "anon");

		filterChainDefinitionMap.put("/user/sendMsg", "anon");
		filterChainDefinitionMap.put("/user/login", "anon");
		filterChainDefinitionMap.put("/home", "anon");
		// /*filterChainDefinitionMap.put("/page", "anon");
		// filterChainDefinitionMap.put("/channel/record", "anon");*/
		//
		// //add操作，该用户必须有【addOperation】权限
		// filterChainDefinitionMap.put("/user/setUser", "roles[superman]");
		filterChainDefinitionMap
				.put("/user/delUser", "authc,perms[usermanage]");
		//
		// // <!-- authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问【放行】-->
		filterChainDefinitionMap.put("/*", "authc");
		filterChainDefinitionMap.put("/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*", "authc");
		filterChainDefinitionMap.put("/*/*/*/**", "authc");

		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMap);
		logger.debug("-----------------Shiro拦截器工厂类注入成功");
		return shiroFilterFactoryBean;
	}

	/**
	 * shiro安全管理器设置realm认证和ehcache缓存管理
	 * @return
	 */
	@Bean
	public org.apache.shiro.mgt.SecurityManager securityManager() {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// 设置realm.
		securityManager.setRealm(shiroRealm());
		// //注入ehcache缓存管理器;
		securityManager.setCacheManager(ehCacheManager());
		// 注入Cookie记住我管理器
		securityManager.setRememberMeManager(rememberMeManager());
		return securityManager;
	}

	/**
	 * 身份认证realm; (账号密码校验；权限等)
	 *
	 * @return
	 */
	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		// 使用自定义的CredentialsMatcher进行密码校验和输错次数限制
		shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
		return shiroRealm;
	}

	/**
	 * ehcache缓存管理器；shiro整合ehcache：
	 * 通过安全管理器：securityManager
	 * 单例的cache防止热部署重启失败
	 * @return EhCacheManager
	 */
	@Bean
	public EhCacheManager ehCacheManager() {
		logger.debug("=====shiro整合ehcache缓存：ShiroConfiguration.getEhCacheManager()");
		EhCacheManager ehcache = new EhCacheManager();
		CacheManager cacheManager = CacheManager.getCacheManager("es");
		if (cacheManager == null) {
			try {

				cacheManager = CacheManager.create(ResourceUtils
						.getInputStreamForPath("classpath:config/ehcache.xml"));

			} catch (CacheException | IOException e) {
				e.printStackTrace();
			}
		}
		ehcache.setCacheManager(cacheManager);
		return ehcache;
	}

	/**
	 * 设置记住我cookie过期时间
	 * @return
	 */
	@Bean
	public SimpleCookie remeberMeCookie() {
		logger.debug("记住我，设置cookie过期时间！");
		// cookie名称;对应前端的checkbox的name = rememberMe
		SimpleCookie scookie = new SimpleCookie("rememberMe");
		// 记住我cookie生效时间30天 ,单位秒 [1小时]
		scookie.setMaxAge(3600);
		return scookie;
	}

	/**
	 * 配置cookie记住我管理器
	 * @return
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		logger.debug("配置cookie记住我管理器！");
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(remeberMeCookie());
		return cookieRememberMeManager;
	}

	/**
	 * 凭证匹配器 （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了
	 * 所以我们需要修改下doGetAuthenticationInfo中的代码,更改密码生成规则和校验的逻辑一致即可; ）
	 *
	 * @return
	 */
	@Bean
	public HashedCredentialsMatcher hashedCredentialsMatcher() {
		HashedCredentialsMatcher hashedCredentialsMatcher = new RetryLimitHashedCredentialsMatcher(
				ehCacheManager());
		// new HashedCredentialsMatcher();
		hashedCredentialsMatcher.setHashAlgorithmName("md5");// 散列算法:这里使用MD5算法;
		hashedCredentialsMatcher.setHashIterations(1);// 散列的次数，比如散列两次，相当于 //
														// md5(md5(""));
		return hashedCredentialsMatcher;
	}

	/**
	 * @描述：ShiroDialect，为了在thymeleaf里使用shiro的标签的bean 
	 * @创建人：wyait
	 * @创建时间：2017年12月21日 下午1:52:59
	 * @return
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * 
	 * @描述：开启Shiro的注解(如@RequiresRoles,@RequiresPermissions),需借助SpringAOP扫描使用Shiro注解的类,并在必要时进行安全逻辑验证
	 * 配置以下两个bean(DefaultAdvisorAutoProxyCreator和AuthorizationAttributeSourceAdvisor)即可实现此功能
	 * </br>Enable Shiro Annotations for Spring-configured beans. Only run after the lifecycleBeanProcessor(保证实现了Shiro内部lifecycle函数的bean执行) has run
	 * </br>不使用注解的话，可以注释掉这两个配置
	 * @创建人：wyait
	 * @创建时间：2018年5月21日 下午6:07:56
	 * @return
	 */
	@Bean
	public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		advisorAutoProxyCreator.setProxyTargetClass(true);
		return advisorAutoProxyCreator;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager());
		return authorizationAttributeSourceAdvisor;
	}

}
