package org.salespointframework.web;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import org.salespointframework.core.user.AuthenticationManager;
import org.salespointframework.core.user.User;

/**
 * {@link HttpSession} based implementation of the {@link AuthenticationManager} interface.
 * @author Paul Henke
 *
 */
public enum WebAuthenticationManager implements AuthenticationManager<HttpSession> {
	INSTANCE;

	private final String attributeName = "org.salespointframework.loggedInUser";
	
	/**
	 * Associate a {@link User} with a given session.
	 * @param user the User to be logged in
	 * @param session the session to associate with the user
	 * @throws NullPointerException if user or session is null
	 */
	@Override
	public void login(User user, HttpSession session) 
	{
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(session, "session must not be null");
		
		session.setAttribute(attributeName, user);
	}
	
	/**
	 * Associate a {@link User} with a session.
	 * {@Code user.verifyPassword()} is called.
	 * @param user the User to be logged in
	 * @param password the password of the user
	 * @param session the session to associate with the user
	 * @return true if the user could be logged in with the given password, false otherwise 
	 * @throws NullPointerException if user, password or session is null
	 */
	@Override
	public boolean login(User user, String password, HttpSession session) 
	{
		Objects.requireNonNull(user, "user must not be null");
		Objects.requireNonNull(password, "password must not be null");
		Objects.requireNonNull(session, "session must not be null");
		
		if(user.verifyPassword(password)) {
			session.setAttribute(attributeName, user);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if a {@link User} is logged in
	 * @param session the associated session of the user
	 * @return true if the associated user is logged in, false otherwise
	 * @throws NullPointerException if session is null
	 */
	@Override
	public boolean loggedIn(HttpSession session) 
	{
		Objects.requireNonNull(session, "session must not be null");
		return session.getAttribute(attributeName) != null;
	}

	/**
	 * Dissociates the given session from the associated {@link User}.
	 * {@code session.invalidate()} is called.
	 * @param session the associated session of the user
	 * @throws NullPointerException if session is null
	 */
	@Override
	public void logout(HttpSession session) 
	{
		Objects.requireNonNull(session, "session must not be null");
		session.invalidate();
		
	}

	/**
	 * Retrieve the {@link User} currently associated with the session.
	 * @param session the associated session of the user
	 * @return the associated user of the session, null is returned if no user is associated with the session 
	 * @throws NullPointerException if session is null
	 */
	@Override
	public User getUser(HttpSession session) 
	{
		Objects.requireNonNull(session, "session must not be null");
		User user = (User) session.getAttribute(attributeName);
		return user;
	}
}
