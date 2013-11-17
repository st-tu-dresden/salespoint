package org.salespointframework.core.useraccount;


/**
 * Central service to manager {@link UserAccount} instances.
 *
 * @author Paul Henke
 * @author Oliver Gierke
 */
public interface UserAccountManager {
	
	UserAccount create(UserAccountIdentifier userAccountIdentifier, String password, Role... roles);
	
	UserAccount get(UserAccountIdentifier userAccountIdentifier);
	
	UserAccount save(UserAccount userAccount);
	
	void enable(UserAccountIdentifier userAccountIdentifier);
	
	void disable(UserAccountIdentifier userAccountIdentifier);
	
	void changePassword(UserAccount userAccount, String password);
	
	boolean contains(UserAccountIdentifier userAccountIdentifier);
	
	Iterable<UserAccount> findAll();
	
	Iterable<UserAccount> findEnabled();
	
	Iterable<UserAccount> findDisabled();
}
