package org.salespointframework.web;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.LoginLogoutManager;
import org.salespointframework.core.user.User;

// TODO naming
public enum WebLoginLogoutManager implements LoginLogoutManager<HttpSession> {
	INSTANCE;

	private final String attributeName = "org.salespointframework.loggedInUser";
	
	@Override
	public void login(User user, HttpSession session) {
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(session, "session must not be null");
		
		session.setAttribute(attributeName, user);
	}
	
	@Override
	public boolean login(User user, String password, HttpSession session) {
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(session, "session must not be null");
		
		if(user.verifyPassword(password)) {
			session.setAttribute(attributeName, user);
		}
		
		return false;
		
	}
	
	@Override
	public boolean loggedIn(HttpSession session) {
		Objects.requireNonNull(session, "session must not be null");
		return session.getAttribute(attributeName) != null;
	}

	@Override
	public void logout(HttpSession session) {
		Objects.requireNonNull(session, "session must not be null");
		session.invalidate();
		
	}

	@Override
	public User getUser(HttpSession session) {
		Objects.requireNonNull(session, "session must not be null");
		User user = (User) session.getAttribute(attributeName);
		return user;
	}





}
