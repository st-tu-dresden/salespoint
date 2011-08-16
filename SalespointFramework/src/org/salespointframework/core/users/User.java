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
	 * 
	 * @return the users name/ID
	 */
	public UserIdentifier getUserIdentifier();

}

