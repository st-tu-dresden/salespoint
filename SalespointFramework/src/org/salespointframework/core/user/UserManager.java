package org.salespointframework.core.user;

/**
 * The <code>UserManager</code> is an interface that provides methods to store
 * and manage users that should be able to interact with the shop. The
 * <code>UserManager</code> is also used to log on and off users from the
 * system.
 * 
 * @author Christopher Bellmann
 * @author Paul Henke
 * @author Hannes Weissbach
 * 
 * @param <T>
 *            Base type of the users managed by the UserManager; has to
 *            implement {@link User}
 * 
 */
public interface UserManager<T extends User> {

    /**
     * Adds a {@link User} to the <code>UserManager</code> if the user not
     * already exists.
     * 
     * @param user
     *            {@link User} to be stored.
     * @throws NullPointerException
     *             if <code>user</code> is <code>null</code>.
     */
    void add(T user);

    /**
     * Removes the user with the given identifier from this
     * <code>UserManager</code>. If the user is logged on, he will be logged off
     * before removal.
     * 
     * @param userIdentifier
     *            The identifier of the user who should be removed.
     * 
     * @return <code>true</code> if removal was successful, <code>false</code>
     *         otherwise.
     * @throws NullPointerException
     *             if <code>userIdentifier</code> is <code>null</code>.
     */
    boolean remove(UserIdentifier userIdentifier);

    /**
     * Checks if the UserManager contains a {@link User}
     * 
     * @param userIdentifier
     *            the {@link UserIdentifier} of the {@link User}
     * @return true if UserManager contains the {@link User}, otherwise false
     * @throws NullPointerException
     *             is userIdentifier is null.
     */
    boolean contains(UserIdentifier userIdentifier);

    /**
     * Logs the user on. When a user is logged on, an association is made
     * between the user, and a supplied token. Only users who have been added to
     * this <code>UserManager</code> before can be logged on. If the given user
     * does not exist in this <code>UserManager</code> <code>false</code> will
     * be returned.
     * 
     * @param user
     *            {@link User} to be logged on.
     * @param token
     *            token, with which the <code>user</code> will be associated.
     * @return <code>true</code> if log on was successful, <code>false</code>
     *         otherwise.
     * 
     * @throws NullPointerException
     *             if <code>user</code> or <code>token</code> or both are
     *             <code>null</code>.
     */
    boolean login(T user, Object token);

    /**
     * Logs a user off. The user associated with <code>token</code> on log on is
     * logged off.
     * 
     * @param token
     *            token, with which a user was logged on.
     * 
     * @throws NullPointerException
     *             if <code>token</code> is <code>null</code>.
     */
    void logout(Object token);

    /**
     * Gets a logged-on user by its token.
     * 
     * @param <E>
     *            common super type of the user returned
     * @param clazz
     *            Class object corresponding to the type of the user to be
     *            returned, has to be a sub-class of <code>T</code>
     * 
     * @param token
     *            token, which was associated with the user, on log on.
     * @return the user which is associated with <code>token</code> and of class
     *         type <code>clazz</code> or <code>null</code> if no user is
     *         associated with the <code>token</code>
     * @throws ClassCastException
     *             if user associated with <code>token</code> is not of type
     *             <code>clazz</code>.
     * @throws NullPointerException
     *             if <code>clazz</code> or <code>token</code> or both are
     *             <code>null</code>.
     */
    <E extends T> E getUserByToken(Class<E> clazz, Object token);

    /**
     * Gets a user by its unique identifier.
     * 
     * @param <E>
     *            common super type of the user returned
     * @param clazz
     *            Class object corresponding to the type of the user to be
     *            returned, has to be a sub-class of <code>T</code>
     * 
     * @param userIdentifier
     *            the identifier of the user
     * 
     * @return the user of class type <code>clazz</code> with the identifier
     *         equal to <code>userIdentifier</code> or <code>null</code> if no
     *         user exists with the given identifier
     * 
     * @throws NullPointerException
     *             if <code>clazz</code> or <code>userIdentifer</code> or both
     *             are <code>null</code>.
     */
    <E extends T> E get(Class<E> clazz, UserIdentifier userIdentifier);

    /**
     * Gets all users of class type <code>clazz</code>.
     * 
     * @param <E>
     *            common super type of the users returned
     * @param clazz
     *            Class object corresponding to the type of the users to be
     *            returned, has to be a sub-class of <code>T</code>
     * 
     * @return all users of class type <code>clazz</code>
     * 
     * @throws NullPointerException
     *             if <code>clazz</code> is <code>null</code>.
     */
    <E extends T> Iterable<E> find(Class<E> clazz);

}