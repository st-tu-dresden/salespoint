package org.salespointframework.web;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.User;
import org.salespointframework.util.LoginLogoutManager;

public enum WebLoginLogoutManager implements LoginLogoutManager<HttpSession> {
	INSTANCE;

	private final String attributeName = "org.salespointframework.loggedInUser";
	
	@Override
	public void login(User user, HttpSession session) {
		session.setAttribute(attributeName, user);
	}
	
	@Override
	public boolean loggedIn(HttpSession session) {
		return session.getAttribute(attributeName) != null;
	}

	@Override
	public void logout(HttpSession session) {
		session.removeAttribute(attributeName);
		session.invalidate(); // TODO nötig?
		
	}

	@Override
	public User getUser(HttpSession session) {
		User user = (User) session.getAttribute(attributeName);
		return user;
	}



}
