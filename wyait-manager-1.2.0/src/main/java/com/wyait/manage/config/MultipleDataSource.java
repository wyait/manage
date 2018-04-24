package com.wyait.manage.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @项目名称：wyait-manage
 * @类名称：MultipleDataSource
 * @类描述：创建多数据源注册到Spring中
		     接口：BeanDefinitionRegistryPostProcessor只要是注入bean,
		     接口：接口 EnvironmentAware 重写方法 setEnvironment
		     可以在工程启动时，获取到系统环境变量和application配置文件中的变量。 
		     方法的执行顺序是：
		  setEnvironment()-->postProcessBeanDefinitionRegistry() --> postProcessBeanFactory()
 * @创建人：wyait
 * @创建时间：2017年12月19日 下午2:49:34 
 * @version：
 */
//@Configuration
@SuppressWarnings("unchecked")
public class MultipleDataSource implements BeanDefinitionRegistryPostProcessor,EnvironmentAware{
    //作用域对象.
    private ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
    //bean名称生成器.
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();
    //如配置文件中未指定数据源类型，使用该默认值
    private static final Object DATASOURCE_TYPE_DEFAULT = "com.alibaba.druid.pool.DruidDataSource";
    // 存放DataSource配置的集合;
    private Map<String, Map<String, Object>> dataSourceMap = new HashMap<String, Map<String, Object>>();

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    System.out.println("MultipleDataSourceBeanDefinitionRegistryPostProcessor.postProcessBeanFactory()");
       //设置为主数据源;
       beanFactory.getBeanDefinition("dataSource").setPrimary(true);
       if(!dataSourceMap.isEmpty()){
           //不为空的时候.
           BeanDefinition bd = null;
            Map<String, Object> dsMap = null;
            MutablePropertyValues mpv = null;
            for (Entry<String, Map<String, Object>> entry : dataSourceMap.entrySet()) {
                 bd = beanFactory.getBeanDefinition(entry.getKey());
                 mpv = bd.getPropertyValues();
                 dsMap = entry.getValue();
                 mpv.addPropertyValue("driverClassName", dsMap.get("driverClassName"));
                 mpv.addPropertyValue("url", dsMap.get("url"));
                 mpv.addPropertyValue("username", dsMap.get("username"));
                 mpv.addPropertyValue("password", dsMap.get("password"));
            }
       }
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    System.out.println("MultipleDataSourceBeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry()");
       try {
           if(!dataSourceMap.isEmpty()){
              //不为空的时候，进行注册bean.
              for(Entry<String,Map<String,Object>> entry:dataSourceMap.entrySet()){
                  Object type = entry.getValue().get("type");//获取数据源类型
                  if(type == null){
                     type= DATASOURCE_TYPE_DEFAULT;
                  }
                  registerBean(registry, entry.getKey(),(Class<? extends DataSource>)Class.forName(type.toString()));
              }
           }
       } catch (ClassNotFoundException  e) {
           //异常捕捉.
           e.printStackTrace();
       }
    }
    /**
     * 注意重写的方法 setEnvironment 是在系统启动的时候被执行。
     * 这个方法主要是：加载多数据源配置
     * 从application.properties文件中进行加载;
     */
    @Override
    public void setEnvironment(Environment environment) {
    System.out.println("MultipleDataSourceBeanDefinitionRegistryPostProcessor.setEnvironment()");
       /*
        * 获取application.properties配置的多数据源配置，添加到map中，之后在postProcessBeanDefinitionRegistry进行注册。
        */
       //获取到前缀是"slave.datasource." 的属性列表值.
       RelaxedPropertyResolver propertyResolver = new RelaxedPropertyResolver(environment,"slave.datasource.");
       //获取到所有数据源的名称.
       String dsPrefixs = propertyResolver.getProperty("names");
       String[] dsPrefixsArr = dsPrefixs.split(",");
       for(String dsPrefix:dsPrefixsArr){
           /*
            * 获取到子属性，对应一个map;
            * 也就是这个map的key就是
            * type、driver-class-name等;
            */
           Map<String, Object> dsMap = propertyResolver.getSubProperties(dsPrefix + ".");
           //存放到一个map集合中，之后在注入进行使用.
           dataSourceMap.put(dsPrefix, dsMap);
       }
    }
    
    /**
     * 注册Bean到Spring
     */
    private void registerBean(BeanDefinitionRegistry registry, String name, Class<?> beanClass) {
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(beanClass);
        ScopeMetadata scopeMetadata = this.scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(scopeMetadata.getScopeName());
        // 可以自动生成name
        String beanName = (name != null ? name : this.beanNameGenerator.generateBeanName(abd, registry));
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }
}
