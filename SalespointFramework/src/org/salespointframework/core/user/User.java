package org.salespointframework.core.user;

/**
 * This interface is the base interface, implemented by all user classes.
 * 
 * @author Christopher Bellmann
 * 
 */
public interface User
{

	/**
	 * Checks a given password against the user's password.
	 * 
	 * @param password
	 *            The password to be checked.
	 * @return <code>true</code>, if the password matches, <code>false</code>
	 *         otherwise.
	 */
	public boolean verifyPassword(String password);

	/**
	 * Get the unique identifier of this <code>User</code>.
	 * 
	 * @return the <code>UserIdentifier</code> of this <code>User</code>
	 */
	public UserIdentifier getUserIdentifier();

	/**
	 * Changes the password of the <code>User</code> to <code>newPassword</code>
	 * . Before the change is made, <code>oldPassword</code> is checked against
	 * the current password. If the check is successful, the password is
	 * changed.
	 * 
	 * @param newPassword
	 *            new password.
	 * @param oldPassword
	 *            old password, which is checked against the current password.
	 */
	public boolean changePassword(String newPassword, String oldPassword);
}
