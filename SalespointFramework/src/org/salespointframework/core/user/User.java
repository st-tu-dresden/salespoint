package org.salespointframework.core.user;

import org.salespointframework.util.ArgumentNullException;

//TODO comment

/**
 * User interface.
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
	 * @return the {@link UserIdentifier} of this <code>User</code>
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
	 * Adds a {@link Capability} to a <code>User</code>
	 * 
	 * @param capability
	 *            <code>capability</code> which the <code>user</code> will
	 *            receive.
	 * @return <code>true</code> if successful, <code>false</code> otherwise.
	 * @throws ArgumentNullException if capability is null
	 */
	boolean addCapability(Capability capability);

	/**
	 * Removes a {@link Capability} from a <code>User</code>.
	 * 
	 * @param capability
	 *            <code>capability</code> which will be removed from
	 *            <code>user</code>
	 * @return <code>true</code> if successful, <code>false</code> otherwise
	 * @throws ArgumentNullException if capability is null
	 */
	boolean removeCapability(Capability capability);

	/**
	 * Checks if a <code>User</code> has a specific {@link Capability}
	 * 
	 * @param capability
	 *            {@link Capability} for which the <code>user</code>
	 *            will be checked for.
	 * @return <code>true</code> if <code>capability</code> was granted to
	 *         <code>user</code>
	 * @throws ArgumentNullException if capability is null
	 */
	boolean hasCapability(Capability capability);
	
	/**
	 * 
	 * @return An <code>Iterable/code> with all {@link Capability}s of the user 
	 */
	Iterable<Capability> getCapabilities();
}
