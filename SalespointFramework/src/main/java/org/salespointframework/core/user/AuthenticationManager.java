package org.salespointframework.core.user;

/**
 * Interface for managing login and logout of {@link User}s 
 * @param <T> a generic parameter
 * @author Paul Henke
 *
 */
public interface AuthenticationManager<T> {
	
	/**
	 * Associate a {@link User} with a given token.
	 * @param user the User to be logged in
	 * @param token the token to associate with the user
	 * @throws NullPointerException if user or token is null
	 */
	void login(User user, T token);
	
	/**
	 * Associate a {@link User} with a token.
	 * Calls {@code User.verifyPassword()} on the user object.
	 * @param user the User to be logged in
	 * @param password the password of the user
	 * @param token the token to associate with the user
	 * @return true if the user could be logged in with the given password, false if user is null or password is wrong 
	 * @throws NullPointerException if password or token is null
	 */
	boolean login(User user, String password, T token);
	
	/**
	 * Checks if a {@link User} is logged in
	 * @param token the associated token of the user
	 * @return true if the associated user is logged in, false otherwise
	 * @throws NullPointerException if token is null
	 */
	boolean loggedIn(T token);
	
	/**
	 * Dissociates the given token from the associated {@link User}.
	 * @param token the associated token of the user
	 * @throws NullPointerException if token is null
	 */
	void logout(T token);
	
	/**
	 * Retrieve the {@link User} currently associated with the token.
	 * @param token the associated token of the user
	 * @return the associated user of the token, null is returned if no user is associated with the token 
	 * @throws NullPointerException if token is null
	 */
	User getUser(T token);
}
