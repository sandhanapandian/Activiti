package com.finactivity.configuration;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import lombok.extern.log4j.Log4j;


@Log4j
@WebFilter("/*")
public class SecurityTrackingFilter implements Filter {
 
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try {
			SecurityContext context = SecurityContextHolder.getContext();
			if (context != null) {
				Authentication authentication = context.getAuthentication();
				if (authentication != null
						&& authentication.getPrincipal() != null
						&& authentication.getPrincipal() instanceof Integer) {
				}
			}
			MDC.put("TRACKID", "12345t");	
		} catch (Throwable e) {
			log.error("Exception ", e);
		} 
		chain.doFilter(request, response);
		MDC.clear();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}