package org.salespointframework.core.user;


// TODO Naming
public interface LoginLogoutManager<T> {
	
	// TODO welche login Methode?
	void login(User user, T token);
	boolean login(User user, String password, T token);
	boolean loggedIn(T token);
	void logout(T token);
	User getUser(T token);
}
