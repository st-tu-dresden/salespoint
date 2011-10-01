package org.salespointframework.core.user;

import org.salespointframework.util.ArgumentNullException;

/**
 * TODO
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 * 
 * @param <T> Base type of the users managed by the UserManager; has to
 *            implement {@link User}
 * 
 */
//TODO @throws and shit
public interface UserManager<T extends User>
{

	/**
	 * Adds a {@link User} to the <code>UserManager</code> if the user not
	 * already exists.
	 * 
	 * @param user
	 *           {@link User} to be stored.
	 * @throws ArgumentNullException if user is null           
	 */
	void add(T user);

	/**
	 * 
	 * @param userIdentifier 
	 * @return true if removal was successful, otherwise false
	 * @throws ArgumentNullException is userIdentifier is null
	 */
	boolean remove(UserIdentifier userIdentifier);

	/**
	 * Checks if the UserManager contains a {@link User}
	 * @param userIdentifier the {@link UserIdentifier} of the {@link User}
	 * @return true if UserManager contains the {@link User}, otherwise false
	 * @throws ArgumentNullException is userIdentifier is null
	 */
	boolean contains(UserIdentifier userIdentifier);

	/**
	 * Logs the user on. When a user is logged on, an association is made
	 * between the user, and a supplied token.
	 * 
	 * @param user
	 *            {@link User} to be logged on.
	 * @param token
	 *            token, with which the <code>user</code> will be associated.
	 * @return TODO
	 */
	boolean logOn(T user, Object token);

	/**
	 * Logs a user off. The user associated with <code>token</code> on log on is
	 * logged off.
	 * 
	 * @param token
	 *            token, with which a user was logged on.
	 */
	void logOff(Object token);

	/**
	 * Get a logged-on user by its token.
	 * 
	 * @param clazz
	 *            the specific class type of the requested user.
	 * @param token
	 *            token, which was associated with the user, on log on.
	 * @return the user which is associated with token and of class type
	 *         <code>clazz</code>.
	 * @throws ClassCastException
	 *             , if user associated with <code>token</code> is not of type
	 *             <code>clazz</code>.
	 */
	<E extends T> E getUserByToken(Class<E> clazz, Object token);

	/**
	 * Get a user by its unique identifier.
	 * 
	 * @param clazz
	 *            the class type of the user.
	 * @param userIdentifier
	 *            the identifier of the user
	 * @return the user of class type <code>clazz</code> with the identifier
	 *         equal to <code>userIdentifier</code>
	 */
	<E extends T> E get(Class<E> clazz, UserIdentifier userIdentifier);

	/**
	 * Get all users of class type <code>clazz</code>.
	 * 
	 * @param clazz
	 *            all users of this type will be returned.
	 * @return all users of class type <code>clazz</code>
	 */
	<E extends T> Iterable<E> find(Class<E> clazz);

}