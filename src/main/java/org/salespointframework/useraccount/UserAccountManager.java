package org.salespointframework.useraccount;

import java.util.Optional;

/**
 * Central service to manager {@link UserAccount} instances.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public interface UserAccountManager {

	/**
	 * Creates a new {@link UserAccount}
	 * 
	 * @param userName the unique name of the user, the name is also used as loginname
	 * @param password the password
	 * @param roles zero or more roles
	 * @return an UserAccount
	 * @throws NullPointerException if userName, password or roles is null
	 */
	UserAccount create(String userName, String password, Role... roles);

	/**
	 * Returns an {@link UserAccount} for a given identifier
	 * 
	 * @param userAccountIdentifier
	 * @return
	 */
	Optional<UserAccount> get(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Saves the {@link UserAccount}
	 * 
	 * @param userAccount
	 * @return
	 */
	UserAccount save(UserAccount userAccount);

	/**
	 * Enables the {@link UserAccount}
	 * 
	 * @param userAccountIdentifier
	 */
	void enable(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Disables the {@link UserAccount}
	 * 
	 * @param userAccountIdentifier
	 */
	void disable(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Changes the passwort of the {@link UserAccount}
	 * 
	 * @param userAccount
	 * @param password
	 */
	void changePassword(UserAccount userAccount, String password);

	/**
	 * Checks if an {@link UserAccount} exists
	 * 
	 * @param userAccountIdentifier
	 * @return
	 */
	boolean contains(UserAccountIdentifier userAccountIdentifier);

	/**
	 * Finds all {@link UserAccount}s
	 * 
	 * @return
	 */
	Iterable<UserAccount> findAll();

	/**
	 * Finds only enabled {@link UserAccount}s
	 * 
	 * @return
	 */
	Iterable<UserAccount> findEnabled();

	/**
	 * Finds only disabled {@link UserAccount}s
	 * 
	 * @return
	 */
	Iterable<UserAccount> findDisabled();
}
