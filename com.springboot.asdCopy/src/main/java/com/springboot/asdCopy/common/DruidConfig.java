package com.springboot.asdCopy.common;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;


/**
 * 由于目前Spring Boot中默认支持的连接池只有 dbcp、dbcp2、 tomcat、hikari连接池,
 * Druid 暂时不在Spring Boot 中的直接支持，故需要进行配置信息的定制
 * @author Administrator
 *
 */
@Configuration
public class DruidConfig {
//	private static final String DB_PREFIX = "spring.datasource";
	
	@SuppressWarnings("rawtypes")
	@Bean
	public ServletRegistrationBean druidServlet() {
		@SuppressWarnings("unchecked")
		ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        
		//ip白名单
		bean.addInitParameter("allow", "");
		// IP黑名单(共同存在时，deny优先于allow)
        bean.addInitParameter("deny", "");
        //控制台管理 用户
        bean.addInitParameter("loginUsername", "");
        bean.addInitParameter("loginPassword", "");
        //是否能够重置数据 禁用HTML页面上的“Reset All”功能
        bean.addInitParameter("resetEnable", "");
        return bean;
	}
	
	@SuppressWarnings("rawtypes")
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		@SuppressWarnings("unchecked")
		FilterRegistrationBean filterBean = new FilterRegistrationBean(new WebStatFilter());
        
		filterBean.addUrlPatterns("/*");
		filterBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterBean;
	}
	
	// 解决 spring.datasource.filters=stat,wall,log4j 无法正常注册进去
	/*
	 * @ConfigurationProperties(prefix = DB_PREFIX)
	 * 
	 * @Getter
	 * 
	 * @Setter class IDataSourceProperties { private String url; private String
	 * username; private String password; private String driverClassName; private
	 * int initialSize; private int minIdle; private int maxActive; private int
	 * maxWait; private int timeBetweenEvictionRunsMillis; private int
	 * minEvictableIdleTimeMillis; private String validationQuery; private boolean
	 * testWhileIdle; private boolean testOnBorrow; private boolean testOnReturn;
	 * private boolean poolPreparedStatements; private int
	 * maxPoolPreparedStatementPerConnectionSize; private String filters; private
	 * String connectionProperties;
	 * 
	 * @Bean //声明其为Bean实例
	 * 
	 * @Primary //在同样的DataSource中，首先使用被标注的DataSource public DataSource dateSource()
	 * { DruidDataSource datasource = new DruidDataSource(); datasource.setUrl(url);
	 * datasource.setUsername(username); datasource.setPassword(password);
	 * datasource.setDriverClassName(driverClassName);
	 * datasource.setInitialSize(initialSize); datasource.setMinIdle(minIdle);
	 * datasource.setMaxActive(maxActive); datasource.setMaxWait(maxWait);
	 * datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	 * datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	 * datasource.setValidationQuery(validationQuery);
	 * datasource.setTestWhileIdle(testWhileIdle);
	 * datasource.setTestOnBorrow(testOnBorrow);
	 * datasource.setTestOnReturn(testOnReturn);
	 * datasource.setPoolPreparedStatements(poolPreparedStatements);
	 * datasource.setMaxPoolPreparedStatementPerConnectionSize(
	 * maxPoolPreparedStatementPerConnectionSize); try {
	 * datasource.setFilters(filters); } catch (SQLException e) {
	 * System.err.println("druid configuration initialization filter: " + e); }
	 * datasource.setConnectionProperties(connectionProperties); return datasource;
	 * } }
	 */
	
}	
	
	
