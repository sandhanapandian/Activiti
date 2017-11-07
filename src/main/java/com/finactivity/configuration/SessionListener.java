package com.finactivity.configuration;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.log4j.Log4j;

@Log4j
public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		
		int inActiveInterval = 12 * 60 * 60;
		session.setMaxInactiveInterval(inActiveInterval);
		log.info("[Session Created] SESSION ID : [ '"+session.getId()+"' ];  InActive TIme Interval : [ '"+inActiveInterval+"' ]");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		log.info("[Session Destroyed] SESSION ID : [ '"+session.getId()+"' ];");
	}
}