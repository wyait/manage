# manage
spring boot + mybatis + shiro + layui 搭建的后台权限管理系统  

[ **wyait-manage** ](https://github.com/wyait/manage.git)：【博客】[spring boot + mybatis + shiro + layui 搭建的后台权限管理系统；http://blog.51cto.com/wyait/2082803](http://blog.51cto.com/wyait/2082803)；   

[ **wyait-manage-1.2.0** ](https://github.com/wyait/manage.git)：【博客】[springboot + shiro之登录人数限制、登录判断重定向、session时间设置；http://blog.51cto.com/wyait/2107423](http://blog.51cto.com/wyait/2107423)；  
本项目是基于[spring boot + mybatis + shiro + layui 后台权限管理系统](http://blog.51cto.com/wyait/2082803)开发的，新增功能:
1. shiro并发登陆人数控制（超出登录用户最大配置数量，清理用户）功能；
2. 解决在父子页面中，判断用户未登录之后，重定向到登录页面嵌套显示问题；
3. 解决ajax请求，判断用户未登录之后，如何重定向到登录页面问题；
4. 解决使用并完成了功能1，导致的session有效时间冲突问题。   

【博客】[spring boot + shiro 动态更新用户信息：http://blog.51cto.com/wyait/2112200](http://blog.51cto.com/wyait/2112200)；  

5. 更新用户操作，通过version字段来保证数据一致；
6. 新增通过拦截器实现动态更新用户信息（同步更新在线用户信息）；
7. 新增登录成功后默认页面home.html；  

【博客】[springboot + shiro 权限注解、统一异常处理、请求乱码解决 ：http://blog.51cto.com/wyait/2125708](http://blog.51cto.com/wyait/2125708)；  

8. 新增shiro权限注解；  
9. 请求乱码问题解决；  
10. 统一异常处理。

**wyait.sql**: sql数据库语句源码




**2018-11-23补充**:spring boot高版本中，关于redis、druid、自定义error配置都有更新变动，大家更新版本的时候注意！！！
