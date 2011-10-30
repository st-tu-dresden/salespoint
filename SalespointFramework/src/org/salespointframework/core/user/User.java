package org.salespointframework.core.user;

import org.salespointframework.util.ArgumentNullException;

//TODO gibt ja nur noch eine "implemented by all user classes."
//TODO mehr comment?

/**
 * This interface is the base interface, implemented by all user classes.
 * 
 * 
 * @author Christopher Bellmann
 * 
 */
public interface User
{
	/**
	 * Get the unique identifier of this <code>User</code>.
	 * 
	 * @return the <code>UserIdentifier</code> of this <code>User</code>
	 */
	public UserIdentifier getIdentifier();

	/**
	 * Checks a given password against the user's password.
	 * 
	 * @param password
	 *            The password to be checked.
	 * @return <code>true</code>, if the password matches, <code>false</code>
	 *         otherwise.
	 * @throws ArgumentNullException if password is null
	 */
	public boolean verifyPassword(String password);
	
	/**
	 * Changes the password of the <code>User</code> to <code>newPassword</code>
	 * 
	 * @param newPassword
	 *            new password.
	 * @throws ArgumentNullException if newPassword is null
	 */
	public void changePassword(String newPassword);

	/**
	 * Adds a {@link UserCapability} to a <code>User</code>
	 * 
	 * @param capability
	 *            <code>capability</code> which the <code>user</code> will
	 *            receive.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * @throws ArgumentNullException if capability is null
	 */
	boolean addCapability(UserCapability capability);

	/**
	 * Removes a {@link UserCapability} from a <code>User</code>.
	 * 
	 * @param capability
	 *            <code>capability</code> which will be removed from
	 *            <code>user</code>
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 * @throws ArgumentNullException if capability is null
	 */
	boolean removeCapability(UserCapability capability);

	/**
	 * Checks if a <code>User</code> has a specific {@link UserCapability}
	 * 
	 * @param capability
	 *            {@link UserCapability} for which the <code>user</code>
	 *            will be checked for.
	 * @return <code>true</code> if <code>capability</code> was granted to
	 *         <code>user</code>
	 * @throws ArgumentNullException if capability is null
	 */
	boolean hasCapability(UserCapability capability);
	
	/**
	 * 
	 * @return An <code>Iterable/code> with all {@link UserCapability}s of the user 
	 */
	Iterable<UserCapability> getCapabilities();
}
