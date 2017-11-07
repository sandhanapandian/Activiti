package com.finactivity.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class MyAppConfig {

	@Autowired
	private Environment env;

	@Bean
	public FilterRegistrationBean authorizationFilter() {
		FilterRegistrationBean filterRegBean = new FilterRegistrationBean();
		filterRegBean.setFilter(new SecurityTrackingFilter());
		List<String> urlPatterns = new ArrayList<String>();
		urlPatterns.add("/*");
		filterRegBean.setUrlPatterns(urlPatterns);
		return filterRegBean;
	}


}
