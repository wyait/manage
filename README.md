# manage
spring boot + mybatis + shiro + layui 搭建的后台权限管理系统  

[ **wyait-manage** ](https://github.com/wyait/manage.git)：【博客】[spring boot 1.5.9 + mybatis + shiro + layui 搭建的后台权限管理系统；http://blog.51cto.com/wyait/2082803](http://blog.51cto.com/wyait/2082803)；   

[ **wyait-manage-1.2.0** ](https://github.com/wyait/manage.git)：【博客】[springboot + shiro之登录人数限制、登录判断重定向、session时间设置；http://blog.51cto.com/wyait/2107423](http://blog.51cto.com/wyait/2107423)；
本项目是基于[spring boot + mybatis + shiro + layui 后台权限管理系统](http://blog.51cto.com/wyait/2082803)开发的，新增完善了实现了:
1. shiro并发登陆人数控制（超出登录用户最大配置数量，清理用户）功能;
2. 解决父子页面判断用户未登录之后，重定向到页面中嵌套显示登录界面问题；
3. 解决ajax请求，判断用户未登录之后，重定向到登录页面问题；
4. 解决完成了功能1，导致的session有效时间冲突问题。   

**wyait.sql**: sql数据库语句源码
