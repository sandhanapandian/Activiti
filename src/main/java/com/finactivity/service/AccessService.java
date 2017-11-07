package com.finactivity.service;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finactivity.model.ActUser;
import com.finactivity.repository.UserRepository;

import lombok.extern.log4j.Log4j;

/**
 * @author Sandhana Pandian RR created on Date : NOV 07, 2017-14:44:08 PM last
 *
 */
@Service
@Log4j
public class AccessService {
	
	@Autowired
	SessionAuthenticationStrategy sessionAuthenticationStrategy;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ActivitiUtilService activitiUtilService;

	/*Login to get session access */
	public Object getAccess(ActUser requestData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {

        ObjectMapper mapper = new ObjectMapper();
        try {
			log.info("ReQ DATA: - "+mapper.writeValueAsString(requestData));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
		
		HttpSession session = httpRequest.getSession(true);

		if (session != null) {
			ActUser userEntity = null;
			userEntity = userRepository.findByEmail(requestData.getEmail());
			if (userEntity == null) {
				return "Invalid UserName Or Password";
			}
			log.info("Login UserName :: ---> " + requestData.getEmail()+""+userEntity.getPassword());

			if (!requestData.getPassword().equals( userEntity.getPassword())) {
				return "Invalid UserName Or Password";
			}
			List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			Authentication authentication = new UsernamePasswordAuthenticationToken(
					userEntity, session.getId(), authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			sessionAuthenticationStrategy.onAuthentication(authentication, httpRequest, httpResponse);

			return userEntity;

		} else {
			return "Session Cannot be able to Create";
		}

	}
	/*Session Invalidate*/
	public void logout(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(httpRequest, httpResponse, auth);
		}
	}
	/*Register*/
	@Transactional
	public Object userRegistration(ActUser requestData, HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		requestData = userRepository.save(requestData);
		
		activitiUtilService.createUser(requestData);
		return getAccess(requestData, httpRequest, httpResponse);
		
	}
}