package com.wyait.manage.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;  
import org.apache.shiro.authc.ExcessiveAttemptsException;  
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;  
import org.apache.shiro.cache.Cache;  
import org.apache.shiro.cache.CacheManager;  
  
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @项目名称：wyait-manage
 * @包名：com.wyait.manage.shiro
 * @类描述：shiro之密码输入次数限制6次，并锁定2分钟
 * @创建人：wyait
 * @创建时间：2018年1月23日17:23:10
 * @version：V1.0
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

    //集群中可能会导致出现验证多过5次的现象，因为AtomicInteger只能保证单节点并发
    //解决方案，利用ehcache、redis（记录错误次数）和mysql数据库（锁定）的方式处理：密码输错次数限制； 或两者结合使用
    private Cache<String, AtomicInteger> passwordRetryCache;  
  
    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        //读取ehcache中配置的登录限制锁定时间
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");  
    }

    /**
     * 在回调方法doCredentialsMatch(AuthenticationToken token,AuthenticationInfo info)中进行身份认证的密码匹配，
     * </br>这里我们引入了Ehcahe用于保存用户登录次数，如果登录失败retryCount变量则会一直累加，如果登录成功，那么这个count就会从缓存中移除，
     * </br>从而实现了如果登录次数超出指定的值就锁定。
     * @param token
     * @param info
     * @return
     */
    @Override  
    public boolean doCredentialsMatch(AuthenticationToken token,  
            AuthenticationInfo info) {
        //获取登录用户名
        String username = (String) token.getPrincipal();  
        //从ehcache中获取密码输错次数
        // retryCount
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if (retryCount == null) {
            //第一次
            retryCount = new AtomicInteger(0);  
            passwordRetryCache.put(username, retryCount);  
        }
        //retryCount.incrementAndGet()自增：count + 1
        if (retryCount.incrementAndGet() > 5) {  
            // if retry count > 5 throw  超过5次 锁定
            throw new ExcessiveAttemptsException("username:"+username+" tried to login more than 5 times in period");
        }  
        //否则走判断密码逻辑
        boolean matches = super.doCredentialsMatch(token, info);  
        if (matches) {  
            // clear retry count  清楚ehcache中的count次数缓存
            passwordRetryCache.remove(username);  
        }  
        return matches;  
    }  
} 