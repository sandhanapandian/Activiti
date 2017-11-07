package com.finactivity.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.finactivity.model.ActUser;
import com.finactivity.service.AccessService;

@RestController
@RequestMapping(value = "/api/v1/access")
public class AccessController {

	@Autowired
	private AccessService accessService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public Object getAccess(@RequestBody ActUser data, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		
		return accessService.getAccess(data, httpRequest, httpResponse);
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public Object userRegistration(@RequestBody ActUser data, HttpServletRequest httpRequest, 
			HttpServletResponse httpResponse){
		return accessService.userRegistration(data, httpRequest, httpResponse);
	}

}