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
public interface UserManager {

    /**
     * Adds a {@link User} to the <code>UserManager</code> if the user not
     * already exists.
     * 
     * @param user
     *            {@link User} to be stored.
     * @throws NullPointerException
     *             if <code>user</code> is <code>null</code>.
     */
    void add(User user);

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
     * Gets a user by its unique identifier.
     * 
     * @param <T>
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
    <T extends User> T get(Class<T> clazz, UserIdentifier userIdentifier);

    /**
     * Gets all users of class type <code>clazz</code>.
     * 
     * @param <T>
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
    <T extends User> Iterable<T> find(Class<T> clazz);

}