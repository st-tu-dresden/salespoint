package org.salespointframework.util;

import org.salespointframework.core.user.User;

public interface LoginLogoutManager<T> {
	void login(User user, T token);
	boolean loggedIn(T token);
	void logout(T token);
	User getUser(T token);
}
