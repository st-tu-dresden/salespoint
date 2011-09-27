package org.salespointframework.core.user;

/**
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 * 
 * @param <T>
 */

public interface UserManager<T extends User>
{

	/**
	 * Adds a <code>User</code> to the <code>UserManager</code> if the user not
	 * already exists.
	 * 
	 * @param user
	 *            <code>User</code> to be stored.
	 */
	void add(T user);

	boolean remove(UserIdentifier userIdentifier);

	boolean contains(UserIdentifier userIdentifier);

	/**
	 * Adds a <code>UserCapability</code> to a <code>User</code>
	 * 
	 * @param user
	 *            <code>User</code> which will receive the
	 *            <code>UserCapability</code>
	 * @param userCapability
	 *            <code>UserCapability</code> which the <code>user</code> will
	 *            receive.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 */
	boolean addCapability(T user, UserCapability userCapability);

	/**
	 * Removes a <code>UserCapability<code> from a <code>User</code>.
	 * 
	 * @param user
	 *            <code>User</code> from which the <code>UserCapability</code>
	 *            will be removed.
	 * @param userCapability
	 *            <code>UserCapability</code> which will be removed from
	 *            <code>user</code>
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 */
	boolean removeCapability(T user, UserCapability userCapability);

	/**
	 * Checks if a <code>User</code> has a specific <code>UserCapability</code>
	 * 
	 * @param user
	 *            <code>User</code> which will be checked if
	 *            <code>userCapability</code> was granted.
	 * @param userCapability
	 *            <code>UserCabability</code> for which the <code>user</code>
	 *            will be checked for.
	 * @return <code>true</code> if <code>userCapability</code> was granted to
	 *         <code>user</code>
	 */
	boolean hasCapability(T user, UserCapability userCapability);

	/**
	 * Logs the user on. When a user is logged on, an association is made
	 * between the user, and a supplied token.
	 * 
	 * @param user
	 *            <code>User<code> to be logged on.
	 * @param token
	 *            token, with which the <code>user</code> will be associated.
	 */
	void logOn(T user, Object token);

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