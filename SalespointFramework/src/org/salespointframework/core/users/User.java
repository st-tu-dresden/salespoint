package org.salespointframework.core.users;

/**
 * This the User Interface. A User should have an username and a password.
 * @author Christopher Bellmann
 *
 */
public interface User {
	
	/**
	 * Verfies if the given password is equal with the user's password.
	 * @param password you want to check
	 * @return true if password is correct
	 */
	public boolean verifyPassword(String password);
	
	/**
	 * Changes Password of the User to newPassword if oldPassword is correct
	 * @param newPassword the Password you want to give the User
	 * @param oldPassword the Password the User already has (should be checked)
	 */
	public boolean changePassword(String newPassword, String oldPassword);
	
	/**
	 * 
	 * @return the users name
	 */
	public String getUsername();

}

