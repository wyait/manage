package com.wyait.manage.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @项目名称：wyait-common
 * @包名：com.wyait.manage.config
 * @类描述：数据源配置
 * @创建人：wyait
 * @创建时间：2018-02-27 13:33
 * @version：V1.0
 */
@Configuration
//指明了扫描dao层，并且给dao层注入指定的SqlSessionTemplate
@MapperScan(basePackages = "com.wyait.manage.dao", sqlSessionTemplateRef  = "testSqlSessionTemplate")
public class DataSourceConfig {
	/**
	 * 创建datasource对象
	 * @return
	 */
	@Bean(name = "testDataSource")
	@ConfigurationProperties(prefix = "slave.datasource.test")// prefix值必须是application.properteis中对应属性的前缀
	@Primary
	public DataSource testDataSource() {
		return DataSourceBuilder.create().build();
	}

	/**
	 * 创建sql工程
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "testSqlSessionFactory")
	@Primary
	public SqlSessionFactory testSqlSessionFactory(@Qualifier("testDataSource") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		//对应mybatis.type-aliases-package配置
		bean.setTypeAliasesPackage("com.wyait.manage.pojo");
		//对应mybatis.mapper-locations配置
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
		//开启驼峰映射
		bean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
		return bean.getObject();
	}

	/**
	 * 配置事务管理
	 * @param dataSource
	 * @return
	 */
	@Bean(name = "testTransactionManager")
	@Primary
	public DataSourceTransactionManager testTransactionManager(@Qualifier("testDataSource") DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * sqlSession模版，用于配置自动扫描pojo实体类
	 * @param sqlSessionFactory
	 * @return
	 * @throws Exception
	 */
	@Bean(name = "testSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("testSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
